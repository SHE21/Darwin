package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncGetPointAmostra extends AsyncTask<String, Void, List<Amostra>> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncTask helperAsyncTask;

    public AsyncGetPointAmostra(DataBaseDarwin dataBaseDarwin, HelperAsyncTask helperAsyncTask) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsyncTask = helperAsyncTask;
    }

    public interface HelperAsyncTask{
        void onPreExeculte();
        void onPostExecute(List<Amostra> amostrasList);
    }

    @Override
    protected void onPreExecute(){
        helperAsyncTask.onPreExeculte();

    }

    @Override
    protected List<Amostra> doInBackground(String... strings) {
        return dataBaseDarwin.getAmostras(strings[0]);
    }

    @Override
    protected void onPostExecute(List<Amostra> amostraList){
        helperAsyncTask.onPostExecute(amostraList);

    }
}
