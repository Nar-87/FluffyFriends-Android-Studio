package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuarderiaActivity extends AppCompatActivity {
    private RadioGroup rgTipo, rgDuracion;
    private Button btnContinuar;
    private TextView tvTotal, tvMascota;
    private BottomNavigationView bottomNav;
    private Mascota mascota;
    private Usuario usuario;
    private ServicioMascota servicioEditar;
    private int precioBase = 0;
    private int dias = 0;
    private double total = 0;
    private String tipoSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarderia);

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
        btnContinuar = findViewById(R.id.btnConfirmar);
        tvTotal = findViewById(R.id.tvTotal);
        tvMascota = findViewById(R.id.tvMascotaG);
        bottomNav = findViewById(R.id.bottomNavigationView);

        tvMascota.setText("Para: " + mascota.getNombre());
        tvTotal.setText("Total: 0 €");
        btnContinuar.setEnabled(false);

        if (servicioEditar != null) {
            precargarServicio(servicioEditar);
        }
        rgTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbDiaria) {
                precioBase = 30;
                tipoSeleccionado = "Guardería diaria";
            } else if (checkedId == R.id.rbNocturna) {
                precioBase = 45;
                tipoSeleccionado = "Guardería nocturna";
            } else if (checkedId == R.id.rbPremium) {
                precioBase = 60;
                tipoSeleccionado = "Guardería premium";
            }
            calcularTotal();
        });

        rgDuracion.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1Dia) dias = 1;
            else if (checkedId == R.id.rb3Dias) dias = 3;
            else if (checkedId == R.id.rb1Semana) dias = 7;

            calcularTotal();
        });

        btnContinuar.setOnClickListener(v -> continuarReserva());

        configurarBottomNav();
    }
    // cálculo del total
    private void calcularTotal() {
        if (precioBase == 0 || dias == 0) {
            tvTotal.setText("Total: 0 €");
            btnContinuar.setEnabled(false);
            return;
        }
        total = precioBase * dias;
        tvTotal.setText("Total: " + total + " €");
        btnContinuar.setEnabled(true);
    }
    // tipo y duración
    private void continuarReserva() {
        if (precioBase == 0 || dias == 0) {
            Toast.makeText(this, "Selecciona tipo y duración", Toast.LENGTH_SHORT).show();
            return;
        }
        String descripcion = tipoSeleccionado + " (" + dias + " días)";
        ServicioMascota servicio;
        if (servicioEditar != null) {
            // editar
            servicio = servicioEditar;
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(total);
        } else {
            servicio = new ServicioMascota("Guardería", descripcion, "", total
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
        // tipo
        if (desc.contains("diaria")) {
            rgTipo.check(R.id.rbDiaria);
            precioBase = 30;
            tipoSeleccionado = "Guardería diaria";
        } else if (desc.contains("nocturna")) {
            rgTipo.check(R.id.rbNocturna);
            precioBase = 45;
            tipoSeleccionado = "Guardería nocturna";
        } else if (desc.contains("premium")) {
            rgTipo.check(R.id.rbPremium);
            precioBase = 60;
            tipoSeleccionado = "Guardería premium";
        }
        // duración
        if (desc.contains("1 día") || desc.contains("1 días")) {
            rgDuracion.check(R.id.rb1Dia);
            dias = 1;
        } else if (desc.contains("3 días")) {
            rgDuracion.check(R.id.rb3Dias);
            dias = 3;
        } else if (desc.contains("7 días")) {
            rgDuracion.check(R.id.rb1Semana);
            dias = 7;
        }
        // cálculo total
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
