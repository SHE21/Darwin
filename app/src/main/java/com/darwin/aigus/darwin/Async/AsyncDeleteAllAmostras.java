package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

public class AsyncDeleteAllAmostras extends AsyncTask<Levantamento, Void, Boolean> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPosHelperExecute(boolean boo);
    }

    public AsyncDeleteAllAmostras(DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onPreHelperExecute();

    }

    @Override
    protected Boolean doInBackground(Levantamento... levantamentos) {
        int a = dataBaseDarwin.deleteAllAmostra(levantamentos[0]);
        int b = dataBaseDarwin.deleteEspecieByLev(levantamentos[0].getLev_id_mask());
        return a != 0 || b != 0;
    }

    @Override
    protected void onPostExecute(Boolean result){
        helperAsync.onPosHelperExecute(result);
    }
}
