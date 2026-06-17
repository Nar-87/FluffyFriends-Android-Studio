package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnAcceder;
    private TextView tvIrARegistro;
    private DBDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBDao(this);

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnAcceder = findViewById(R.id.btnAcceder);
        tvIrARegistro = findViewById(R.id.tvIrARegistro);

        btnAcceder.setOnClickListener(v -> login());

        tvIrARegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
            finish();
        });
    }
    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = db.login(email, password);

        if (usuario == null) {
            Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Bienvenid@ " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HomepageActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}
