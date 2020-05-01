package com.darwin.aigus.darwin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.ArrayList;
import java.util.List;

public class AdapterLev extends RecyclerView.Adapter<AdapterLev.ViewHolder> {
    private List<Levantamento> levantamentos;
    private Context context;
    private OnClickListenerLev onClickListenerLev;

    public AdapterLev(List<Levantamento> levantamentos, Context context, OnClickListenerLev onClickListenerLev) {
        this.levantamentos = levantamentos;
        this.context = context;
        this.onClickListenerLev = onClickListenerLev;
    }

    public interface OnClickListenerLev{
        void onClickEdit(View view, int position);
        void onClickDelete(View view, int position);
        void onClickItem(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_levantamento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DataBaseDarwin dataBaseDarwin = new DataBaseDarwin(context);
        Levantamento levantamento = levantamentos.get(position);

        int t = dataBaseDarwin.countAmostra(levantamento.getLev_id_mask());
        holder.nameLev.setText(levantamento.getLev_name());
        String subTitle = t + " " + context.getString(R.string.amostras);
        holder.nAmostra.setText(subTitle);

        holder.btnEdit.setOnClickListener(v -> onClickListenerLev.onClickEdit(v, position));

        holder.btnDelete.setOnClickListener(v -> onClickListenerLev.onClickDelete(v, position));

        holder.contBtnLev.setOnClickListener(v -> onClickListenerLev.onClickItem(v, position));

    }

    @Override
    public int getItemCount() {
        return this.levantamentos != null ? this.levantamentos.size():0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout contBtnLev;
        private ImageButton btnEdit, btnDelete;
        private TextView nameLev, nAmostra;
        private View view;

        ViewHolder(View view) {
            super(view);
            this.view = itemView;

            contBtnLev = view.findViewById(R.id.contBtnLev);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);

            nameLev = view.findViewById(R.id.nameLev);
            nAmostra = view.findViewById(R.id.nAmostra);
        }
    }

    public void setFilter(ArrayList<Levantamento> levantament){
        levantamentos = new ArrayList<>();
        levantamentos.addAll(levantament);
        notifyDataSetChanged();

    }
}
