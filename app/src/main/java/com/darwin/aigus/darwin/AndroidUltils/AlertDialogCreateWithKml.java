package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Adapters.AdapterAsmotraDialog;
import com.darwin.aigus.darwin.Async.AsyncCreateAmostraWithKml;
import com.darwin.aigus.darwin.Async.AsyncGenerateAmostraWithKml;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlertDialogCreateWithKml extends DialogFragment {
    private Levantamento levantamento;
    private List<Amostra> listAmostrasFromKml;
    private Spinner listKml;
    private DataBaseDarwin dataBaseDarwin;
    private Context context;
    private ProgressBar progress;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView alertNoAmostra, typeError;
    private UpdateCreateAmostras updateCreateAmostras;

    public interface UpdateCreateAmostras {
        void upadateCreateAmostras();
        void closeDialog();
    }

    public void setUpdateCreateAmostras(UpdateCreateAmostras updateCreateAmostras) {
        this.updateCreateAmostras = updateCreateAmostras;
    }

    public AlertDialogCreateWithKml() {
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Levantamento getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Levantamento levantamento) {
        this.levantamento = levantamento;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dataBaseDarwin = new DataBaseDarwin(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.wait);
        progressDialog.setMessage(getContext().getString(R.string.criandoAmostra));

        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.activity_create_with_kml, null);
        alertNoAmostra = view.findViewById(R.id.alertNoAmostra);
        typeError = view.findViewById(R.id.typeError);
        progress = view.findViewById(R.id.progress);
        final LinearLayout contentListaKml = view.findViewById(R.id.contentListaKml);
        Button btnCreate = view.findViewById(R.id.btnCreate);
        listKml = view.findViewById(R.id.listKml);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
        List<String> stringFiles = manegerFiles.getListKmlFiles();
        final List<String> stringList = new ArrayList<>();
        stringList.add(getContext().getString(R.string.ultilListaKml));
        stringList.addAll(stringFiles);

        if (stringList.size() != 0) {
            listKml.setVisibility(View.VISIBLE);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, stringList);
            listKml.setAdapter(arrayAdapter);
            contentListaKml.setVisibility(View.VISIBLE);

        } else {
            noFilesKml(view);
        }

        listKml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nameFile = listKml.getSelectedItem().toString();

                if (!nameFile.equals(stringList.get(0))){
                    ManegerFiles manegerFiles1 = new ManegerFiles(helperManegerFile());
                    File dirFile = manegerFiles1.getDirSDCard("Kml", nameFile);
                    AsyncGenerateAmostraWithKml generateAmostraWithKml = new AsyncGenerateAmostraWithKml(dirFile, helperAsyncGenerate());
                    generateAmostraWithKml.execute("AMOSTRA", getLevantamento().getLev_id_mask());

                }else{
                    typeError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnCreate.setOnClickListener(v -> {
            String nameFile = listKml.getSelectedItem().toString();

            if (!nameFile.equals(stringList.get(0))){
                if(listAmostrasFromKml != null){
                    updateCreateAmostras.closeDialog();
                    AsyncCreateAmostraWithKml createAmostraWithKml = new AsyncCreateAmostraWithKml(dataBaseDarwin, listAmostrasFromKml, helperAsyncCreateAmostra());
                    createAmostraWithKml.execute();

                }else {
                    Toast.makeText(getContext(), R.string.typeErrorPoint, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> { });
        builder.setView(view);
        return builder.create();
    }

    private AsyncCreateAmostraWithKml.HelperAsync helperAsyncCreateAmostra(){
        return new AsyncCreateAmostraWithKml.HelperAsync() {
            @Override
            public void onPreExecuteHelper() {
                progressDialog.show();
            }

            @Override
            public void onPosExecuteHelper(boolean boo) {
                progressDialog.dismiss();
                if (boo){
                    Toast.makeText(getContext(), R.string.amostraCriadas, Toast.LENGTH_LONG).show();
                    updateCreateAmostras.upadateCreateAmostras();

                }else{
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_LONG).show();
                }

            }
        };
    }

    private AsyncGenerateAmostraWithKml.HelperAsync helperAsyncGenerate(){
        return new AsyncGenerateAmostraWithKml.HelperAsync() {
            @Override
            public void onPreExecuteHelper() {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPosExecuteHelper(List<Amostra> amostraList) {
                listAmostrasFromKml = amostraList;
                int totalAmostra = listAmostrasFromKml.size();
                if (totalAmostra != 0) {
                    String am = getContext().getString(R.string.amostras);
                    String a = totalAmostra + " " + am;
                    progress.setVisibility(View.INVISIBLE);
                    alertNoAmostra.setText(a);
                    recyclerView.setAdapter(new AdapterAsmotraDialog(listAmostrasFromKml));
                    recyclerView.setVisibility(View.VISIBLE);

                    typeError.setVisibility(View.INVISIBLE);

                } else {
                    warningGeometry();
                }
            }
        };
    }

    public void noFilesKml(View view){
        LinearLayout displayWarn = view.findViewById(R.id.displayWarn);
        TextView title = view.findViewById(R.id.title);
        TextView menssage = view.findViewById(R.id.menssage);
        title.setText(R.string.noFileKml);
        menssage.setText(R.string.mnoFileKml);
        displayWarn.setVisibility(View.VISIBLE);
        displayWarn.setOnClickListener(v -> displayWarn.setVisibility(View.GONE));

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

    private void warningGeometry(){
        typeError.setText(R.string.typeErrorPoint);
        typeError.setVisibility(View.VISIBLE);
        listAmostrasFromKml = null;
        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.INVISIBLE);
        alertNoAmostra.setText(R.string.noAmostras);

    }
}