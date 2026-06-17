package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class HomepageActivity extends AppCompatActivity {
    private TextView tvSaludoHeader, tvSubHeader;
    private ImageView imgPerfilUser;
    private BottomNavigationView bottomNav;
    private Usuario usuario;
    private View layoutPerfilHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // Recibir usuario
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        // Referencias
        tvSaludoHeader = findViewById(R.id.tvSaludoHeader);
        tvSubHeader = findViewById(R.id.tvSubHeader);
        imgPerfilUser = findViewById(R.id.imgIconoPerfil);
        bottomNav = findViewById(R.id.bottomNavigationView);
        // UI
        tvSaludoHeader.setText("¡Hola " + usuario.getNombre() + "!");
        tvSubHeader.setText("Nos alegra verte de nuevo");
        // Click imagen perfil
        imgPerfilUser.setOnClickListener(v -> abrirPerfil());
        // Cards
        findViewById(R.id.cardRegistrarMascota).setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarMascotaActivity.class).putExtra("usuario", usuario))
        );
        findViewById(R.id.cardMisMascotas).setOnClickListener(v ->
                startActivity(new Intent(this, MisMascotasActivity.class).putExtra("usuario", usuario))
        );
        findViewById(R.id.cardMiCuenta).setOnClickListener(v -> abrirPerfil());
        findViewById(R.id.cardInfoSoporte).setOnClickListener(v ->
                startActivity(new Intent(this, SupportActivity.class).putExtra("usuario", usuario))
        );

        layoutPerfilHome = findViewById(R.id.layoutPerfilHome);
        layoutPerfilHome.setOnClickListener(v -> abrirPerfil());

        configurarBottomNav();
        cargarFotoUsuario();
    }
    private void abrirPerfil() {
        Intent intent = new Intent(this, EditUsuarioActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private void cargarFotoUsuario() {
        if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
            File imgFile = new File(usuario.getFotoPath());
            if (imgFile.exists()) {
                imgPerfilUser.setImageURI(Uri.fromFile(imgFile));
            } else {
                imgPerfilUser.setImageResource(R.drawable.ic_profile);
            }
        } else {
            imgPerfilUser.setImageResource(R.drawable.ic_profile);
        }
    }

    private void configurarBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_perfil) {
                abrirPerfil();
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
    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
