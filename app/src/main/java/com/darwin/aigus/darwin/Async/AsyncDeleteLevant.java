package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

public class AsyncDeleteLevant extends AsyncTask<Levantamento, Void, Boolean> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncDelete helperAsyncDelete;

    public interface HelperAsyncDelete{
        void onPreExecuteHelper();
        void onPosExecuteHelper(boolean bool);
    }

    public AsyncDeleteLevant(DataBaseDarwin dataBaseDarwin, HelperAsyncDelete helperAsyncDelete) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsyncDelete = helperAsyncDelete;
    }

    @Override
    protected void onPreExecute(){
        helperAsyncDelete.onPreExecuteHelper();
    }

    @Override
    protected Boolean doInBackground(Levantamento... levantamentos) {
        Amostra amostra = new Amostra();
        amostra.setId_mask_lev(levantamentos[0].getLev_id_mask());

        dataBaseDarwin.deleteEspecieByAmostra(amostra);
        dataBaseDarwin.deleteAmostra(amostra);
        dataBaseDarwin.deleteGeodata(levantamentos[0]);
        dataBaseDarwin.deleteLevById(levantamentos[0]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean bool){
        helperAsyncDelete.onPosExecuteHelper(bool);
    }
}
