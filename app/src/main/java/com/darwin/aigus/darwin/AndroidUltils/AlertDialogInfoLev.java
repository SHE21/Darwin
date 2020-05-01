package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.darwin.aigus.darwin.Async.AsyncGetUser;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

public class AlertDialogInfoLev extends DialogFragment{
    private Levantamento levantamento;
    private int totalAmostra;

    public Levantamento getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Levantamento levantamento) {
        this.levantamento = levantamento;
    }

    public int getTotalAmostra() {
        return totalAmostra;
    }

    public void setTotalAmostra(int totalAmostra) {
        this.totalAmostra = totalAmostra;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_lev_info, null);
        TextView nameLev = view.findViewById(R.id.nameAmostra);
        TextView dateCreateLev = view.findViewById(R.id.dateCreateAmostra);
        TextView totalAmostras = view.findViewById(R.id.totalAmostras);
        TextView totalEspecies = view.findViewById(R.id.totalEspecies);
        TextView userLev = view.findViewById(R.id.userAmostra);
        TextView idLev = view.findViewById(R.id.idLev);

        DataBaseDarwin dataBaseDarwin = new DataBaseDarwin(getContext());
        int totalEsp = dataBaseDarwin.countEspecieByLev(getLevantamento().getLev_id_mask());

        DelegateDarwin delegateDarwin = new DelegateDarwin(getContext());
        PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getContext());
        String id = preferencesDarwin.getUserAccount();
        AsyncGetUser asyncTaskUser = new AsyncGetUser(delegateDarwin, userDarwin -> userLev.setText(userDarwin.getFirstName()));
        asyncTaskUser.execute(id);

        String nomeLevant = getLevantamento().getLev_name() + "" + getLevantamento().getId();
        nameLev.setText(nomeLevant);
        dateCreateLev.setText(getLevantamento().getLev_date());
        totalAmostras.setText(String.valueOf(getTotalAmostra()));
        totalEspecies.setText(String.valueOf(totalEsp));
        idLev.setText(getLevantamento().getLev_id_mask());

        builder.setTitle(R.string.datalevantamento);
        builder.setPositiveButton(R.string.fechar, (dialog, which) -> {

        });

        builder.setView(view);
        return builder.create();
    }
}
