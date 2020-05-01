package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncInsertArea extends AsyncTask<String, Void, Boolean> {
    private DataBaseDarwin dataBaseDarwin;
    private List<DarwinPolygon> polygonList;
    private String idRadom;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPosHelperExecute(boolean boo);
    }

    public AsyncInsertArea(DataBaseDarwin dataBaseDarwin, String idRadom, HelperAsync helperAsync, List<DarwinPolygon> polygonList) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.idRadom = idRadom;
        this.helperAsync = helperAsync;
        this.polygonList = polygonList;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onPreHelperExecute();
    }

    @Override
    protected Boolean doInBackground(String... lists) {
        boolean f = false;
        if (polygonList != null) {
            f = true;
            for (int d = 0; d < polygonList.size(); d++) {
                DarwinPolygon darwinPolygon1 = polygonList.get(d);
                darwinPolygon1.setId_mask(idRadom);
                dataBaseDarwin.insertGeoLev(darwinPolygon1);

            }
        }
        return f;
    }

    @Override
    protected void onPostExecute(Boolean boo){
        helperAsync.onPosHelperExecute(boo);
    }
}
