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

public class VeterinarioActivity extends AppCompatActivity {
    private TextView tvMascota, tvTotal;
    private RadioGroup rgTipo;
    private Button btnContinuar;
    private BottomNavigationView bottomNav;
    private Mascota mascota;
    private Usuario usuario;
    private ServicioMascota servicioEditar;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinario);

        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        servicioEditar = (ServicioMascota) getIntent().getSerializableExtra("servicio");

        if (mascota == null || usuario == null) {
            finish();
            return;
        }

        // Referencias
        tvMascota = findViewById(R.id.tvMascotaVeterinario);
        tvTotal = findViewById(R.id.tvTotalVeterinario);
        rgTipo = findViewById(R.id.rgTipoVeterinario);
        btnContinuar = findViewById(R.id.btnConfirmarVeterinario);
        bottomNav = findViewById(R.id.bottomNavigationView);

        tvMascota.setText("Para: " + mascota.getNombre());

        // precargar datos cuando se edita el servicio
        if (servicioEditar != null) {
            precargarServicio(servicioEditar);
        }
        rgTipo.setOnCheckedChangeListener((group, checkedId) -> calcularTotal());
        btnContinuar.setOnClickListener(v -> continuarReserva());
        configurarBottomNav();
    }

    // Cálculo del total
    private void calcularTotal() {
        int id = rgTipo.getCheckedRadioButtonId();

        if (id == R.id.rbConsulta) total = 30;
        else if (id == R.id.rbVacunacion) total = 25;
        else if (id == R.id.rbDesparasitacion) total = 20;
        else if (id == R.id.rbRevision) total = 40;

        tvTotal.setText("Total: " + total + " €");
    }
    // selección del servicio
    private void continuarReserva() {
        int selectedId = rgTipo.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona un servicio", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton rb = findViewById(selectedId);
        ServicioMascota servicio;

        if (servicioEditar != null) {
            // edita el servicio manteniendo el ID
            servicio = servicioEditar;
            servicio.setDescripcion(rb.getText().toString());
            servicio.setPrecio(total);
        } else {
            // crea nuevo servicio
            servicio = new ServicioMascota("Veterinario", rb.getText().toString(), "", total
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
    // precarga de servicio
    private void precargarServicio(ServicioMascota servicio) {
        String desc = servicio.getDescripcion();
        if (desc.contains("Consulta")) rgTipo.check(R.id.rbConsulta);
        else if (desc.contains("Vacunación")) rgTipo.check(R.id.rbVacunacion);
        else if (desc.contains("Desparasitación")) rgTipo.check(R.id.rbDesparasitacion);
        else if (desc.contains("Revisión")) rgTipo.check(R.id.rbRevision);
        total = servicio.getPrecio();
        tvTotal.setText("Total: " + total + " €");
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
