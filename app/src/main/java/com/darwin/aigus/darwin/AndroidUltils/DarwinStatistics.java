package com.darwin.aigus.darwin.AndroidUltils;

import com.darwin.aigus.darwin.Modelos.Especie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 08/03/2018.
 */

public class DarwinStatistics {
    public static final int HEIGHT = 1;
    public static final int DIAMETER = 2;

    public DarwinStatistics() {
    }

    public double[] getHeightArray(List<Especie> especieList){
        int t = especieList.size();
        double[] listheight = new double[t];

        for(int i =0; i < t; i++){
            listheight[i] = especieList.get(i).getEs_height();
        }

        return listheight;
    }

    public double[] getDiameterArray(List<Especie> listAmostra){
        int t = listAmostra.size();
        double[] listDiameter = new double[t];

        for(int i =0; i < t; i++){
            listDiameter[i] = listAmostra.get(i).getEs_diameter();
        }

        return listDiameter;
    }

    public double getMedia(double[] data){
        double soma = 0;
        int total = data.length;

        for (double aData : data) {
            soma = soma + aData;
        }

        //BigDecimal bd = new BigDecimal(soma/total).setScale(3, RoundingMode.HALF_EVEN);
        return soma/total;
    }

    //VARIANCIA
    public double getVariance(double[] data, double media){
        int t = data.length;
        double[] d = new double[t];

        for(int i = 0; i < t; i++){
            double o  = data[i]-media;
            d[i] = o*o;
        }

        return getMedia(d);
    }
    //DEVIO PADRÃO
    public double getDesvio(double variance){
        //double d = Math.sqrt(variance);

        //BigDecimal bd = new BigDecimal(d).setScale(3, RoundingMode.HALF_EVEN);
        return Math.sqrt(variance);
    }
}
