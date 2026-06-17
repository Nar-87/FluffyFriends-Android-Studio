package com.example.fluffyfriends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {
    private List<ServicioMascota> servicios;
    private OnServicioClickListener listener;

    public interface OnServicioClickListener {
        void onServicioClick(ServicioMascota servicio);
    }
    public ServicioAdapter(List<ServicioMascota> servicios, OnServicioClickListener listener) {
        this.servicios = servicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        ServicioMascota servicio = servicios.get(position);

        holder.tvTipo.setText(servicio.getTipo());
        holder.tvDescripcion.setText(servicio.getDescripcion());
        holder.tvFecha.setText(servicio.getFecha());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onServicioClick(servicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }
    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvDescripcion, tvFecha;
        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvTipoServicio);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionServicio);
            tvFecha = itemView.findViewById(R.id.tvFechaServicio);
        }
    }
}
