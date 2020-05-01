package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncGetAmostras extends AsyncTask<String, Void, List<Amostra>> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncGetAmostras helperAsyncGetAmostras;

    public interface HelperAsyncGetAmostras{
        void onPreHelperExecute();
        void onPosHelperExecute(List<Amostra> amostraList);
    }

    public AsyncGetAmostras(DataBaseDarwin dataBaseDarwin, HelperAsyncGetAmostras helperAsyncGetAmostras) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsyncGetAmostras = helperAsyncGetAmostras;
    }

    @Override
    protected void onPreExecute(){
        helperAsyncGetAmostras.onPreHelperExecute();
    }

    @Override
    protected List<Amostra> doInBackground(String... strings) {
        return dataBaseDarwin.getAmostras(strings[0]);
    }

    @Override
    protected void onPostExecute(List<Amostra> amostras){
        helperAsyncGetAmostras.onPosHelperExecute(amostras);
    }
}
