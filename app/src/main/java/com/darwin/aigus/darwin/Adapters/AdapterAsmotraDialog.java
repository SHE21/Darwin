package com.darwin.aigus.darwin.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.R;

import java.util.List;

public class AdapterAsmotraDialog extends RecyclerView.Adapter<AdapterAsmotraDialog.ViewHolder>  {
    private List<Amostra> amostras;

    public AdapterAsmotraDialog(List<Amostra> amostras) {
        this.amostras = amostras;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_amostra_dialog, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Amostra amostra = amostras.get(position);
        holder.nameAmostra.setText(amostra.getAm_name());
        String coords = amostra.getAm_geodata_lat()+", "+amostra.getAm_geodata_long();
        holder.coordsAmostras.setText(coords);
    }

    @Override
    public int getItemCount() {
        return this.amostras != null ? this.amostras.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameAmostra, coordsAmostras;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            nameAmostra = view.findViewById(R.id.nameAmostra);
            coordsAmostras = view.findViewById(R.id.coordsAmostras);
        }
    }
}
