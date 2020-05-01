package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.AndroidUltils.DarwinStatistics;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.jjoe64.graphview.series.DataPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 08/03/2018.
 */

public class AsyncGetStatisticsEspeciesHeight extends AsyncTask<String, Void, DataPoint[]>{
    private List<Especie> especieList;
    private List<Amostra> amostraList;
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;

    public AsyncGetStatisticsEspeciesHeight(List<Especie> especieList, List<Amostra> amostraList, DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync) {
        this.especieList = especieList;
        this.amostraList = amostraList;
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPosHelperExecute(DataPoint[] dataPoints);
    }

    @Override
    protected DataPoint[] doInBackground(String... strings) {
        DarwinStatistics s = new DarwinStatistics();
        int tA = amostraList.size();
        DataPoint[] dataPoints = new DataPoint[tA];

        try {

            for (int i =0; i < tA; i++) {
                List<Especie> especie = dataBaseDarwin.getEspecies(amostraList.get(i).getId_mask());
                dataPoints[i] = new DataPoint(i, s.getMedia(s.getHeightArray(especie)));

            }

        }catch (ArrayIndexOutOfBoundsException e){

        }
        return dataPoints;
    }

    @Override
    protected void onPostExecute(DataPoint[] result){

    }
}
