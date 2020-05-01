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
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

public class AlertDialogInfoAmostra extends DialogFragment{
    private Amostra amostra;
    private int totalEspecie;
    private String coordsxy;
    private TextView userAmostra;

    public int getTotalEspecie() {
        return totalEspecie;
    }

    public void setTotalEspecie(int totalEspecie) {
        this.totalEspecie = totalEspecie;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public String getCoordsxy() {
        return coordsxy;
    }

    public void setCoordsxy(String coordsxy) {
        this.coordsxy = coordsxy;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")View view = inflater.inflate(R.layout.activity_amostra_info, null);
        TextView nameAmostra = view.findViewById(R.id.nameAmostra);
        TextView dateCreateAmostra = view.findViewById(R.id.dateCreateAmostra);
        TextView totalEspecies = view.findViewById(R.id.totalEspecies);
        userAmostra =  view.findViewById(R.id.userAmostra);
        TextView coords = view.findViewById(R.id.coords);
        TextView idAm = view.findViewById(R.id.idAm);
        TextView idLev = view.findViewById(R.id.idLev);

        DelegateDarwin delegateDarwin = new DelegateDarwin(getContext());
        PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getContext());
        String id = preferencesDarwin.getUserAccount();
        AsyncGetUser asyncTaskUser = new AsyncGetUser(delegateDarwin, updateInterface());
        asyncTaskUser.execute(id);

        nameAmostra.setText(getAmostra().getAm_name());
        dateCreateAmostra.setText(getAmostra().getAm_date());
        coords.setText(getCoordsxy());
        idAm.setText(getAmostra().getId_mask());
        idLev.setText(getAmostra().getId_mask_lev());
        totalEspecies.setText(String.valueOf(getTotalEspecie()));

        builder.setTitle(R.string.dataamostra);
        builder.setPositiveButton(R.string.fechar, (dialog, which) -> {

        });

        builder.setView(view);
        return builder.create();
    }

    AsyncGetUser.UpdateInterface updateInterface(){
        return userDarwin -> userAmostra.setText(userDarwin.getFirstName());
    }
}
