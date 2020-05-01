package com.darwin.aigus.darwin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.Activity.ActivityAmostras;
import com.darwin.aigus.darwin.Activity.ActivityUser;
import com.darwin.aigus.darwin.Adapters.AdapterLev;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogCreateLev;
import com.darwin.aigus.darwin.AndroidUltils.AlertDialogEditLevantamento;
import com.darwin.aigus.darwin.AndroidUltils.AndroidSettings;
import com.darwin.aigus.darwin.AndroidUltils.KmlPolygon;
import com.darwin.aigus.darwin.AndroidUltils.ManegerFiles;
import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;
import com.darwin.aigus.darwin.Async.AsyncDeleteLevant;
import com.darwin.aigus.darwin.Async.AsyncGetLevant;
import com.darwin.aigus.darwin.Async.AsyncGetUser;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private DataBaseDarwin dataBaseDarwin;
    private DelegateDarwin delegateDarwin;
    private RecyclerView recyclerView;
    private List<Levantamento> levantamento;
    private AdapterLev adapterLev;
    private Toolbar toolbar;
    private LinearLayout notification, contentTop;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private int totalLev;
    private TextView displayTotal;
    private Context context;
    private int blockFab = 1;
    private AlertDialogEditLevantamento editLevantamento = new AlertDialogEditLevantamento();
    private AlertDialogCreateLev createLev = new AlertDialogCreateLev();

    public Context getContext() {
        return context = this;
    }

    public void setContext() {
        this.context = this;
    }

    public int getTotalLev() {
        return totalLev;
    }

    public void setTotalLev(int totalLev) {
        this.totalLev = totalLev;
    }

    public List<Levantamento> getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(List<Levantamento> levantamento) {
        this.levantamento = levantamento;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContext();

        dataBaseDarwin = new DataBaseDarwin(getContext());
        delegateDarwin = new DelegateDarwin(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.wait);

        showLevantamento();
        showUser();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.user);
        setSupportActionBar(toolbar);

        createLev.setContext(getContext());
        editLevantamento.setContext(getContext());

        if(AndroidSettings.isExternalStorageWritable()){
            ManegerFiles manegerFiles = new ManegerFiles(helperManegerFile());
            manegerFiles.createDirDarwin();
            manegerFiles.createDirDarwinKml();

        }else{
            // MOSTRA MENSAGEM DE ERROR
            final LinearLayout displayWarn = findViewById(R.id.displayWarn);
            TextView title = findViewById(R.id.title);
            TextView menssage = findViewById(R.id.menssage);
            title.setText(R.string.noPermissionSd);
            menssage.setText(R.string.mnoPermissionSd);
            displayWarn.setVisibility(View.VISIBLE);
            displayWarn.setOnClickListener(v -> displayWarn.setVisibility(View.GONE));
        }

        contentTop = findViewById(R.id.contentTop);
        progressBar = findViewById(R.id.progressBar);
        displayTotal = findViewById(R.id.totalLev);
        notification = findViewById(R.id.notification);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        editLevantamento.setCancelable(false);
        createLev.setCancelable(false);

       FloatingActionButton fab = findViewById(R.id.fab);
       fab.setOnClickListener(view -> {
           if(blockFab != 0){
               createLev.setUpdateLevantamento(new AlertDialogCreateLev.UpdateLevantamento() {

                   @Override
                   public void updateLevantamento() {
                       showLevantamento();
                       createLev.dismiss();
                       blockFab = 1;
                   }

                   @Override
                   public void blockFab() {
                       blockFab = 1;
                   }
               });
               createLev.show(getSupportFragmentManager(), null);
           }
           blockFab = 0;
       });
    }

    private AdapterLev.OnClickListenerLev onClickList(){
        return new AdapterLev.OnClickListenerLev(){
            @Override
            public void onClickEdit(View view, int position) {
                Levantamento levantament = getLevantamento().get(position);

                if(blockFab != 0){
                    editLevantamento.setUpdateLevantamento(new AlertDialogEditLevantamento.UpdateLevantamento() {
                        @Override
                        public void updateLevantamento() {
                            showLevantamento();
                            editLevantamento.dismiss();
                            blockFab = 1;
                        }

                        @Override
                        public void blockFab() {
                            blockFab = 1;
                        }
                    });
                    editLevantamento.setLevantamento(levantament);
                    editLevantamento.show(getSupportFragmentManager(), null);
                }
                blockFab = 0;
            }

            @Override
            public void onClickDelete(View view, int position) {
                Levantamento levantament = getLevantamento().get(position);
                onCreateDialog(levantament);
            }

            @Override
            public void onClickItem(View view, int position) {
                Levantamento lev = getLevantamento().get(position);
                Intent intent = new Intent(getApplicationContext(), ActivityAmostras.class);
                intent.putExtra("lev", lev);
                startActivity(intent);
            }
        };
    }

    public void onCreateDialog(final Levantamento levantamento){
        final String s = levantamento.getLev_name();
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteLevantamento(levantamento);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.excluirLeva);//"Excluir Levantamento"
        builder.setMessage("O Levantamento " + s + " será apagado com suas amostras e espécies.");
        builder.setPositiveButton(R.string.excluir, dialogClickListener);
        builder.setNegativeButton(R.string.cancel, dialogClickListener);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user) {
            Intent intent = new Intent(getBaseContext(), ActivityUser.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Levantamento> list = new ArrayList<>();

        for (Levantamento levantamentos : getLevantamento()){
            String lev = levantamentos.getLev_name().toLowerCase();
            if (lev.contains(newText)){
                list.add(levantamentos);
            }
        }

        adapterLev.setFilter(list);
        return true;
    }

    public void showLevantamento(){
        AsyncGetLevant getLevant = new AsyncGetLevant(dataBaseDarwin, helperAsyncLevant());
        getLevant.execute();
    }

    private AsyncGetLevant.HelperAsync helperAsyncLevant(){
        return new AsyncGetLevant.HelperAsync() {
            @Override
            public void onHelperPreExecute() {

            }

            @Override
            public void onHelperPosExecute(List<Levantamento> levantamentos) {
                setLevantamento(levantamentos);
                setTotalLev(levantamentos.size());

                adapterLev = new AdapterLev(getLevantamento(), getBaseContext(), onClickList());
                String title = getTotalLev() + " " + getResources().getString(R.string.levantamento);//TITULO DA ACTIVITY
                displayTotal.setText(title);

                if (getTotalLev() != 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapterLev);
                    contentTop.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    notification.setVisibility(View.GONE);

                }else{
                    recyclerView.setVisibility(View.GONE);
                    notification.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
    }

    public void deleteLevantamento(Levantamento levantamento){
        AsyncDeleteLevant deleteLevant = new AsyncDeleteLevant(dataBaseDarwin, helperAsyncDelete());
        deleteLevant.execute(levantamento);
    }

    private AsyncDeleteLevant.HelperAsyncDelete helperAsyncDelete(){
        return new AsyncDeleteLevant.HelperAsyncDelete() {
            @Override
            public void onPreExecuteHelper() {
                progressDialog.setMessage(getResources().getString(R.string.excluindoLevantamento));//"Excluindo Levantamento ..."
                progressDialog.show();

            }

            @Override
            public void onPosExecuteHelper(boolean bool) {
                showLevantamento();
                progressDialog.dismiss();
                Toast.makeText(getContext(),R.string.levExcluido, Toast.LENGTH_SHORT).show();//"Levantamento excluído!"
            }
        };
    }

    public void showUser(){
        PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getContext());
        String user = preferencesDarwin.getUserAccount();
        AsyncGetUser asyncTaskUser = new AsyncGetUser(delegateDarwin, updateInterface());
        asyncTaskUser.execute(user);
    }

    private AsyncGetUser.UpdateInterface updateInterface(){
        return userDarwin -> toolbar.setTitle(userDarwin.getFirstName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showUser();
        showLevantamento();
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

    public void showEspecies(){
        DataBaseDarwin baseDarwin = new DataBaseDarwin(getBaseContext());
        List<Especie> e = baseDarwin.getAllEspecies();
        for (Especie especie : e){
            Log.d("ESPECIES", "ID_LEV: " + especie.getId_mask_lev() + " ID_AM: " + especie.getId_mask_am() + " NAME: " + especie.getEs_name());

        }
    }

    public void showGeoLev(){
        DataBaseDarwin baseDarwin = new DataBaseDarwin(getBaseContext());
        List<KmlPolygon> k = baseDarwin.getGeoLevAll();

        for (KmlPolygon kmlPolygon: k){
            Log.d("GEO_LEV", "ID: "+kmlPolygon.get_id() + " ID_LEV: " + kmlPolygon.getId_mask() + " COORD: " + kmlPolygon.getLat() + "," + kmlPolygon.getLon());
        }
    }
    //String idUser, String firstName, String lastName, String email, String country, String stated,
    //String city, String profession, String institution, String pass, Date date, Date dateEdit
    public void showUsers(){
        DelegateDarwin delegateDarwin = new DelegateDarwin(getContext());
        List<UserDarwin> list = delegateDarwin.getAllUsers();

        if (list != null) {
            for (UserDarwin user : list) {
                Log.d("USER_SUPER", "firstName:" + user.getFirstName() + " lastName:" + user.getLastName() + "" + " email:" + user.getEmail() + " pais:" + user.getCountry() + " estado:" + user.getStated() + " city: "+ user.getCity()+
                " profissão: " + user.getProfession() + " insti: " + user.getInstitution() + " pass:" + user.getPass()+ " dateStart: " +user.getDate() + " dateEnd" + user.getDateEdit());
            }

        }else{
            Toast.makeText(context, "Sem data!", Toast.LENGTH_SHORT).show();
        }
    }
}
