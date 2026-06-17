package com.example.fluffyfriends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private final List<Mascota> mascotas;
    private final Usuario usuario;

    public MascotaAdapter(List<Mascota> mascotas, Usuario usuario) {
        this.mascotas = mascotas;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtTipo.setText(mascota.getTipo());
        holder.txtRaza.setText(mascota.getRaza());

        if (mascota.getFotoPath() != null && !mascota.getFotoPath().isEmpty()) {
            holder.imgMascota.setImageURI(Uri.parse(mascota.getFotoPath()));
        } else {
            holder.imgMascota.setImageResource(R.drawable.ic_pets);
        }

        // Abrir perfil mascota
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PerfilMascotaActivity.class);
            intent.putExtra("mascota", mascota);
            intent.putExtra("usuario", usuario);
            v.getContext().startActivity(intent);
        });

        // Editar mascota
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegistrarMascotaActivity.class);
            intent.putExtra("mascota", mascota);
            intent.putExtra("usuario", usuario);
            v.getContext().startActivity(intent);
        });

        // Eliminar mascota
        holder.btnEliminar.setOnClickListener(v -> {
            Context context = v.getContext();
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar mascota")
                    .setMessage("¿Seguro que quieres eliminar esta mascota?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        DBDao db = new DBDao(context);
                        db.eliminarMascota(mascota.getId());
                        mascotas.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    static class MascotaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMascota;
        TextView txtNombre, txtTipo, txtRaza;
        ImageButton btnEditar, btnEliminar;

        MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMascota = itemView.findViewById(R.id.imgMascota);
            txtNombre = itemView.findViewById(R.id.txtNombreMascota);
            txtTipo = itemView.findViewById(R.id.txtTipoMascota);
            txtRaza = itemView.findViewById(R.id.txtRazaMascota);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
