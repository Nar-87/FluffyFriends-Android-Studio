package com.example.fluffyfriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SupportActivity extends AppCompatActivity {

    private CardView cardFAQ, cardContacto, cardEmail;
    private BottomNavigationView bottomNav;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        // Recibe el usuario
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        cardFAQ = findViewById(R.id.cardFAQ);
        cardContacto = findViewById(R.id.cardContacto);
        cardEmail = findViewById(R.id.cardEmail);
        bottomNav = findViewById(R.id.bottomNavigationView);

        // Menú inferior
        bottomNav.setSelectedItemId(R.id.nav_home);
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

        // Clicks en las tarjetas
        cardFAQ.setOnClickListener(v ->
                startActivity(new Intent(this, FAQsActivity.class).putExtra("usuario", usuario))
        );
        cardContacto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+34123456789"));
            startActivity(intent);
        });
        cardEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:soporte@fluffyfriends.com"));
            startActivity(intent);
        });
    }
}
