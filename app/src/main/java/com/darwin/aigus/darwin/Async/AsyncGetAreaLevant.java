package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.AndroidUltils.KmlPolygon;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncGetAreaLevant extends AsyncTask<String, Void, List<KmlPolygon>> {
    private DataBaseDarwin dataBaseDarwin;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onHelperPreExecute();
        void onHelperPosExecute(List<KmlPolygon> KmlPolygon);
    }

    public AsyncGetAreaLevant(DataBaseDarwin dataBaseDarwin, HelperAsync helperAsync) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.helperAsync = helperAsync;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onHelperPreExecute();
    }

    @Override
    protected List<KmlPolygon> doInBackground(String... strings) {
        return dataBaseDarwin.getGeoLev(strings[0]);
    }

    @Override
    protected void onPostExecute(List<KmlPolygon>  kmlPolygonList) {
        helperAsync.onHelperPosExecute(kmlPolygonList);
    }
}
