package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 07/03/2018.
 */

public class AsyncCountEspecieByAmostra extends AsyncTask<String, Void, DataPoint[]> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;
    private List<Amostra> listAmostra;

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPosHelperExecute(DataPoint[] dataPoint);
    }

    public AsyncCountEspecieByAmostra(DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync, List<Amostra> listAmostra) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
        this.listAmostra = listAmostra;
    }

    @Override
    protected DataPoint[] doInBackground(String... strings) {
        int tA = listAmostra.size();
        DataPoint[] dataPoints = new DataPoint[tA];

        try {
            for (int i = 0; i < tA; i++) {
                int tE = dataBaseDarwin.countEspecie(listAmostra.get(i).getId_mask());
                dataPoints[i] = new DataPoint(i, tE);
            }

        }catch (ArrayIndexOutOfBoundsException e){
            Log.v("GRAF", "" + e);
        }
        return dataPoints;
    }

    @Override
    protected void onPostExecute(DataPoint[] result){
        helperAsync.onPosHelperExecute(result);

    }
}
