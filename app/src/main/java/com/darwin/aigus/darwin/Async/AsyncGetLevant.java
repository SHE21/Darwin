package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncGetLevant extends AsyncTask<String, Void, List<Levantamento>> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onHelperPreExecute();
        void onHelperPosExecute(List<Levantamento> levantamentos);
    }

    public AsyncGetLevant(DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onHelperPreExecute();

    }

    @Override
    protected List<Levantamento> doInBackground(String... strings) {
        return dataBaseDarwin.getAllLevantamento();
    }

    @Override
    protected void onPostExecute(List<Levantamento> levantamento){
        helperAsync.onHelperPosExecute(levantamento);
    }
}
