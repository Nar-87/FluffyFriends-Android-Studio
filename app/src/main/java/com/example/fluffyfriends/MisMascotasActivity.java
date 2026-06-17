package com.example.fluffyfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MisMascotasActivity extends AppCompatActivity {

    private TextView tvSaludo, tvSubSaludo2, tvContadorMascotas;
    private RecyclerView recyclerMascotas;
    private BottomNavigationView bottomNav;
    private SearchView searchMascotas;
    private TextView tvEditarPerfil;
    private Usuario usuario;
    private DBDao db;
    private List<Mascota> listaCompleta = new ArrayList<>();
    private MascotaAdapter adapter;
    private ImageView imgPerfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismascotas);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        tvSaludo = findViewById(R.id.tvSaludo);
        tvSubSaludo2 = findViewById(R.id.tvSubSaludo2);
        tvContadorMascotas = findViewById(R.id.tvContadorMascotas);
        recyclerMascotas = findViewById(R.id.recyclerMascotas);
        bottomNav = findViewById(R.id.bottomNavigationView);
        searchMascotas = findViewById(R.id.searchMascotas);
        tvEditarPerfil = findViewById(R.id.tvEditarPerfil);
        imgPerfilUsuario = findViewById(R.id.imgPerfil);
        tvSaludo.setText("¡Hola " + usuario.getNombre() + "!");
        tvSubSaludo2.setText("Estas son tus mascotas");

        recyclerMascotas.setLayoutManager(new LinearLayoutManager(this));

        cargarMascotas();
        cargarFotoUsuario();
        configurarBuscador();

        findViewById(R.id.cardRegistrarMascota).setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarMascotaActivity.class).putExtra("usuario", usuario))
        );
        findViewById(R.id.imgPerfil).setOnClickListener(v -> abrirPerfilUsuario());
        tvEditarPerfil.setOnClickListener(v -> abrirPerfilUsuario());
        configurarBottomNav();
    }
    private void abrirPerfilUsuario() {
        startActivity(new Intent(this, EditUsuarioActivity.class).putExtra("usuario", usuario));
    }
    private void cargarMascotas() {
        listaCompleta = db.obtenerMascotas(usuario.getId());
        tvContadorMascotas.setText("Mascotas: " + listaCompleta.size());

        adapter = new MascotaAdapter(listaCompleta, usuario);
        recyclerMascotas.setAdapter(adapter);
    }

    private void cargarFotoUsuario() {
        if (usuario.getFotoPath() != null && !usuario.getFotoPath().isEmpty()) {
            imgPerfilUsuario.setImageURI(Uri.parse(usuario.getFotoPath()));
        } else {
            imgPerfilUsuario.setImageResource(R.drawable.ic_profile);
        }
    }

    private void configurarBuscador() {
        searchMascotas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarMascotas(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarMascotas(newText);
                return true;
            }
        });
    }
    private void filtrarMascotas(String texto) {
        List<Mascota> filtradas = new ArrayList<>();
        for (Mascota m : listaCompleta) {
            if (m.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtradas.add(m);
            }
        }
        adapter = new MascotaAdapter(filtradas, usuario);
        recyclerMascotas.setAdapter(adapter);
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
        cargarMascotas();
    }
}
