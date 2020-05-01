package com.darwin.aigus.darwin.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

/**
 * Created by SANTIAGO on 05/02/2018.
 */

public class AsyncTaskDeleteGeoLev extends AsyncTask<String, Void, Boolean> {
    private ProgressDialog progressDialog;
    private Context context;
    private String id_mask;

    public AsyncTaskDeleteGeoLev(Context context, String id_mask) {
        this.context = context;
        this.id_mask = id_mask;
    }

    @Override
    protected void onPreExecute(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Aguarde");
        progressDialog.setMessage("Excluindo Dados Geográficos ...");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        DataBaseDarwin db = new DataBaseDarwin(context);
        int i = db.deleteGeoLev(id_mask);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b){
        Toast.makeText(context, "Dados geográficos excluídos!", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}
