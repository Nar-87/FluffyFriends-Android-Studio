package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PerfilMascotaActivity extends AppCompatActivity {

    private ImageView imgMascota, imgFondo;
    private TextView tvNombre, tvTipo, tvRaza, tvSexo;
    private TextView tvObservacionesMascota, tvColorMascota, tvDescripcionMascota;
    private LinearLayout llServicios;
    private BottomNavigationView bottomNav;
    private SearchView searchServicios;

    private Mascota mascota;
    private Usuario usuario;
    private DBDao db;

    private List<ServicioMascota> listaServicios = new ArrayList<>();
    private Uri nuevaFotoUri;

    // Launcher galería
    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    String path = guardarImagenInterna(uri);
                    if (path != null) {
                        mascota.setFotoPath(path);
                        db.actualizarMascota(mascota);
                        imgMascota.setImageURI(Uri.fromFile(new File(path)));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (mascota == null || usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        // Referencias
        imgMascota = findViewById(R.id.imgMascota);
        imgFondo = findViewById(R.id.imgFondoMascota);
        tvNombre = findViewById(R.id.tvNombreMascota);
        tvTipo = findViewById(R.id.tvTipoMascota);
        tvRaza = findViewById(R.id.tvRazaMascota);
        tvSexo = findViewById(R.id.tvSexoMascota);
        tvColorMascota = findViewById(R.id.tvColorMascota);
        tvDescripcionMascota = findViewById(R.id.tvDescripcionMascota);
        tvObservacionesMascota = findViewById(R.id.tvObservacionesMascota);
        llServicios = findViewById(R.id.llServicios);
        bottomNav = findViewById(R.id.bottomNavigationView);
        searchServicios = findViewById(R.id.searchServicios);

        cargarDatosMascota();

        listaServicios = db.obtenerServicios(mascota.getId());
        mostrarServicios(listaServicios);

        configurarBuscador();
        configurarBottomNav();

        // imagen perfil
        imgMascota.setOnClickListener(v -> galeriaLauncher.launch("image/*"));

        findViewById(R.id.tvEditarMascota).setOnClickListener(v -> abrirEdicionMascota());
        findViewById(R.id.btnServicios).setOnClickListener(v -> abrirServicios());
    }
    private void abrirEdicionMascota() {
        Intent intent = new Intent(this, RegistrarMascotaActivity.class);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
    private void cargarDatosMascota() {
        tvNombre.setText(mascota.getNombre());
        tvTipo.setText(mascota.getTipo());
        tvRaza.setText("Raza: " + mascota.getRaza());
        tvSexo.setText("Sexo: " + mascota.getSexo());
        tvColorMascota.setText("Color: " + mascota.getColor());
        tvDescripcionMascota.setText("Descripción: " + mascota.getDescripcion());
        tvObservacionesMascota.setText("Observaciones: " + mascota.getObservaciones());

        if (mascota.getFotoPath() != null) {
            File img = new File(mascota.getFotoPath());
            if (img.exists()) {
                imgMascota.setImageURI(Uri.fromFile(img));
            } else {
                imgMascota.setImageResource(R.drawable.ic_pets);
            }
        }
        imgFondo.setImageResource(
                mascota.getTipo().equalsIgnoreCase("perro") ? R.drawable.fondo_perro : R.drawable.fondo_gato
        );
    }
    private void mostrarServicios(List<ServicioMascota> servicios) {
        llServicios.removeAllViews();
        if (servicios.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No hay servicios contratados");
            tv.setTextSize(16);
            llServicios.addView(tv);
            return;
        }
        for (ServicioMascota servicio : servicios) {
            View card = getLayoutInflater().inflate(R.layout.item_servicio, llServicios, false);
            ((TextView) card.findViewById(R.id.tvTipoServicio)).setText(servicio.getTipo());
            ((TextView) card.findViewById(R.id.tvDescripcionServicio)).setText(servicio.getDescripcion());
            ((TextView) card.findViewById(R.id.tvFechaServicio)).setText(servicio.getFecha());

            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetalleServicioActivity.class);
                intent.putExtra("servicio", servicio);
                intent.putExtra("mascota", mascota);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            });
            llServicios.addView(card);
        }
    }
    private void configurarBuscador() {
        searchServicios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarServicios(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarServicios(newText);
                return true;
            }
        });
    }
    private void filtrarServicios(String texto) {
        if (texto == null || texto.isEmpty()) {
            mostrarServicios(listaServicios);
            return;
        }
        String filtro = texto.toLowerCase();
        List<ServicioMascota> filtrados = new ArrayList<>();
        for (ServicioMascota s : listaServicios) {
            if (s.getTipo().toLowerCase().contains(filtro)
                    || s.getDescripcion().toLowerCase().contains(filtro)) {
                filtrados.add(s);
            }
        }
        mostrarServicios(filtrados);
    }
    private void abrirServicios() {
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    private String guardarImagenInterna(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);

            File dir = new File(getFilesDir(), "mascotas");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "mascota_" + System.currentTimeMillis() + ".jpg");
            OutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }

            input.close();
            output.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    @Override
    protected void onResume() {
        super.onResume();
        listaServicios = db.obtenerServicios(mascota.getId());
        mostrarServicios(listaServicios);
    }
}
