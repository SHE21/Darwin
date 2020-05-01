package com.darwin.aigus.darwin.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Adapters.AdapterAsmotra;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateAmostra;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateWithKml;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogEditAmostra;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogInfoLev;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogSeeMapLevAll;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogSeeMapAmostra;
import com.darwin.aigus.darwin.AndroidUltils.DarwinLisenca;
import com.darwin.aigus.darwin.AndroidUltils.ManegerFiles;
import com.darwin.aigus.darwin.AndroidUltils.UpdateAmostras;
import com.darwin.aigus.darwin.Async.AsyncCreateFileFromEspecies;
import com.darwin.aigus.darwin.Async.AsyncDeleteAllAmostras;
import com.darwin.aigus.darwin.Async.AsyncDeleteOneAmostra;
import com.darwin.aigus.darwin.Async.AsyncGetAmostras;
import com.darwin.aigus.darwin.Async.AsyncGetEspeciesByLevant;
import com.darwin.aigus.darwin.Async.AsyncTaskDeleteGeoLev;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.io.File;
import java.util.List;

public class ActivityAmostras extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private DataBaseDarwin dataBaseDarwin;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<Amostra> amostras;
    private int totalAmostra;
    private Levantamento levantamento;
    private LinearLayout notification;
    private AdapterAsmotra adapterAsmotra;
    private Context context;
    private int blockFab = 1;
    private Toolbar toolbar;
    private TextView topDir;
    private boolean l = true;

    public void setContext(){
        this.context = this;
    }

    public Context getContext() {
        return this.context;
    }

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

    public List<Amostra> getAmostras() {
        return amostras;
    }

    public void setAmostras(List<Amostra> amostras) {
        this.amostras = amostras;
    }

    AlertDialogCreateAmostra createAmostra = new AlertDialogCreateAmostra();
    AlertDialogEditAmostra editAmostra = new AlertDialogEditAmostra();
    AlertDialogCreateWithKml createWithKml = new AlertDialogCreateWithKml();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amostras);
        setContext();

        dataBaseDarwin = new DataBaseDarwin(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.wait);

        Levantamento l = (Levantamento)getIntent().getSerializableExtra("lev");
        setLevantamento(l);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(l.getLev_name());
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //container de notificação, caso não haja levantamentos
        notification = findViewById(R.id.notification);
        //display de diretorio
        topDir = findViewById(R.id.topDir);
        //trava fechamento dos dialogs
        createAmostra.setCancelable(false);
        editAmostra.setCancelable(false);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        showAmostras();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            createAmostra.setLevantamento(getLevantamento());
            createAmostra.setAmostra(getAmostras());
            if (blockFab != 0){
                createAmostra.setUpdateAmostra(new UpdateAmostras() {
                    @Override
                    public void updateAmostras() {
                        showAmostras();
                        createAmostra.dismiss();
                        blockFab = 1;
                    }

                    @Override
                    public void blockFab() {
                        blockFab = 1;

                    }
                });
                createAmostra.show(getSupportFragmentManager(), null);
            }
            blockFab = 0;
        });
    }
    //implementação dos listeners do adapter Amostras
    private AdapterAsmotra.OnClickAsmotra onClickAsmotra(){
        return new AdapterAsmotra.OnClickAsmotra() {
            @Override
            public void onClickEdit(View view, int position) {
                if (blockFab != 0) {
                    Amostra amostra = amostras.get(position);
                    editAmostra.setAmostra(amostra);
                    editAmostra.setUpdateAmostras(new UpdateAmostras() {
                        @Override
                        public void updateAmostras() {
                            showAmostras();
                            editAmostra.dismiss();
                            blockFab = 1;
                        }

                        @Override
                        public void blockFab() {
                            blockFab = 1;

                        }
                    });
                    editAmostra.show(getSupportFragmentManager(), null);
                }
                blockFab = 0;
            }

            @Override
            public void onClickDelete(View view, int position) {
                Amostra amostra = amostras.get(position);
                onCreateDialog(amostra);

            }

            @Override
            public void onClickSeeMap(View view, int position) {
                DarwinLisenca darwinLisenca = new DarwinLisenca(getContext(), dialogLicensa());

                AlertDialogSeeMapAmostra seeMapAmostra = new AlertDialogSeeMapAmostra();
                seeMapAmostra.setLevantamento(getLevantamento());
                seeMapAmostra.setSelectedAmostra(position);

                if (l){
                    seeMapAmostra.show(getSupportFragmentManager(), null);

                }else {
                    darwinLisenca.onCreateDialog();
                }
            }

            @Override
            public void onClickItem(View view, int position) {
                Amostra amostra = amostras.get(position);
                Toast.makeText(context, "item : " + amostra.getAm_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ActivityEspecie.class);
                intent.putExtra("lev", getLevantamento());
                intent.putExtra("amost", amostra);
                startActivity(intent);

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_amostras, menu);
        // menu search
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        DarwinLisenca darwinLisenca = new DarwinLisenca(getContext(), dialogLicensa());

        switch (item.getItemId()){
            case R.id.action_delete:
                if (getTotalAmostra() != 0){
                    onCreateDialogDelete();
                }else{
                    Toast.makeText(getContext(),R.string.noAmostraExcluir, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.action_graphi:
                Intent intent = new Intent(this, ActivityGraph.class);
                intent.putExtra("lev", getLevantamento());
                startActivity(intent);
                break;

            case R.id.action_info:
                AlertDialogInfoLev infoAmostra = new AlertDialogInfoLev();
                // manda objeto atravez do metodo getLevantamento() ao AlertDialog
                infoAmostra.setLevantamento(getLevantamento());
                infoAmostra.setTotalAmostra(getTotalAmostra());
                infoAmostra.show(getSupportFragmentManager(), null);
                break;

            case R.id.action_upload:
                if (l){
                    createWithKml.setUpdateCreateAmostras(upadateAmostras());
                    createWithKml.setLevantamento(getLevantamento());
                    createWithKml.setContext(getContext());
                    createWithKml.show(getSupportFragmentManager(), null);

                }else{
                    darwinLisenca.onCreateDialog();
                }
                break;

            case R.id.action_down_especies:
                onCreateDialogDownload();
                break;

            case R.id.action_see_map:
                if (l){
                    Intent seeMap = new Intent(ActivityAmostras.this, ActivitySeeMap.class);
                    seeMap.putExtra("lev", getLevantamento());
                    startActivity(seeMap);

                }else{
                    darwinLisenca.onCreateDialog();
                }
                break;

            case R.id.action_delete_datageo:
                onDialogDeleteGeolev();
                break;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Amostra> amostraList = getAmostras();

        for (int i = 0; i < getAmostras().size(); i++){
            String amost = getAmostras().get(i).getAm_name().toLowerCase();
            if (!amost.contains(newText)){
                amostraList.remove(i);
            }
        }

        adapterAsmotra.setFilter(getAmostras());
        return true;
    }

    public void showAmostras(){
        AsyncGetAmostras asyncGetAmostras = new AsyncGetAmostras(dataBaseDarwin, helperAsyncGetAmostras());
        asyncGetAmostras.execute(getLevantamento().getLev_id_mask());
    }

    private AsyncGetAmostras.HelperAsyncGetAmostras helperAsyncGetAmostras(){
        return new AsyncGetAmostras.HelperAsyncGetAmostras() {
            @Override
            public void onPreHelperExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPosHelperExecute(List<Amostra> amostraList) {
                setTotalAmostra(amostraList.size());
                setAmostras(amostraList);

                adapterAsmotra = new AdapterAsmotra(amostraList, getBaseContext(), onClickAsmotra());
                toolbar.setSubtitle(getTotalAmostra() + " Amostras");
                String nameDir = getLevantamento().getLev_name()+ " / ";
                topDir.setText(nameDir);
                if (getTotalAmostra() != 0) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapterAsmotra);
                    notification.setVisibility(View.GONE);

                }else{
                    recyclerView.setVisibility(View.GONE);
                    notification.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }

            }
        };
    }

    public void onCreateDialogDelete(){
        DialogInterface.OnClickListener dialogInterface = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    AsyncDeleteAllAmostras asyncDeleteAllAmostras = new AsyncDeleteAllAmostras(dataBaseDarwin, helperAsyncDeleteAllAmostras());
                    asyncDeleteAllAmostras.execute(getLevantamento());

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(R.string.action_delete_allamostra);
        alertDialog.setMessage(R.string.excluirMsnAllAmostras);
        alertDialog.setPositiveButton(R.string.excluir, dialogInterface);
        alertDialog.setNegativeButton(R.string.cancel, dialogInterface);
        alertDialog.show();
    }
    //BAIXAR TODAS AS ESPECIES
    public void onCreateDialogDownload(){
        DialogInterface.OnClickListener dialogInterface = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    AsyncGetEspeciesByLevant getEspeciesByLevant = new AsyncGetEspeciesByLevant(dataBaseDarwin,helperAsyncGetEspeciesByLevant());
                    getEspeciesByLevant.execute(getLevantamento().getLev_id_mask());

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Baixar todas as Espécies do Levantamento");
        alertDialog.setMessage("Esta é uma operação um pouco demorada");
        alertDialog.setPositiveButton(R.string.excluir, dialogInterface);
        alertDialog.setNegativeButton(R.string.cancel, dialogInterface);
        alertDialog.show();
    }
    //DIALO PARA APAGAR DADOS GEOGRAFICOS DO LEVANTAMENTO
    public void onDialogDeleteGeolev(){
        DialogInterface.OnClickListener dialogInterface = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    AsyncTaskDeleteGeoLev geoLev = new AsyncTaskDeleteGeoLev(getContext(), getLevantamento().getLev_id_mask());
                    geoLev.execute();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(R.string.excluirDataGeo);
        alertDialog.setMessage(R.string.excluirMsnDataGeo);
        alertDialog.setPositiveButton(R.string.excluir, dialogInterface);
        alertDialog.setNegativeButton(R.string.cancel, dialogInterface);
        alertDialog.show();
    }

    public void onCreateDialog(final Amostra amostra){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    startDeleteAmostra(amostra);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.excluirAmostra);
        builder.setMessage(R.string.excluirMsnAmostras);
        builder.setPositiveButton(R.string.excluir, dialogClickListener);
        builder.setNegativeButton(R.string.cancel, dialogClickListener);
        builder.show();
    }

    public void startDeleteAmostra(Amostra amostra){
        AsyncDeleteOneAmostra deleteAmostras = new AsyncDeleteOneAmostra(dataBaseDarwin, new AsyncDeleteOneAmostra.HelperAsyncTask() {
            @Override
            public void onUpdate() {
                showAmostras();
            }

            @Override
            public void onPreExecute() {
                progressDialog.setMessage(getContext().getString(R.string.excluindoAmostra));
                progressDialog.show();

            }

            @Override
            public void onPostExecute() {
                progressDialog.dismiss();
                showAmostras();
                Toast.makeText(getContext(), R.string.amostraExcluida, Toast.LENGTH_SHORT).show();
            }
        });
        deleteAmostras.execute(amostra);
    }

    public AsyncDeleteAllAmostras.HelperAsync helperAsyncDeleteAllAmostras(){
        return new AsyncDeleteAllAmostras.HelperAsync() {
            @Override
            public void onPreHelperExecute() {
                progressDialog.setMessage(getContext().getString(R.string.excluindoAmostras));
                progressDialog.show();
            }

            @Override
            public void onPosHelperExecute(boolean boo) {
                progressDialog.dismiss();
                if (boo){
                    showAmostras();
                    Toast.makeText(getContext(), R.string.amostraExcluida, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), R.string.dataBaseErro, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public AlertDialogCreateWithKml.UpdateCreateAmostras upadateAmostras(){
        return new AlertDialogCreateWithKml.UpdateCreateAmostras(){
            @Override
            public void upadateCreateAmostras() {
                showAmostras();
            }

            @Override
            public void closeDialog() {
                createWithKml.dismiss();
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showAmostras();
    }

    public AlertDialogSeeMapLevAll.ReloadAmostras realodAmostras(){
        return this::showAmostras;
    }

    DialogInterface.OnClickListener dialogLicensa(){return (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(getContext(), ActivityLicense.class);
                startActivity(intent);

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    };}

    private AsyncGetEspeciesByLevant.HelperAsync helperAsyncGetEspeciesByLevant(){
        return new AsyncGetEspeciesByLevant.HelperAsync() {
            @Override
            public void onPreHelperExecute() {

            }

            @Override
            public void onPosHelperExecute(List<Especie> especieList) {
                String nameFile = getLevantamento().getLev_name() + ".csv";
                /*if (especieList.size() != 0){
                    ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
                    AsyncCreateFileFromEspecies fileFromAmostra = new AsyncCreateFileFromEspecies(manegerFiles, helperAsyncFile(), levantamento, amostra, especieList);

                    if (AndroidSettings.isExternalStorageWritable()){
                        String file = manegerFiles.createDirDarwinAmostras() + File.separator + nameFile;
                        fileFromAmostra.execute(file);
                    }
                } */
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
}

