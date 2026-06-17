package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OpcionesCuentaActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_cuenta);

        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        Button btnAcceder = findViewById(R.id.btnAcceder);
        Button btnGoogle = findViewById(R.id.btnGoogle);

        btnRegistrarse.setOnClickListener(v ->
                startActivity(new Intent(OpcionesCuentaActivity.this, RegisterUserActivity.class))
        );

        btnAcceder.setOnClickListener(v ->
                startActivity(new Intent(OpcionesCuentaActivity.this, LoginActivity.class))
        );

        btnGoogle.setOnClickListener(v -> {
            // botón de diseño (no funciona)
        });

    }
}
