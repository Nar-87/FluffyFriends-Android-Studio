package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FAQsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private SearchView searchFAQ;
    private List<FAQItem> listaCompleta = new ArrayList<>();
    private Usuario usuario;
    private FAQAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        if (usuario == null) {
            finish();
            return;
        }

        bottomNav = findViewById(R.id.bottomNavigationView);
        searchFAQ = findViewById(R.id.searchFAQ);

        RecyclerView recyclerFAQ = findViewById(R.id.recyclerFAQ);
        recyclerFAQ.setLayoutManager(new LinearLayoutManager(this));

        // Lista preguntas
        listaCompleta.add(new FAQItem(
                "¿Cómo registro a mi mascota?",
                "Desde la sección 'Mis Mascotas' pulsa en añadir y completa el formulario."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo editar los datos de mi mascota?",
                "Sí, pulsa el icono de editar en la tarjeta de la mascota."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo eliminar una mascota?",
                "Sí, desde 'Mis Mascotas' pulsa el icono de la papelera. Se eliminarán también sus servicios."
        ));
        listaCompleta.add(new FAQItem(
                "¿Qué servicios ofrece la app?",
                "Guardería, peluquería, veterinario, adiestramiento, paseos y tratamientos antipulgas."
        ));
        listaCompleta.add(new FAQItem(
                "¿Cómo contrato un servicio?",
                "Entra en el perfil de tu mascota y pulsa 'Contratar servicios'."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo modificar un servicio contratado?",
                "Sí, pulsa sobre el servicio y podrás editarlo."
        ));
        listaCompleta.add(new FAQItem(
                "¿Se pueden cancelar servicios?",
                "Sí, desde el detalle del servicio puedes eliminarlo."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo tener varias mascotas registradas?",
                "Sí, puedes registrar tantas mascotas como quieras."
        ));
        listaCompleta.add(new FAQItem(
                "¿Los servicios se guardan por mascota?",
                "Sí, cada servicio está asociado a una mascota concreta."
        ));
        listaCompleta.add(new FAQItem(
                "¿Necesito conexión a internet?",
                "Actualmente los datos se guardan en el dispositivo. No es necesario internet."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo cambiar mi foto de perfil?",
                "Sí, desde la sección de edición de cuenta."
        ));
        listaCompleta.add(new FAQItem(
                "¿Mis datos están seguros?",
                "Los datos se almacenan de forma local y solo son accesibles desde tu cuenta."
        ));
        listaCompleta.add(new FAQItem(
                "¿Puedo usar la app en otro móvil?",
                "No, los datos no se sincronizan entre dispositivos por el momento."
        ));
        listaCompleta.add(new FAQItem(
                "¿Qué pasa si cierro la app?",
                "Toda la información se guarda automáticamente."
        ));
        listaCompleta.add(new FAQItem(
                "¿Habrá más servicios en el futuro?",
                "Sí, la app está pensada para seguir creciendo."
        ));

        adapter = new FAQAdapter(listaCompleta);
        recyclerFAQ.setAdapter(adapter);

        configurarBuscador();
        configurarBottomNav();
    }
    // Buscador
    private void configurarBuscador() {
        searchFAQ.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarFAQs(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarFAQs(newText);
                return true;
            }
        });
    }
    private void filtrarFAQs(String texto) {
        List<FAQItem> filtradas = new ArrayList<>();
        for (FAQItem faq : listaCompleta) {
            if (faq.getPregunta().toLowerCase().contains(texto.toLowerCase()) ||
                    faq.getRespuesta().toLowerCase().contains(texto.toLowerCase())) {
                filtradas.add(faq);
            }
        }
        adapter = new FAQAdapter(filtradas);
        ((RecyclerView) findViewById(R.id.recyclerFAQ)).setAdapter(adapter);
    }
    // Menú inferior
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
}
