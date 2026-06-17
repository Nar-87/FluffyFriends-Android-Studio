package com.example.fluffyfriends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private final List<FAQItem> faqList;

    public FAQAdapter(List<FAQItem> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faq = faqList.get(position);

        holder.tvPregunta.setText(faq.getPregunta());
        holder.tvRespuesta.setText(faq.getRespuesta());

        // Mostrar u ocultar respuesta
        holder.tvRespuesta.setVisibility(
                faq.isExpanded() ? View.VISIBLE : View.GONE
        );

        holder.imgExpand.setRotation(
                faq.isExpanded() ? 180f : 0f
        );

        holder.layoutPregunta.setOnClickListener(v -> {
            faq.setExpanded(!faq.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }
    // View Holder
    static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta, tvRespuesta;
        ImageView imgExpand;
        View layoutPregunta;
        FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPregunta = itemView.findViewById(R.id.tvPregunta);
            tvRespuesta = itemView.findViewById(R.id.tvRespuesta);
            imgExpand = itemView.findViewById(R.id.imgExpand);
            layoutPregunta = itemView.findViewById(R.id.layoutPregunta);
        }
    }
}
