package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

public class AsyncCreateLevant extends AsyncTask<Levantamento, Void, Long>{
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncTask helperAsyncTask;

    public interface HelperAsyncTask{
        void onPreHelperExecute();
        void onPosHelperExecute(long result);
    }

    public AsyncCreateLevant(DataBaseDarwin dataBaseDarwin, HelperAsyncTask helperAsyncTask) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsyncTask = helperAsyncTask;
    }

    @Override
    protected void onPreExecute(){
        helperAsyncTask.onPreHelperExecute();
    }

    @Override
    protected Long doInBackground(Levantamento... levantamentos) {
        return dataBaseDarwin.insertLevantamento(levantamentos[0]);
    }

    @Override
    protected void onPostExecute(Long result){
        helperAsyncTask.onPosHelperExecute(result);
    }
}
