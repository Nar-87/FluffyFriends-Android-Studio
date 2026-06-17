package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdiestramientoActivity extends AppCompatActivity {
    private RadioGroup rgTipo, rgSesiones;
    private TextView tvTotal, tvMascota;
    private Button btnContinuar;
    private BottomNavigationView bottomNav;
    private Mascota mascota;
    private Usuario usuario;
    private ServicioMascota servicioEditar;
    private int precioBase = 0;
    private int sesiones = 0;
    private double total = 0;
    private String tipoSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiestramiento);

        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        servicioEditar = (ServicioMascota) getIntent().getSerializableExtra("servicio");

        if (mascota == null || usuario == null) {
            finish();
            return;
        }
        // Referencias
        rgTipo = findViewById(R.id.rgTipoAdiestramiento);
        rgSesiones = findViewById(R.id.rgSesiones);
        tvTotal = findViewById(R.id.tvTotal);
        tvMascota = findViewById(R.id.tvMascotaAdiestramiento);
        btnContinuar = findViewById(R.id.btnConfirmarAdiestramiento);
        bottomNav = findViewById(R.id.bottomNavigationView);

        tvMascota.setText("Para: " + mascota.getNombre());
        tvTotal.setText("Total: 0 €");
        btnContinuar.setEnabled(false);

        if (servicioEditar != null) {
            precargarServicio(servicioEditar);
        }
        rgTipo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBasico) {
                precioBase = 30;
                tipoSeleccionado = "Adiestramiento básico";
            } else if (checkedId == R.id.rbConductual) {
                precioBase = 50;
                tipoSeleccionado = "Adiestramiento conductual";
            } else if (checkedId == R.id.rbAvanzado) {
                precioBase = 70;
                tipoSeleccionado = "Adiestramiento avanzado";
            }
            calcularTotal();
        });
        rgSesiones.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1Sesion) sesiones = 1;
            else if (checkedId == R.id.rb5Sesiones) sesiones = 5;
            else if (checkedId == R.id.rb10Sesiones) sesiones = 10;

            calcularTotal();
        });
        btnContinuar.setOnClickListener(v -> continuarReserva());

        configurarBottomNav();
    }
    // Cálculo total
    private void calcularTotal() {
        if (precioBase == 0 || sesiones == 0) {
            tvTotal.setText("Total: 0 €");
            btnContinuar.setEnabled(false);
            return;
        }
        total = precioBase * sesiones;
        tvTotal.setText("Total: " + total + " €");
        btnContinuar.setEnabled(true);
    }
    // Reservas
    private void continuarReserva() {
        if (precioBase == 0 || sesiones == 0) {
            Toast.makeText(this, "Selecciona tipo y sesiones", Toast.LENGTH_SHORT).show();
            return;
        }
        String descripcion = tipoSeleccionado + " (" + sesiones + " sesiones)";
        ServicioMascota servicio;
        if (servicioEditar != null) {
            // Editar el servicio
            servicio = servicioEditar;
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(total);
        } else {
            servicio = new ServicioMascota("Adiestramiento", descripcion, "", total
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
    // Precargar edición
    private void precargarServicio(ServicioMascota servicio) {
        String desc = servicio.getDescripcion();
        // tipo
        if (desc.contains("básico")) {
            rgTipo.check(R.id.rbBasico);
            precioBase = 30;
            tipoSeleccionado = "Adiestramiento básico";
        } else if (desc.contains("conductual")) {
            rgTipo.check(R.id.rbConductual);
            precioBase = 50;
            tipoSeleccionado = "Adiestramiento conductual";
        } else if (desc.contains("avanzado")) {
            rgTipo.check(R.id.rbAvanzado);
            precioBase = 70;
            tipoSeleccionado = "Adiestramiento avanzado";
        }
        // sesiones
        if (desc.contains("1 sesiones")) {
            rgSesiones.check(R.id.rb1Sesion);
            sesiones = 1;
        } else if (desc.contains("5 sesiones")) {
            rgSesiones.check(R.id.rb5Sesiones);
            sesiones = 5;
        } else if (desc.contains("10 sesiones")) {
            rgSesiones.check(R.id.rb10Sesiones);
            sesiones = 10;
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
