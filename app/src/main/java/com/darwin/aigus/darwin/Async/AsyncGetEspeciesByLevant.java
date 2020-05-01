package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncGetEspeciesByLevant extends AsyncTask<String, Void, List<Especie>> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPosHelperExecute(List<Especie> especieList);
    }

    public AsyncGetEspeciesByLevant(DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onPreHelperExecute();
    }

    @Override
    protected List<Especie> doInBackground(String... strings) {
        return dataBaseDarwin.getEspeciesByLevant(strings[0]);
    }

    @Override
    protected void onPostExecute(List<Especie> especies){
        helperAsync.onPosHelperExecute(especies);
    }
}
