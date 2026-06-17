package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaseosActivity extends AppCompatActivity {

    private RadioGroup rgTipo, rgDuracion, rgFrecuencia;
    private TextView tvTotal, tvMascota;
    private Button btnContinuar;
    private BottomNavigationView bottomNav;
    private Mascota mascota;
    private Usuario usuario;
    private ServicioMascota servicioEditar;
    private int precioBase = 0;
    private int extraDuracion = 0;
    private int frecuencia = 0;
    private double total = 0;
    private String tipoSeleccionado = "";
    private String duracionSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paseos);
        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        servicioEditar = (ServicioMascota) getIntent().getSerializableExtra("servicio");
        if (mascota == null || usuario == null) {
            finish();
            return;
        }
        // Referencias
        rgTipo = findViewById(R.id.rgTipoPaseo);
        rgDuracion = findViewById(R.id.rgDuracionPaseo);
        rgFrecuencia = findViewById(R.id.rgFrecuencia);
        tvTotal = findViewById(R.id.tvTotal);
        tvMascota = findViewById(R.id.tvMascotaPaseos);
        btnContinuar = findViewById(R.id.btnConfirmarPaseo);
        bottomNav = findViewById(R.id.bottomNavigationView);

        tvMascota.setText("Para: " + mascota.getNombre());
        tvTotal.setText("Total: 0 €");
        btnContinuar.setEnabled(false);

        if (servicioEditar != null) {
            precargarServicio(servicioEditar);
        }

        rgTipo.setOnCheckedChangeListener((g, i) -> {
            if (i == R.id.rbIndividual) {
                precioBase = 15;
                tipoSeleccionado = "Paseo individual";
            } else if (i == R.id.rbGrupo) {
                precioBase = 10;
                tipoSeleccionado = "Paseo en grupo";
            }
            calcularTotal();
        });

        rgDuracion.setOnCheckedChangeListener((g, i) -> {
            if (i == R.id.rb30Min) {
                extraDuracion = 5;
                duracionSeleccionada = "30 min";
            } else if (i == R.id.rb60Min) {
                extraDuracion = 10;
                duracionSeleccionada = "60 min";
            }
            calcularTotal();
        });

        rgFrecuencia.setOnCheckedChangeListener((g, i) -> {
            if (i == R.id.rb1Dia) frecuencia = 1;
            else if (i == R.id.rb5Dias) frecuencia = 5;
            else if (i == R.id.rb1Semana) frecuencia = 7;

            calcularTotal();
        });

        btnContinuar.setOnClickListener(v -> continuarReserva());

        configurarBottomNav();
    }
    // Cálculo del total
    private void calcularTotal() {
        if (precioBase == 0 || extraDuracion == 0 || frecuencia == 0) {
            tvTotal.setText("Total: 0 €");
            btnContinuar.setEnabled(false);
            return;
        }
        total = (precioBase + extraDuracion) * frecuencia;
        tvTotal.setText("Total: " + total + " €");
        btnContinuar.setEnabled(true);
    }
    // Continuar reservas
    private void continuarReserva() {
        if (precioBase == 0 || extraDuracion == 0 || frecuencia == 0) {
            Toast.makeText(this, "Completa todas las opciones", Toast.LENGTH_SHORT).show();
            return;
        }
        String descripcion = tipoSeleccionado +
                " (" + duracionSeleccionada + ", " + frecuencia + " días)";

        ServicioMascota servicio;
        if (servicioEditar != null) {
            // edita
            servicio = servicioEditar;
            servicio.setDescripcion(descripcion);
            servicio.setPrecio(total);
        } else {
            servicio = new ServicioMascota("Paseos", descripcion, "", total
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
    // precarga servicios
    private void precargarServicio(ServicioMascota servicio) {
        String desc = servicio.getDescripcion();
        // tipo
        if (desc.contains("individual")) {
            rgTipo.check(R.id.rbIndividual);
            precioBase = 15;
            tipoSeleccionado = "Paseo individual";
        } else if (desc.contains("grupo")) {
            rgTipo.check(R.id.rbGrupo);
            precioBase = 10;
            tipoSeleccionado = "Paseo en grupo";
        }
        // duración
        if (desc.contains("30 min")) {
            rgDuracion.check(R.id.rb30Min);
            extraDuracion = 5;
            duracionSeleccionada = "30 min";
        } else if (desc.contains("60 min")) {
            rgDuracion.check(R.id.rb60Min);
            extraDuracion = 10;
            duracionSeleccionada = "60 min";
        }
        // frecuencia
        if (desc.contains("1 días")) {
            rgFrecuencia.check(R.id.rb1Dia);
            frecuencia = 1;
        } else if (desc.contains("5 días")) {
            rgFrecuencia.check(R.id.rb5Dias);
            frecuencia = 5;
        } else if (desc.contains("7 días")) {
            rgFrecuencia.check(R.id.rb1Semana);
            frecuencia = 7;
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
