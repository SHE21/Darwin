package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

public class AlertDialogEditAmostra extends DialogFragment{
    private Amostra amostra;
    private EditText latAmostras, longAmostras;
    private UpdateAmostras updateAmostras;

    public void setUpdateAmostras(UpdateAmostras updateAmostras) {
        this.updateAmostras = updateAmostras;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_create_am, null);
        TextView nameAmostra = view.findViewById(R.id.nameAmostra);
        nameAmostra.setText(getAmostra().getAm_name());

        longAmostras = view.findViewById(R.id.longAmostras);
        String lon = String.valueOf(getAmostra().getAm_geodata_long());
        longAmostras.setText(lon);

        latAmostras = view.findViewById(R.id.latAmostras);
        String lat = String.valueOf(getAmostra().getAm_geodata_lat());
        latAmostras.setText(lat);

        final LinearLayout displayWarn = view.findViewById(R.id.displayWarn);
        displayWarn.setOnClickListener(v -> displayWarn.setVisibility(View.GONE));

        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.oneDicaTitle);
        TextView menssage = view.findViewById(R.id.menssage);
        menssage.setText(R.string.oneDicaMessage);

        final TextView alertError = view.findViewById(R.id.alertError);

        Button btnCreateAmost = view.findViewById(R.id.btnCreateAmost);
        btnCreateAmost.setText(R.string.save);
        btnCreateAmost.setOnClickListener(v -> {
            String amostraName = getAmostra().getAm_name();
            String latitude = latAmostras.getText().toString();
            String longitude = longAmostras.getText().toString();

            if (amostraName.isEmpty() || latitude.isEmpty() || longitude.isEmpty()){
                alertError.setVisibility(View.VISIBLE);

            }else{

                try{
                    double x = Double.parseDouble(latitude);
                    double y = Double.parseDouble(longitude);

                    if(DarwinGeometryAnalyst.validateCoord(x) || DarwinGeometryAnalyst.validateCoord(y)){
                        alertError.setText(R.string.coordIncorrectFormat);
                        alertError.setVisibility(View.VISIBLE);

                    }else{
                        Amostra amostra = new Amostra();
                        amostra.setId(getAmostra().getId());
                        amostra.setAm_name(amostraName);
                        amostra.setAm_geodata_lat(x);
                        amostra.setAm_geodata_long(y);
                        amostra.setAm_date(DateDarwin.getDate());

                        DataBaseDarwin baseDarwin = new DataBaseDarwin(getContext());
                        int t = baseDarwin.updateAmostra(amostra);

                        if(t != 0){
                            Toast.makeText(getContext(),amostraName+" " + getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                            updateAmostras.updateAmostras();

                        }else{
                            Toast.makeText(getContext(), getResources().getString(R.string.dataBaseErro), Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch(NumberFormatException e){
                    alertError.setText(R.string.onlyNumberFormat);
                    alertError.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setTitle(getContext().getString(R.string.editarAmostra) +" "+ getAmostra().getAm_name());
        builder.setPositiveButton(R.string.cancel, (dialog, which) -> updateAmostras.blockFab());
        builder.setView(view);
        return builder.create();
    }

    public Amostra getAmostra(){
        return this.amostra;
    }

    public void setAmostra(Amostra amostra){
        this.amostra = amostra;
    }
}
