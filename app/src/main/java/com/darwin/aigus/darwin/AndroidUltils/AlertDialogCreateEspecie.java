package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
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

public class AlertDialogCreateEspecie extends DialogFragment {
    private Amostra amostra;
    private EditText heightEspecie, diameterEspcie;
    private AutoCompleteTextView nameEspecie, familiaEspecie, cientificoEspecie;
    private UpdateEspecie updateEspecie;
    ReadTxt readTxt;

    private  boolean ativeNewLine = false;

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public void setUpdateEspecie(UpdateEspecie updateEspecie) {
        this.updateEspecie = updateEspecie;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_create_esp, null);

        cientificoEspecie = view.findViewById(R.id.cientificoEspecie);
        familiaEspecie =  view.findViewById(R.id.familiaEspecie);

        //AUTOCOMPLETE FAMILY
        ReadTxt listFamily = new ReadTxt("familie.txt", getContext());
        List<String> listStringFamily = listFamily.getFamilyFromFile();
        final String[] familyArray = listFamily.convertToArray(listStringFamily);

        familiaEspecie.setOnItemClickListener((parent, view12, position, id) -> {
            String famile = familiaEspecie.getText().toString();
            ReadTxt file = new ReadTxt(famile + ".txt", getContext());
            if (file.getBooleanFile()){
                getAutoCompleteEspecies(file);

            }else{
                Toast.makeText(getContext(), R.string.familyNoEspecies, Toast.LENGTH_LONG).show();
            }
        });
        ArrayAdapter<String> adapterFamilia = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, familyArray);
        familiaEspecie.setAdapter(adapterFamilia);

        readTxt = new ReadTxt("especies.txt", getContext());
        List<String> t = readTxt.getListBiomasFromFile();
        final String[] s = readTxt.convertArray(t);

        TextView alertError = view.findViewById(R.id.alertError);

        heightEspecie = view.findViewById(R.id.heightEspecie);
        diameterEspcie = view.findViewById(R.id.diameterEspcie);
        Button btnSave = view.findViewById(R.id.btnSave);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, s);
        nameEspecie = view.findViewById(R.id.nameEspecie);
        nameEspecie.setOnItemClickListener((parent, view1, position, id) -> {
            String u = nameEspecie.getText().toString();
            String[] s1 = u.split("-");
            cientificoEspecie.setText(s1[1]);
            ativeNewLine = true;

        });
        nameEspecie.setAdapter(stringArrayAdapter);

        btnSave.setOnClickListener(v -> {

            String especieN = nameEspecie.getText().toString();
            String nameCientN = cientificoEspecie.getText().toString();
            String familiaEspecieN = familiaEspecie.getText().toString();
            String height = heightEspecie.getText().toString();
            String diameter = diameterEspcie.getText().toString();

            if (especieN.isEmpty() || height.isEmpty() || diameter.isEmpty()){
                alertError.setVisibility(View.VISIBLE);

            }else {
                DataBaseDarwin dataBaseDarwin = new DataBaseDarwin(getContext());
                Especie especie = new Especie();

                especie.setId_mask_esp(GenerateId.generateId());
                especie.setId_mask_am(amostra.getId_mask());
                especie.setId_mask_lev(amostra.getId_mask_lev());
                especie.setEs_name(especieN);
                especie.setEs_name_cient(nameCientN);
                especie.setEs_familia(familiaEspecieN);
                especie.setEs_height(Double.parseDouble(height));
                especie.setEs_diameter(Double.parseDouble(diameter));
                especie.setEsp_data(DateDarwin.getDate());

                if (!ativeNewLine){
                    try {
                        readTxt.inserNewLine(especieN);
                        Toast.makeText(getContext(), "STATUS: " + readTxt.inserNewLine(especieN), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                dataBaseDarwin.insertEspecie(especie);
                updateEspecie.updateEspecie();
            }
        });

        builder.setTitle(R.string.adEspecie);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> updateEspecie.blockFab());
        builder.setView(view);
        return builder.create();
    }

    public void getAutoCompleteEspecies(ReadTxt readTxt){
        List<String> listStringEspecie = readTxt.getEspecieFromFile();
        String[] especieArray = readTxt.convertToArray(listStringEspecie);
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, especieArray);
        cientificoEspecie.setAdapter(adapterEspecie);
    }
}
