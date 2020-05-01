package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.AndroidUltils.DarwinStatistics;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 08/03/2018.
 */

public class AsyncGetStatisticEspecie extends AsyncTask<Integer, Void, List<double[]>>{
    private List<Amostra> amostraList;
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsyncStatisticEspecie helperAsync;

    public AsyncGetStatisticEspecie(List<Amostra> amostraList, DataBaseDarwin dataBaseDarwin, HelperAsyncStatisticEspecie helperAsync) {
        this.amostraList = amostraList;
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    public interface HelperAsyncStatisticEspecie{
        void onPreHelperExecute();
        void onPosHelperExecute(List<double[]> listData);
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onPreHelperExecute();
    }

    @Override
    protected List<double[]> doInBackground(Integer... property) {
        DarwinStatistics s = new DarwinStatistics();
        int tA = amostraList.size();
        List<double[]> a = new ArrayList<>();

        try {

            if(DarwinStatistics.HEIGHT == property[0]) {

                for (int i = 0; i < tA; i++) {
                    List<Especie> especie = dataBaseDarwin.getEspecies(amostraList.get(i).getId_mask());
                    double media = s.getMedia(s.getHeightArray(especie));
                    double variance = s.getVariance(s.getHeightArray(especie), media);
                    double desvio = s.getDesvio(variance);

                    a.add(new double[]{media, variance, desvio});

                }
            }

            if(DarwinStatistics.DIAMETER == property[0]) {

                for (int i = 0; i < tA; i++) {
                    List<Especie> especie = dataBaseDarwin.getEspecies(amostraList.get(i).getId_mask());
                    double media = s.getMedia(s.getDiameterArray(especie));
                    double variance = s.getVariance(s.getDiameterArray(especie), media);
                    double desvio = s.getDesvio(variance);

                    a.add(new double[]{media, variance, desvio});

                }
            }

        }catch (ArrayIndexOutOfBoundsException e){
            a = null;

        }
        return a;
    }

    @Override
    protected void onPostExecute(List<double[]> result){
        helperAsync.onPosHelperExecute(result);

    }
}
