package com.example.fluffyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleServicioActivity extends AppCompatActivity {
    private ServicioMascota servicio;
    private Mascota mascota;
    private Usuario usuario;
    private DBDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);

        servicio = (ServicioMascota) getIntent().getSerializableExtra("servicio");
        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (servicio == null || mascota == null || usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        // Referencias
        TextView tvTipo = findViewById(R.id.tvTipoServicio);
        TextView tvDescripcion = findViewById(R.id.tvDescripcionServicio);
        TextView tvFecha = findViewById(R.id.tvFechaServicio);
        TextView tvPrecio = findViewById(R.id.tvPrecioServicio);
        TextView tvEstado = findViewById(R.id.tvEstadoServicio);
        Button btnModificar = findViewById(R.id.btnEditarServicio);
        Button btnCancelar = findViewById(R.id.btnCancelarServicio);
        // Datos
        tvTipo.setText(servicio.getTipo());
        tvDescripcion.setText(servicio.getDescripcion());
        tvFecha.setText("Fecha: " + servicio.getFecha());
        tvPrecio.setText("Precio: " + servicio.getPrecio() + " €");
        tvEstado.setText("Estado: " + servicio.getEstado());
        // Cancela el servicio
        btnCancelar.setOnClickListener(v -> cancelarServicio());
        // Modifica el servicio, vuelve al flujo correcto
        btnModificar.setOnClickListener(v -> modificarServicio());
    }
    private void cancelarServicio() {
        db.eliminarServicio(servicio.getId());
        Toast.makeText(this, "Servicio cancelado", Toast.LENGTH_SHORT).show();
        volverAlPerfil();
    }
    private void modificarServicio() {
        Intent intent = null;
        switch (servicio.getTipo()) {
            case "Adiestramiento": intent = new Intent(this, AdiestramientoActivity.class);
                break;
            case "Peluquería": intent = new Intent(this, PeluqueriaActivity.class);
                break;
            case "Guardería": intent = new Intent(this, GuarderiaActivity.class);
                break;
            case "Veterinario": intent = new Intent(this, VeterinarioActivity.class);
                break;
            case "Paseos": intent = new Intent(this, PaseosActivity.class);
                break;
            case "Antipulgas": intent = new Intent(this, AntipulgasActivity.class);
                break;
        }
        if (intent != null) {
            intent.putExtra("servicio", servicio);
            intent.putExtra("mascota", mascota);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
            finish();
        }
    }
    private void volverAlPerfil() {
        Intent intent = new Intent(this, PerfilMascotaActivity.class);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}
