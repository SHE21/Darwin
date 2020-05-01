package com.darwin.aigus.darwin.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Adapters.AdapterEspecie;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateEspecie;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogEditEspecie;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogInfoAmostra;
import com.darwin.aigus.darwin.AndroidUltils.AndroidSettings;
import com.darwin.aigus.darwin.AndroidUltils.ManegerFiles;
import com.darwin.aigus.darwin.AndroidUltils.UpdateEspecie;
import com.darwin.aigus.darwin.Async.AsyncCreateFileFromEspecies;
import com.darwin.aigus.darwin.Async.AsyncGetEspecies;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.io.File;
import java.util.List;

public class ActivityEspecie extends AppCompatActivity {
    private DataBaseDarwin dataBaseDarwin;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<Especie> especies;
    private int totalEspecie;
    private LinearLayout notification, contentTop;
    private Amostra amostra;
    private Levantamento levantamento;
    private ProgressBar progressBar;
    private int blockFab  = 1;
    private Toolbar toolbar;

    public Context getContext() {
        return this;
    }

    public Levantamento getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Levantamento levantamento) {
        this.levantamento = levantamento;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    private AlertDialogCreateEspecie createEspecie = new AlertDialogCreateEspecie();
    private AlertDialogEditEspecie editEspecie = new AlertDialogEditEspecie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especie);

        dataBaseDarwin = new DataBaseDarwin(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.wait);

        Amostra amostra = (Amostra)getIntent().getSerializableExtra("amost");
        setAmostra(amostra);

        Levantamento levantamento = (Levantamento)getIntent().getSerializableExtra("lev");
        setLevantamento(levantamento);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notification = findViewById(R.id.notification);
        contentTop = findViewById(R.id.contentTop);
        progressBar = findViewById(R.id.progressBar);
        TextView topDir = findViewById(R.id.topDir);

        String dir = levantamento.getLev_name()+ " / " + getAmostra().getAm_name();
        topDir.setText(dir);

        createEspecie.setCancelable(false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        showEspecies();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (blockFab != 0){
                createEspecie.setAmostra(getAmostra());
                createEspecie.setUpdateEspecie(new UpdateEspecie() {
                    @Override
                    public void updateEspecie() {
                        showEspecies();
                        createEspecie.dismiss();
                        blockFab = 1;
                        Toast.makeText(getContext(), R.string.addEspecie, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void blockFab() {
                        blockFab = 1;
                    }
                });
                createEspecie.show(getSupportFragmentManager(), null);
            }
            blockFab = 0;
        });
    }

    private AdapterEspecie.OnClickEspecie onClickEspecie(){
        return new AdapterEspecie.OnClickEspecie() {

            @Override
            public void onClickDelete(View view, int position) {
                Especie especie = especies.get(position);
                onCreateDialog(especie);
            }

            @Override
            public void onClickEdit(View view, int position) {
                Especie especie = especies.get(position);
                editEspecie.setAmostra(getAmostra());
                editEspecie.setEspecie(especie);
                editEspecie.setUpdateEspecie(new UpdateEspecie() {
                    @Override
                    public void updateEspecie() {
                        showEspecies();
                        editEspecie.dismiss();
                    }

                    @Override
                    public void blockFab() {

                    }
                });
                editEspecie.show(getSupportFragmentManager(), null);
            }

            @Override
            public void onClickItem(View view, int position) {
                Especie especie = especies.get(position);
                Intent intent = new Intent(getBaseContext(), ActivityDetalheEspecie.class);
                intent.putExtra("esp", especie);
                intent.putExtra("amost", getAmostra());
                startActivity(intent);
            }
        };
    }

    public void onCreateDialog(final Especie especie){
        final String e = especie.getEs_name();
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    DataBaseDarwin baseDarwin = new DataBaseDarwin(getBaseContext());
                    baseDarwin.deleteEspecie(especie);
                    showEspecies();
                    Toast.makeText(getBaseContext(),R.string.excluidoEspecie, Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.excluirEspecie);
        builder.setMessage("\"" + e + "\" " + getContext().getString(R.string.seraExcuida));
        builder.setPositiveButton(R.string.excluir, dialogClickListener);
        builder.setNegativeButton(R.string.cancel, dialogClickListener);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_especies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_info:
                AlertDialogInfoAmostra infoEspecies = new AlertDialogInfoAmostra();
                infoEspecies.setAmostra(getAmostra());
                infoEspecies.setTotalEspecie(totalEspecie);
                infoEspecies.setCoordsxy(getAmostra().getAm_geodata_lat() + ", " + getAmostra().getAm_geodata_long());
                infoEspecies.show(getSupportFragmentManager(), null);
                break;

            case R.id.action_download:
                String nameFile = getLevantamento().getLev_name()+ "_" + getAmostra().getAm_name() + ".csv";
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
                            AsyncCreateFileFromEspecies fileFromAmostra = new AsyncCreateFileFromEspecies(manegerFiles, helperAsyncFile(), levantamento, amostra, especies);

                            if (AndroidSettings.isExternalStorageWritable()){
                                String file = manegerFiles.createDirDarwinAmostras() + File.separator + nameFile;
                                fileFromAmostra.execute(file);
                            }

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.titleDownAmostra);
                builder.setMessage(getContext().getString(R.string.oArquivo) + " \"" + nameFile + "\" " + getContext().getString(R.string.onDirAmostra));
                builder.setPositiveButton(R.string.downFile, dialogClickListener);
                builder.setNegativeButton(R.string.cancel, dialogClickListener);

                //verifica se há especies para download
                if (totalEspecie != 0){//se sim mostra o dialog
                    builder.show();

                }else{//se não, mostra a mensagem
                    Toast.makeText(getBaseContext(), R.string.amostraSemEspecies, Toast.LENGTH_LONG).show();
                }
                break;

            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    public void showEspecies(){
        AsyncGetEspecies getEspecies = new AsyncGetEspecies(dataBaseDarwin, helperAsyncGetEspecies());
        getEspecies.execute(amostra.getId_mask());
    }

    private AsyncGetEspecies.HelperAsync helperAsyncGetEspecies(){
        return new AsyncGetEspecies.HelperAsync() {
            @Override
            public void onPreHelperExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPosHelperExecute(List<Especie> especieList) {
                totalEspecie = especieList.size();
                toolbar.setTitle(getAmostra().getAm_name());
                toolbar.setSubtitle(totalEspecie + " " + getContext().getString(R.string.especies));

                if (totalEspecie != 0){
                    especies = especieList;
                    recyclerView.setAdapter(new AdapterEspecie(especies, onClickEspecie()));
                    recyclerView.setVisibility(View.VISIBLE);

                    contentTop.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    notification.setVisibility(View.GONE);

                }else{
                    notification.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
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

    private AsyncCreateFileFromEspecies.HelperAsyncFile helperAsyncFile(){
        return new AsyncCreateFileFromEspecies.HelperAsyncFile() {
            @Override
            public void onPreHelperExecute() {
                progressDialog.setMessage("Criando o arquivo da Amostra ...");
                progressDialog.show();

            }

            @Override
            public void onPosHelperExecute(boolean boo) {
                progressDialog.dismiss();
                if (boo){
                    View parentLayout = findViewById(R.id.parent);
                    Snackbar.make(parentLayout, "Arquivo de Amostra criado!", Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(getBaseContext(), "Arquivo " + nameFile + " criado!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "Bloco try: Algo deu errado!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}