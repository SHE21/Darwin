package com.darwin.aigus.darwin.AndroidUltils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by SANTIAGO on 29/12/2017.
 */

public class AlertDialogCreateAmostra extends DialogFragment {
    private Levantamento levantamento;
    public List<Amostra> amostra;
    private EditText latAmostras, longAmostras;
    private TextView alertError, nameAmostra;
    private Button btnCreateAmost;
    private UpdateAmostras updateAmostra;

    public void setUpdateAmostra(UpdateAmostras updateAmostra) {
        this.updateAmostra = updateAmostra;
    }

    public Levantamento getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Levantamento levantamento) {
        this.levantamento = levantamento;
    }

    public List<Amostra> getAmostra() {
        return amostra;
    }

    public void setAmostra(List<Amostra> amostra) {
        this.amostra = amostra;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_am, null);

        nameAmostra = view.findViewById(R.id.nameAmostra);
        latAmostras = view.findViewById(R.id.latAmostras);
        longAmostras = view.findViewById(R.id.longAmostras);
        btnCreateAmost = view.findViewById(R.id.btnCreateAmost);

        alertError = view.findViewById(R.id.alertError);
        final LinearLayout displayWarn = view.findViewById(R.id.displayWarn);
        displayWarn.setOnClickListener(v -> displayWarn.setVisibility(View.GONE));

        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.oneDicaTitle);
        TextView menssage = view.findViewById(R.id.menssage);
        menssage.setText(R.string.oneDicaMessage);

        nameAmostra.setText(suggestNameAmostra(getAmostra()));

        btnCreateAmost.setOnClickListener(v -> {
            DataBaseDarwin baseDarwin = new DataBaseDarwin(getContext());
            Amostra amostra = new Amostra();

            String nomeAmostra = suggestNameAmostra(getAmostra());
            String latitude = latAmostras.getText().toString();
            String longitude = longAmostras.getText().toString();

            if (nomeAmostra.isEmpty() || latitude.isEmpty() || longitude.isEmpty()){
                alertError.setVisibility(View.VISIBLE);
                alertError.setOnClickListener(v1 -> alertError.setVisibility(View.GONE));

            }else{

                try{
                    double x = Double.parseDouble(latitude);
                    double y = Double.parseDouble(longitude);

                    if(DarwinGeometryAnalyst.validateCoord(x) || DarwinGeometryAnalyst.validateCoord(y)){
                        alertError.setText(R.string.coordIncorrectFormat);
                        alertError.setVisibility(View.VISIBLE);

                    }else{
                        amostra.setId_mask(GenerateId.generateId());
                        amostra.setId_mask_lev(getLevantamento().getLev_id_mask());
                        amostra.setAm_name(nomeAmostra);
                        amostra.setAm_geodata_lat(x);
                        amostra.setAm_geodata_long(y);
                        amostra.setAm_date(DateDarwin.getDate());

                        String menssage1 = nomeAmostra + " " + getResources().getString(R.string.criadoAmostra);
                        Toast.makeText(getContext(), menssage1, Toast.LENGTH_SHORT).show();
                        baseDarwin.insertAmostra(amostra);
                        updateAmostra.updateAmostras();
                    }

                }catch(NumberFormatException e){
                    alertError.setText(R.string.onlyNumberFormat);
                    alertError.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setTitle(R.string.criarAmostra);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> updateAmostra.blockFab());

        builder.setView(view);
        return builder.create();
    }

    public static String suggestNameAmostra(List<Amostra> amostraList){
        int size = amostraList.size();
        if (size != 0) {
            Amostra amostra = amostraList.get(size - 1);
            String firstString = amostra.getAm_name();
            //ENCONTRA A PRIMEIRA OCORRENCIA DE "_"
            int index = firstString.indexOf("_");
            //TAMANHO DE firstString
            int lengh = firstString.length();
            //EXTRAI CARACTERES ENTRE index + 1 e lengh
            String numberName =  firstString.substring(index + 1, lengh);
            //VONVER O RESULTADO DE INTEIRO
            int number = Integer.parseInt(numberName);
            //SOMA O RESULTADO COM UM
            int renameNumber = number + 1;
            return "AMOSTRA_" + renameNumber;
        }else{
            return "AMOSTRA_0";
        }
    }
}
