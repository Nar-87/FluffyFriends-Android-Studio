package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServicesActivity extends AppCompatActivity {
    private Mascota mascota;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (mascota == null || usuario == null) {
            finish();
            return;
        }

        abrir(R.id.cardGuarderia, GuarderiaActivity.class);
        abrir(R.id.cardPeluqueria, PeluqueriaActivity.class);
        abrir(R.id.cardVeterinario, VeterinarioActivity.class);
        abrir(R.id.cardAdiestramiento, AdiestramientoActivity.class);
        abrir(R.id.cardPaseos, PaseosActivity.class);
        abrir(R.id.cardAntipulgas, AntipulgasActivity.class);

        // Ir a la comunidad
        findViewById(R.id.btnUnirseComunidad).setOnClickListener(v -> abrirComunidad());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomepageActivity.class).putExtra("usuario", usuario));
                finish();
                return true;
            }
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, EditUsuarioActivity.class).putExtra("usuario", usuario));
                finish();
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
    private void abrir(int cardId, Class<?> destino) {
        findViewById(cardId).setOnClickListener(v -> {
            Intent intent = new Intent(this, destino);
            intent.putExtra("mascota", mascota);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        });
    }
    private void abrirComunidad() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://anaaweb.org/"));
        startActivity(intent);
    }
}
