package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterUserActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnRegisterSubmit;
    private TextView loginLink;
    private DBDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_usuario);

        db = new DBDao(this);

        //Referencias
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        loginLink = findViewById(R.id.loginLink);

        btnRegisterSubmit.setOnClickListener(v -> registrarUsuario());

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
    private void registrarUsuario() {
        String nombre = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        Usuario usuario = new Usuario(nombre, email, password);
        boolean ok = db.insertarUsuario(usuario);
        if (!ok) {
            Toast.makeText(this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HomepageActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}
