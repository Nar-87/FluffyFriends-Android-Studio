package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class RegistrarMascotaActivity extends AppCompatActivity {

    private TextInputEditText edtNombre, edtRaza, edtColor, edtDescripcion, edtObservaciones;
    private Spinner spinnerTipo, spinnerSexo;
    private Button btnRegistrar;
    private ImageView imgFotoMascota;
    private TextView tvCambiarFoto;
    private BottomNavigationView bottomNav;

    private Usuario usuario;
    private Mascota mascotaEditar;
    private DBDao db;

    private String fotoPathSeleccionada;

    // Galería
    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        File file = new File(
                                getFilesDir(),
                                "mascota_" + System.currentTimeMillis() + ".jpg"
                        );

                        FileOutputStream outputStream = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        outputStream.close();
                        inputStream.close();

                        fotoPathSeleccionada = file.getAbsolutePath();
                        //imgFotoMascota.setImageURI(Uri.fromFile(file));

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        mascotaEditar = (Mascota) getIntent().getSerializableExtra("mascota");

        if (usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        // Referencias
        edtNombre = findViewById(R.id.edtNombreMascota);
        edtRaza = findViewById(R.id.edtRazaMascota);
        edtColor = findViewById(R.id.edtColorMascota);
        edtDescripcion = findViewById(R.id.edtDescripcionMascota);
        edtObservaciones = findViewById(R.id.edtObservacionesMascota);

        spinnerTipo = findViewById(R.id.spinnerTipoMascota);
        spinnerSexo = findViewById(R.id.spinnerSexoMascota);

        btnRegistrar = findViewById(R.id.btnRegistrarMascota);
        imgFotoMascota = findViewById(R.id.imgIconoUsuario);
        tvCambiarFoto = findViewById(R.id.tvCambiarFotoMascota);
        bottomNav = findViewById(R.id.bottomNavigationView);

        configurarSpinners();

        imgFotoMascota.setOnClickListener(v -> galeriaLauncher.launch("image/*"));
        tvCambiarFoto.setOnClickListener(v -> galeriaLauncher.launch("image/*"));

        if (mascotaEditar != null) {
            precargarDatos();
            btnRegistrar.setText("Guardar cambios");
        } else {
            // Mostrar foto del usuario SOLO de forma visual
            if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
                File img = new File(usuario.getFotoPath());
                if (img.exists()) {
                    imgFotoMascota.setImageURI(Uri.fromFile(img));
                } else {
                    imgFotoMascota.setImageResource(R.drawable.ic_pets);
                }
            } else {
                imgFotoMascota.setImageResource(R.drawable.ic_pets);
            }
        }

        btnRegistrar.setOnClickListener(v -> guardarMascota());
        configurarBottomNav();
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> tipoAdapter =
                ArrayAdapter.createFromResource(this, R.array.tipos_mascota, android.R.layout.simple_spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);

        ArrayAdapter<CharSequence> sexoAdapter =
                ArrayAdapter.createFromResource(this, R.array.sexos_mascota, android.R.layout.simple_spinner_item);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(sexoAdapter);
    }

    private void precargarDatos() {
        edtNombre.setText(mascotaEditar.getNombre());
        edtRaza.setText(mascotaEditar.getRaza());
        edtColor.setText(mascotaEditar.getColor());
        edtDescripcion.setText(mascotaEditar.getDescripcion());
        edtObservaciones.setText(mascotaEditar.getObservaciones());

        spinnerTipo.setSelection(
                ((ArrayAdapter) spinnerTipo.getAdapter()).getPosition(mascotaEditar.getTipo())
        );
        spinnerSexo.setSelection(
                ((ArrayAdapter) spinnerSexo.getAdapter()).getPosition(mascotaEditar.getSexo())
        );

        if (mascotaEditar.getFotoPath() != null) {
            File img = new File(mascotaEditar.getFotoPath());
            if (img.exists()) {
                imgFotoMascota.setImageURI(Uri.fromFile(img));
                fotoPathSeleccionada = mascotaEditar.getFotoPath();
            }
        }
    }

    private void guardarMascota() {

        String nombre = edtNombre.getText().toString().trim();
        if (nombre.isEmpty()) {
            edtNombre.setError("Nombre obligatorio");
            return;
        }

        if (mascotaEditar != null) {

            mascotaEditar.setNombre(nombre);
            mascotaEditar.setRaza(edtRaza.getText().toString().trim());
            mascotaEditar.setTipo(spinnerTipo.getSelectedItem().toString());
            mascotaEditar.setSexo(spinnerSexo.getSelectedItem().toString());
            mascotaEditar.setColor(edtColor.getText().toString().trim());
            mascotaEditar.setDescripcion(edtDescripcion.getText().toString().trim());
            mascotaEditar.setObservaciones(edtObservaciones.getText().toString().trim());

            if (fotoPathSeleccionada != null) {
                mascotaEditar.setFotoPath(fotoPathSeleccionada);
            }

            db.actualizarMascota(mascotaEditar);
            Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();

        } else {

            Mascota nueva = new Mascota(
                    nombre,
                    edtRaza.getText().toString().trim(),
                    spinnerSexo.getSelectedItem().toString(),
                    spinnerTipo.getSelectedItem().toString(),
                    edtColor.getText().toString().trim(),
                    edtDescripcion.getText().toString().trim(),
                    edtObservaciones.getText().toString().trim(),
                    fotoPathSeleccionada
            );

            db.insertarMascota(nueva, usuario.getId());
            Toast.makeText(this, "Mascota registrada", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, MisMascotasActivity.class)
                .putExtra("usuario", usuario));
        finish();
    }

    private void configurarBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, HomepageActivity.class)
                        .putExtra("usuario", usuario));
                return true;
            }
            if (item.getItemId() == R.id.nav_perfil) {
                startActivity(new Intent(this, EditUsuarioActivity.class)
                        .putExtra("usuario", usuario));
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
