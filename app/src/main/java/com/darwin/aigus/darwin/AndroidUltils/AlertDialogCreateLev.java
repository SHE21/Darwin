package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Async.AsyncCreateLevant;
import com.darwin.aigus.darwin.Async.AsyncGenerateArea;
import com.darwin.aigus.darwin.Async.AsyncInsertArea;
import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlertDialogCreateLev extends DialogFragment {
    private List<DarwinPolygon> polygonList;
    private DataBaseDarwin dataBaseDarwin;
    private EditText nameLev;
    private Spinner listKml;
    private Context context;
    private TextView typeError;
    private UpdateLevantamento updateLevantamento;

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUpdateLevantamento(UpdateLevantamento updateLevantamento) {
        this.updateLevantamento = updateLevantamento;
    }

    public interface UpdateLevantamento{
        void updateLevantamento();
        void blockFab();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_create_lev, null);

        dataBaseDarwin = new DataBaseDarwin(getContext());

        Button btnCreateLev = view.findViewById(R.id.btnCreateLev);
        nameLev = view.findViewById(R.id.nameLev);
        listKml = view.findViewById(R.id.listKml);
        typeError = view.findViewById(R.id.typeError);

        ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
        List<String> stringFiles = manegerFiles.getListKmlFiles();
        final List<String> stringList = new ArrayList<>();
        stringList.add(getResources().getString(R.string.ultilListaKml));
        stringList.addAll(stringFiles);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, stringList);
        listKml.setAdapter(arrayAdapter);

        listKml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nameFile = listKml.getSelectedItem().toString();
                if (!nameFile.equals(stringList.get(0))){
                    File dirFile = manegerFiles.getDirSDCard("Kml", nameFile);
                    AsyncGenerateArea generateArea = new AsyncGenerateArea(dirFile, helperAsyncGenerateArea());
                    generateArea.execute();

                }else{
                    nameLev.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView alertError = view.findViewById(R.id.alertError);

        btnCreateLev.setOnClickListener(v -> {
            String idRadom = GenerateId.generateId();
            Levantamento levantamento = new Levantamento();

            String name = nameLev.getText().toString();
            String nameFile = listKml.getSelectedItem().toString();

            levantamento.setLev_name(name);
            levantamento.setLev_geodata(idRadom);
            levantamento.setLev_id_mask(idRadom);
            levantamento.setLev_date(DateDarwin.getDate());

            AsyncCreateLevant createLevant = new AsyncCreateLevant(dataBaseDarwin,helperAsyncTaskCreateLev());

            if (!name.isEmpty()) {
                if(name.length() <= 30) {
                    if (!nameFile.equals(stringList.get(0))) {

                        if (polygonList != null) {
                            AsyncInsertArea insertArea = new AsyncInsertArea(dataBaseDarwin, levantamento.getLev_id_mask(), helperAsyncInsertArea(), polygonList);
                            insertArea.execute();
                            createLevant.execute(levantamento);

                        } else {
                            Toast.makeText(getContext(), R.string.typeErrorPoly, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        createLevant.execute(levantamento);
                    }
                }else{
                    alertError.setVisibility(View.VISIBLE);
                    alertError.setText(R.string.exceptionNameNumber);
                }

            }else{
                alertError.setVisibility(View.VISIBLE);
                alertError.setText(R.string.errorCampoBranco2);
            }
        });

        builder.setTitle(R.string.criarLevantamento);//criar levantamento
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {//cancelar
            updateLevantamento.blockFab();

        });

        builder.setView(view);
        return builder.create();
    }

    private AsyncInsertArea.HelperAsync helperAsyncInsertArea(){
        return new AsyncInsertArea.HelperAsync() {
            @Override
            public void onPreHelperExecute() {
            }

            @Override
            public void onPosHelperExecute(boolean boo) {
                if (!boo){
                    Toast.makeText(getContext(), R.string.dataBaseErro, Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    private AsyncCreateLevant.HelperAsyncTask helperAsyncTaskCreateLev(){
        return new AsyncCreateLevant.HelperAsyncTask() {
            @Override
            public void onPreHelperExecute() {
            }

            @Override
            public void onPosHelperExecute(long result) {
                if (result != 0){
                    updateLevantamento.updateLevantamento();
                    Toast.makeText(getContext(), R.string.criadoLevant, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), R.string.dataBaseErro, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private AsyncGenerateArea.HelperAsync helperAsyncGenerateArea(){
        return new AsyncGenerateArea.HelperAsync() {
            @Override
            public void onPreHelperExecute() {

            }

            @Override
            public void onPostHelperExecute(List<DarwinPolygon> darwinPolygons) {
                polygonList = darwinPolygons;
                if (polygonList != null){
                    nameLev.setText(listKml.getSelectedItem().toString().replace(".kml", ""));
                    typeError.setVisibility(View.INVISIBLE);

                }else{
                    polygonList = null;
                    nameLev.setText("");
                    typeError.setText(R.string.typeErrorPoly);
                    typeError.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private ManegerFiles.HelperManegerFile helperManegerFile(){
        return new ManegerFiles.HelperManegerFile() {
        @Override
        public void dirNotFound() {

        }

        @Override
        public void getPermission() {

        }

        @Override
        public void createFile() {

        }
    };}
}
