package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PeluqueriaActivity extends AppCompatActivity {
    private TextView tvMascota, tvTotal;
    private RadioGroup rgTipo;
    private Button btnContinuar;
    private Mascota mascota;
    private Usuario usuario;
    private double total = 0;
    private BottomNavigationView bottomNav;
    private ServicioMascota servicioEditando;
    private boolean esEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peluqueria);

        tvMascota = findViewById(R.id.tvMascotaPeluqueria);
        tvTotal = findViewById(R.id.tvTotalPeluqueria);
        rgTipo = findViewById(R.id.rgTipoPeluqueria);
        btnContinuar = findViewById(R.id.btnConfirmarPeluqueria);
        bottomNav = findViewById(R.id.bottomNavigationView);
        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        tvMascota.setText("Para: " + mascota.getNombre());

        if (mascota == null || usuario == null) {
            finish();
            return;
        }

        servicioEditando = (ServicioMascota) getIntent().getSerializableExtra("servicio");
        if (servicioEditando != null) {
            esEdicion = true;
            precargarDatos(servicioEditando);
        }
        rgTipo.setOnCheckedChangeListener((group, checkedId) -> calcularTotal());

        btnContinuar.setOnClickListener(v -> continuarReserva());

        configurarBottomNav();
    }
    //calcula el total del servicio
    private void calcularTotal() {
        int id = rgTipo.getCheckedRadioButtonId();
        if (id == R.id.rbCorteBasico) total = 15;
        else if (id == R.id.rbBanoSecado) total = 20;
        else if (id == R.id.rbPremium) total = 30;
        tvTotal.setText("Total: " + total + " €");
    }
    private void continuarReserva() {
        int selectedId = rgTipo.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona un servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton rb = findViewById(selectedId);
        if (esEdicion) {
            // actualiza
            servicioEditando.setDescripcion(rb.getText().toString());
            servicioEditando.setPrecio(total);
            servicioEditando.setFecha("");
            irAReserva(servicioEditando);
        } else {
            // crear el servicio
            ServicioMascota nuevo = new ServicioMascota("Peluquería", rb.getText().toString(), "", total
            );
            irAReserva(nuevo);
        }
    }
    private void irAReserva(ServicioMascota servicio) {
        Intent intent = new Intent(this, ReservasActivity.class);
        intent.putExtra("servicio", servicio);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
    private void precargarDatos(ServicioMascota servicio) {
        tvTotal.setText("Total: " + servicio.getPrecio() + " €");
        if (servicio.getDescripcion().contains("Corte")) {
            rgTipo.check(R.id.rbCorteBasico);
        } else if (servicio.getDescripcion().contains("Baño")) {
            rgTipo.check(R.id.rbBanoSecado);
        } else if (servicio.getDescripcion().contains("Premium")) {
            rgTipo.check(R.id.rbPremium);
        }
    }
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
