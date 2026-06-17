package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditUsuarioActivity extends AppCompatActivity {

    private ImageView imgPerfil;
    private TextView tvNombreActual, tvEmailActual, tvTelefonoActual, tvEditarPerfil;
    private TextInputEditText edtNombre, edtEmail, edtTelefono;
    private Button btnGuardar;
    private CardView cardEditarUsuario;
    private BottomNavigationView bottomNav;

    private Usuario usuario;
    private DBDao db;

    // Galería
    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        // Abrir stream
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        // Crear archivo interno
                        File file = new File(
                                getFilesDir(),
                                "user_" + System.currentTimeMillis() + ".jpg"
                        );

                        FileOutputStream outputStream = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        outputStream.close();
                        inputStream.close();

                        // Guardar PATH
                        usuario.setFotoPath(file.getAbsolutePath());
                        db.actualizarUsuario(usuario);

                        // Mostrar imagen
                        imgPerfil.setImageURI(Uri.fromFile(file));

                        Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editusuario);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        // Referencias
        imgPerfil = findViewById(R.id.imgPerfilUsuario);
        tvNombreActual = findViewById(R.id.tvNombreActual);
        tvEmailActual = findViewById(R.id.tvEmailActual);
        tvTelefonoActual = findViewById(R.id.tvTelefonoActual);
        tvEditarPerfil = findViewById(R.id.tvEditarPerfil);

        edtNombre = findViewById(R.id.edtNombreUsuario);
        edtEmail = findViewById(R.id.edtEmailUsuario);
        edtTelefono = findViewById(R.id.edtTelefonoUsuario);
        btnGuardar = findViewById(R.id.btnGuardarPerfil);

        cardEditarUsuario = findViewById(R.id.cardEditarUsuario);
        bottomNav = findViewById(R.id.bottomNavigationViewPerfil);

        cargarDatosUsuario();

        // Imagen galería
        imgPerfil.setOnClickListener(v -> galeriaLauncher.launch("image/*"));

        // Texto formulario (NO galería)
        tvEditarPerfil.setOnClickListener(v ->
                cardEditarUsuario.setVisibility(View.VISIBLE)
        );

        btnGuardar.setOnClickListener(v -> guardarCambios());

        configurarBottomNav();
    }

    private void cargarDatosUsuario() {
        tvNombreActual.setText(usuario.getNombre());
        tvEmailActual.setText(usuario.getEmail());
        tvTelefonoActual.setText(usuario.getTelefono());

        edtNombre.setText(usuario.getNombre());
        edtEmail.setText(usuario.getEmail());
        edtTelefono.setText(usuario.getTelefono());

        if (usuario.getFotoPath() != null) {
            File img = new File(usuario.getFotoPath());
            if (img.exists()) {
                imgPerfil.setImageURI(Uri.fromFile(img));
            } else {
                imgPerfil.setImageResource(R.drawable.ic_profile);
            }
        } else {
            imgPerfil.setImageResource(R.drawable.ic_profile);
        }
    }

    private void guardarCambios() {
        usuario.setNombre(edtNombre.getText().toString().trim());
        usuario.setEmail(edtEmail.getText().toString().trim());
        usuario.setTelefono(edtTelefono.getText().toString().trim());

        db.actualizarUsuario(usuario);

        cargarDatosUsuario();
        cardEditarUsuario.setVisibility(View.GONE);

        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
    }

    private void configurarBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_perfil);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, HomepageActivity.class)
                        .putExtra("usuario", usuario));
                finish();
                return true;
            }
            if (item.getItemId() == R.id.nav_perfil) {
                return true;
            }
            if (item.getItemId() == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
