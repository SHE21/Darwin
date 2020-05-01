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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Async.AsyncGenerateArea;
import com.darwin.aigus.darwin.Async.AsyncGetAreaLevant;
import com.darwin.aigus.darwin.Async.AsyncInsertArea;
import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlertDialogEditLevantamento extends DialogFragment {
    private DataBaseDarwin dataBaseDarwin;
    private int relevate;
    private List<DarwinPolygon> polygonList;
    private Levantamento levantamento;
    private EditText nameLev;
    private Spinner listKml;
    private LinearLayout displayWarn;
    private Context context;
    private TextView typeError,alertError, infoEscolha, title, menssage;
    private UpdateLevantamento updateLevantamento;

    public Levantamento getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Levantamento levantamento) {
        this.levantamento = levantamento;
    }

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

        AsyncGetAreaLevant getAreaLevant = new AsyncGetAreaLevant(dataBaseDarwin, helperAsyncGetArea());
        getAreaLevant.execute(getLevantamento().getLev_id_mask());

        Button btnCreateLev = view.findViewById(R.id.btnCreateLev);
        nameLev = view.findViewById(R.id.nameLev);
        listKml = view.findViewById(R.id.listKml);
        typeError = view.findViewById(R.id.typeError);
        infoEscolha = view.findViewById(R.id.infoEscolha);
        displayWarn = view.findViewById(R.id.displayWarn);
        menssage = view.findViewById(R.id.menssage);
        alertError = view.findViewById(R.id.alertError);
        title = view.findViewById(R.id.title);

        nameLev.setText(getLevantamento().getLev_name());
        btnCreateLev.setText(R.string.save);

        ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
        List<String> stringFiles = manegerFiles.getListKmlFiles();
        final List<String> stringList = new ArrayList<>();
        stringList.add(getContext().getString(R.string.ultilListaKml));
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCreateLev.setOnClickListener(v -> {
            DataBaseDarwin db = new DataBaseDarwin(getContext());
            Levantamento levantamento = new Levantamento();

            String name = nameLev.getText().toString();
            String nameFile = listKml.getSelectedItem().toString();

            levantamento.setLev_name(name);
            levantamento.setLev_id_mask(getLevantamento().getLev_id_mask());
            levantamento.setLev_date(DateDarwin.getDate());

            if (!name.isEmpty()) {
                if (!nameFile.equals(stringList.get(0))) {
                    if (polygonList != null || relevate == 0) {
                        AsyncInsertArea insertArea = new AsyncInsertArea(dataBaseDarwin, levantamento.getLev_id_mask(), helperAsyncInsertArea(), polygonList);
                        insertArea.execute();
                        generateLevantamento(levantamento, db);

                    }else{
                        Toast.makeText(getContext(), R.string.typeErrorPoly, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    generateLevantamento(levantamento, db);
                }

            }else{
                alertError.setVisibility(View.VISIBLE);
                alertError.setText(R.string.errorCampoBranco);
            }
        });

        builder.setTitle(getContext().getString(R.string.editarLevantamento));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> updateLevantamento.blockFab());

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
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
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
                    typeError.setText(R.string.typeErrorPoly);
                    typeError.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private AsyncGetAreaLevant.HelperAsync helperAsyncGetArea(){
        return new AsyncGetAreaLevant.HelperAsync() {
            @Override
            public void onHelperPreExecute() {
            }

            @Override
            public void onHelperPosExecute(List<KmlPolygon> KmlPolygon) {
                relevate = KmlPolygon.size();
                if (relevate != 0){
                    displayWarn.setVisibility(View.VISIBLE);
                    menssage.setText(R.string.noEditGeoData);
                    title.setText(R.string.noEditTitle);
                    displayWarn.setVisibility(View.VISIBLE);
                    listKml.setVisibility(View.GONE);
                    infoEscolha.setVisibility(View.GONE);
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
        };
    }

    public void generateLevantamento(Levantamento levantamento , DataBaseDarwin db){
        db.updateLev(levantamento.getLev_id_mask(), levantamento.getLev_name());
        updateLevantamento.updateLevantamento();
        Toast.makeText(getContext(),R.string.saved, Toast.LENGTH_SHORT).show();
    }
}
