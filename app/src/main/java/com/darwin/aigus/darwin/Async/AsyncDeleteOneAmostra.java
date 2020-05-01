package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

/**
 * Created by SANTIAGO on 05/02/2018.
 */

public class AsyncDeleteOneAmostra extends AsyncTask<Amostra, String, Boolean> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncTask helperAsyncTask;

    public interface HelperAsyncTask{
        void onUpdate();
        void onPreExecute();
        void onPostExecute();
    }

    public AsyncDeleteOneAmostra(DataBaseDarwin dataBaseDarwin, HelperAsyncTask helperAsyncTask) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsyncTask = helperAsyncTask;
    }

    @Override
    protected void onPreExecute(){
        helperAsyncTask.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(Amostra... amostra) {
        dataBaseDarwin.deleteAmostra(amostra[0]);
        dataBaseDarwin.deleteEspecieByAmostra(amostra[0]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result){
        helperAsyncTask.onUpdate();
        helperAsyncTask.onPostExecute();
    }
}
