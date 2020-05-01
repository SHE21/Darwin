package com.darwin.aigus.darwin.AndroidUltils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.io.IOException;
import java.util.List;

/**
 * Created by SANTIAGO on 29/12/2017.
 */

public class AlertDialogEditEspecie extends DialogFragment {
    private Amostra amostra;
    private Especie especie;
    private EditText heightEspecie, diameterEspcie;
    private AutoCompleteTextView nameEspecie, familiaEspecie, cientificoEspecie;
    private Button btnSave;
    private UpdateEspecie updateEspecie;
    ReadTxt readTxt;

    private  boolean ativeNewLine = false;

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public void setUpdateEspecie(UpdateEspecie updateEspecie) {
        this.updateEspecie = updateEspecie;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_esp, null);

        cientificoEspecie = (AutoCompleteTextView)view.findViewById(R.id.cientificoEspecie);
        familiaEspecie = (AutoCompleteTextView) view.findViewById(R.id.familiaEspecie);

        //AUTOCOMPLETE FAMILY
        ReadTxt listFamily = new ReadTxt("familie.txt", getContext());
        List<String> listStringFamily = listFamily.getFamilyFromFile();
        final String[] familyArray = listFamily.convertToArray(listStringFamily);

        familiaEspecie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String famile = familiaEspecie.getText().toString();
                ReadTxt file = new ReadTxt(famile + ".txt", getContext());
                if (file.getBooleanFile()){
                    getAutoCompleteEspecies(file);

                }else{
                    Toast.makeText(getContext(), "Grupo de familia sem Espécies!", Toast.LENGTH_LONG).show();
                }
            }
        });
        ArrayAdapter<String> adapterFamilia = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, familyArray);
        familiaEspecie.setAdapter(adapterFamilia);
        //AUTOCOMPLETE FAMILY

        readTxt = new ReadTxt("especies.txt", getContext());
        List<String> t = readTxt.getListBiomasFromFile();
        final String[] s = readTxt.convertArray(t);
        Log.v("TEXTE", "" + s[1] + "");

        final TextView alertError = (TextView)view.findViewById(R.id.alertError);

        heightEspecie = (EditText)view.findViewById(R.id.heightEspecie);
        diameterEspcie = (EditText)view.findViewById(R.id.diameterEspcie);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setText("Salvar");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, s);
        nameEspecie = (AutoCompleteTextView) view.findViewById(R.id.nameEspecie);
        nameEspecie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String u = nameEspecie.getText().toString();
                String[] s = u.split("-");
                cientificoEspecie.setText(s[1]);
                ativeNewLine = true;

            }
        });
        nameEspecie.setAdapter(stringArrayAdapter);

        nameEspecie.setText(getEspecie().getEs_name());
        cientificoEspecie.setText(getEspecie().getEs_name_cient());
        familiaEspecie.setText(getEspecie().getEs_familia());
        heightEspecie.setText(String.valueOf(getEspecie().getEs_height()));
        diameterEspcie.setText(String.valueOf(getEspecie().getEs_diameter()));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String especieN = nameEspecie.getText().toString();
                String cientificoEspecieN = cientificoEspecie.getText().toString();
                String familiaEspecieN = familiaEspecie.getText().toString();
                String height = heightEspecie.getText().toString();
                String diameter = diameterEspcie.getText().toString();

                if (especieN.isEmpty() || height.isEmpty() || diameter.isEmpty()){
                    alertError.setVisibility(View.VISIBLE);

                }else {
                    DataBaseDarwin dataBaseDarwin = new DataBaseDarwin(getContext());
                    Especie especie = new Especie();

                    especie.setId(getEspecie().getId());
                    especie.setEs_name(especieN);
                    especie.setEs_name_cient(cientificoEspecieN);
                    especie.setEs_familia(familiaEspecieN);
                    especie.setEs_height(Double.parseDouble(height));
                    especie.setEs_diameter(Double.parseDouble(diameter));
                    especie.setEsp_data("Editado " + DateDarwin.getDate());

                    dataBaseDarwin.updateEspecie(especie);
                    Toast.makeText(getContext(), "Edições salvas!", Toast.LENGTH_SHORT).show();
                    updateEspecie.updateEspecie();
                }
            }
        });

        builder.setTitle("Editar Espécie \"" + getEspecie().getEs_name() + "\"");
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateEspecie.blockFab();

            }
        });
        builder.setView(view);
        return builder.create();
    }

    public void getAutoCompleteEspecies(ReadTxt readTxt){
        //ReadTxt listEspecie = new ReadTxt(family+".txt", getContext());Log.v("ARQUIVO", "" + listEspecie.getFileName());
        List<String> listStringEspecie = readTxt.getEspecieFromFile();
        String[] especieArray = readTxt.convertToArray(listStringEspecie);
        //AUTOCOMPLETE ESPECIE
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, especieArray);
        cientificoEspecie.setAdapter(adapterEspecie);
    }
}
