package com.example.fluffyfriends;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReservasActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btnHora, btnConfirmar;
    private TextView tvHora;
    private int hora = -1, minuto = -1;
    private long fechaSeleccionada;
    private ServicioMascota servicio;
    private Mascota mascota;
    private Usuario usuario;
    private DBDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        servicio = (ServicioMascota) getIntent().getSerializableExtra("servicio");
        mascota = (Mascota) getIntent().getSerializableExtra("mascota");
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        if (servicio == null || mascota == null || usuario == null) {
            finish();
            return;
        }

        db = new DBDao(this);

        calendarView = findViewById(R.id.calendarView);
        btnHora = findViewById(R.id.btnSelectTime);
        btnConfirmar = findViewById(R.id.btnConfirm);
        tvHora = findViewById(R.id.tvSelectedTime);

        fechaSeleccionada = calendarView.getDate();

        calendarView.setOnDateChangeListener((view, y, m, d) -> {
            Calendar c = Calendar.getInstance();
            c.set(y, m, d);
            fechaSeleccionada = c.getTimeInMillis();
        });

        btnHora.setOnClickListener(v -> seleccionarHora());
        btnConfirmar.setOnClickListener(v -> confirmarReserva());
    }
    private void seleccionarHora() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, h, min) -> {
                    hora = h;
                    minuto = min;
                    tvHora.setText(String.format("Hora: %02d:%02d", h, min));
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        );
        dialog.show();
    }
    private void confirmarReserva() {
        if (hora == -1) {
            Toast.makeText(this, "Selecciona una hora", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(fechaSeleccionada);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minuto);

        String fechaFinal = String.format(
                "%02d/%02d/%d %02d:%02d",
                c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.YEAR),
                hora,
                minuto
        );
        servicio.setFecha(fechaFinal);
        servicio.setEstado("ACTIVO");
        servicio.setMascotaId(mascota.getId());
        // insertar en la base de datos
        if (servicio.getId() > 0) {
            db.actualizarServicio(servicio);
        } else {
            long id = db.insertarServicio(mascota.getId(), servicio);
            servicio.setId((int) id);
        }

        Toast.makeText(this, "Servicio contratado correctamente", Toast.LENGTH_LONG).show();

        // volver al prefil de la mascota
        Intent intent = new Intent(this, PerfilMascotaActivity.class);
        intent.putExtra("mascota", mascota);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}
