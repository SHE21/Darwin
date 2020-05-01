package com.darwin.aigus.darwin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.ArrayList;
import java.util.List;

public class AdapterAsmotra extends RecyclerView.Adapter<AdapterAsmotra.ViewHolder>  {
    private List<Amostra> amostras;
    private Context context;
    private OnClickAsmotra onClickAsmotra;

    public interface OnClickAsmotra{
        void onClickEdit(View view, int position);
        void onClickDelete(View view, int position);
        void onClickSeeMap(View view, int position);
        void onClickItem(View view, int position);
    }

    public AdapterAsmotra(List<Amostra> amostras, Context context, OnClickAsmotra onClickAsmotra) {
        this.amostras = amostras;
        this.context = context;
        this.onClickAsmotra = onClickAsmotra;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_amostra, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DataBaseDarwin dataBaseDarwin = new DataBaseDarwin(context);
        Amostra amostra = amostras.get(position);

        holder.nameAmostra.setText(amostra.getAm_name());
        int t = dataBaseDarwin.countEspecie(amostra.getId_mask());
        String i = t + " " + context.getString(R.string.especies);
        holder.nEspecie.setText(i);

        holder.contBtnLev.setOnClickListener(v -> onClickAsmotra.onClickItem(v, position));

        holder.btnSeeMap.setOnClickListener(v -> onClickAsmotra.onClickSeeMap(v, position));

        holder.btnEdit.setOnClickListener(v -> onClickAsmotra.onClickEdit(v, position));

        holder.btnDelete.setOnClickListener(v -> onClickAsmotra.onClickDelete(v, position));
    }

    @Override
    public int getItemCount() {
        return this.amostras != null ? this.amostras.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout contBtnLev;
        private TextView nameAmostra, nEspecie;
        private ImageButton btnEdit, btnDelete, btnSeeMap;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            contBtnLev = view.findViewById(R.id.contBtnLev);
            nameAmostra = view.findViewById(R.id.nameAmostra);
            nEspecie = view.findViewById(R.id.nEspecie);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
            btnSeeMap = view.findViewById(R.id.btnSeeMap);
        }
    }

    public void setFilter(List<Amostra> amostra){
        amostras = new ArrayList<>();
        amostras.addAll(amostra);
        notifyDataSetChanged();

    }
}
