package com.darwin.aigus.darwin.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.R;

import java.util.List;

public class AdapterEspecie extends RecyclerView.Adapter<AdapterEspecie.ViewHolder>  {
    private List<Especie> especies;
    private OnClickEspecie onClickEspecie;

    public interface OnClickEspecie{
        void onClickDelete(View view, int position);
        void onClickEdit(View view, int position);
        void onClickItem(View view, int position);
    }

    public AdapterEspecie(List<Especie> especies, OnClickEspecie onClickEspecie) {
        this.especies = especies;
        this.onClickEspecie = onClickEspecie;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_especie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Especie especie = especies.get(position);
        String height = "A" + String.valueOf(especie.getEs_height()) + " m";
        String diameter = ", D" + String.valueOf(especie.getEs_diameter())  + " cm";
        holder.nameEspecie.setText(especie.getEs_name());
        holder.dataHeight.setText(height);
        holder.dataDiameter.setText(diameter);

        holder.contBtnLev.setOnClickListener(v -> onClickEspecie.onClickItem(v, position));

        holder.btnDelete.setOnClickListener(v -> onClickEspecie.onClickDelete(v, position));

        holder.btnEdit.setOnClickListener(v -> onClickEspecie.onClickEdit(v, position));
    }

    @Override
    public int getItemCount() {
        return this.especies != null ? this.especies.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout contBtnLev;
        private TextView nameEspecie, dataHeight, dataDiameter;
        private ImageButton btnDelete, btnEdit;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            contBtnLev = view.findViewById(R.id.contBtnLev);
            nameEspecie = view.findViewById(R.id.nameEspecie);
            dataHeight = view.findViewById(R.id.dataHeight);
            dataDiameter = view.findViewById(R.id.dataDiameter);
            btnDelete = view.findViewById(R.id.btnDelete);
            btnEdit = view.findViewById(R.id.btnEdit);
        }
    }
}
