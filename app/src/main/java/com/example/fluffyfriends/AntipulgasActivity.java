package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AntipulgasActivity extends AppCompatActivity {
    private RadioGroup rgTipo, rgDuracion;
    private TextView tvTotal, tvMascota;
    private Button btnContinuar;
    private BottomNavigationView bottomNav;
    private Mascota mascota;
    private Usuario usuario;
    private ServicioMascota servicioEditar;
    private int precioBase = 0;
    private int meses = 0;
    private double total = 0;
    private String tipoSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antipulgas);

        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        servicioEditar = (ServicioMascota) getIntent().getSerializableExtra("servicio");

        if (mascota == null || usuario == null) {
            finish();
            return;
        }

        // Referencias
        rgTipo = findViewById(R.id.rgTipo);
        rgDuracion = findViewById(R.id.rgDuracion);
        tvTotal = findViewById(R.id.tvTotal);
        tvMascota = findViewById(R.id.tvMascotaAntipulgas);
        btnContinuar = findViewById(R.id.btnConfirmarAntipulgas);
        bottomNav = findViewById(R.id.bottomNavigationView);

        tvMascota.setText("Para: " + mascota.getNombre());
        tvTotal.setText("Total: 0 €");
        btnContinuar.setEnabled(false);

        // Precargar edición
        if (servicioEditar != null) {
            precargarServicio(servicioEditar);
        }
        // tipo
        rgTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPipeta) {
                precioBase = 15;
                tipoSeleccionado = "Pipeta antipulgas";
            } else if (checkedId == R.id.rbCollar) {
                precioBase = 30;
                tipoSeleccionado = "Collar antipulgas";
            } else if (checkedId == R.id.rbPastilla) {
                precioBase = 25;
                tipoSeleccionado = "Pastilla antipulgas";
            }
            calcularTotal();
        });
        // duración
        rgDuracion.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1Mes) meses = 1;
            else if (checkedId == R.id.rb3Meses) meses = 3;
            else if (checkedId == R.id.rb6Meses) meses = 6;

            calcularTotal();
        });
        btnContinuar.setOnClickListener(v -> continuarReserva());
        configurarBottomNav();
    }
    // Cálculo
    private void calcularTotal() {
        if (precioBase == 0 || meses == 0) {
            tvTotal.setText("Total: 0 €");
            btnContinuar.setEnabled(false);
            return;
        }

        total = precioBase * meses;
        tvTotal.setText("Total: " + total + " €");
        btnContinuar.setEnabled(true);
    }
    // reserva
    private void continuarReserva() {
        if (precioBase == 0 || meses == 0) {
            Toast.makeText(this, "Selecciona tipo y duración", Toast.LENGTH_SHORT).show();
            return;
        }
        String descripcion = tipoSeleccionado + " (" + meses + " meses)";
        ServicioMascota servicio;
        if (servicioEditar != null) {
            // editar servicio
            servicio = servicioEditar;
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(total);
        } else {
            servicio = new ServicioMascota("Antipulgas", descripcion, "", total
            );
        }
        irAReserva(servicio);
    }
    private void irAReserva(ServicioMascota servicio) {
        Intent intent = new Intent(this, ReservasActivity.class);
        intent.putExtra("servicio", servicio);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
    // precarga de servicios
    private void precargarServicio(ServicioMascota servicio) {
        String desc = servicio.getDescripcion();
        if (desc.contains("Pipeta")) {
            rgTipo.check(R.id.rbPipeta);
            precioBase = 15;
            tipoSeleccionado = "Pipeta antipulgas";
        } else if (desc.contains("Collar")) {
            rgTipo.check(R.id.rbCollar);
            precioBase = 30;
            tipoSeleccionado = "Collar antipulgas";
        } else if (desc.contains("Pastilla")) {
            rgTipo.check(R.id.rbPastilla);
            precioBase = 25;
            tipoSeleccionado = "Pastilla antipulgas";
        }
        if (desc.contains("1 mes")) {
            rgDuracion.check(R.id.rb1Mes);
            meses = 1;
        } else if (desc.contains("3 meses")) {
            rgDuracion.check(R.id.rb3Meses);
            meses = 3;
        } else if (desc.contains("6 meses")) {
            rgDuracion.check(R.id.rb6Meses);
            meses = 6;
        }
        total = servicio.getPrecio();
        tvTotal.setText("Total: " + total + " €");
        btnContinuar.setEnabled(true);
    }
    // Menú inferior
    private void configurarBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomepageActivity.class).putExtra("usuario", usuario));
                return true;
            }
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, EditUsuarioActivity.class).putExtra("usuario", usuario));
                return true;
            }
            if (id == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
