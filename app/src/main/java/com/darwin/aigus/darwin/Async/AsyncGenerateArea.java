package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.Geometry.ReaderKml;

import java.io.File;
import java.util.List;

public class AsyncGenerateArea extends AsyncTask<Void, Void, List<DarwinPolygon>> {
    private File dirFile;
    private HelperAsync helperAsync;

    public interface HelperAsync{
        void onPreHelperExecute();
        void onPostHelperExecute(List<DarwinPolygon> DarwinPolygons);
    }

    public AsyncGenerateArea(File dirFile, HelperAsync helperAsync) {
        this.dirFile = dirFile;
        this.helperAsync = helperAsync;
    }

    @Override
    protected void onPreExecute(){
        helperAsync.onPreHelperExecute();
    }

    @Override
    protected List<DarwinPolygon> doInBackground(Void... voids) {
        List<DarwinPolygon> darwinPolygons = null;
        ReaderKml readerKmlPoly = new ReaderKml(dirFile);
        String stringPoly = readerKmlPoly.getPolygonString();

        if (stringPoly != null){
            darwinPolygons = DarwinPolygon.getLatLong(stringPoly);
        }
        return darwinPolygons;
    }

    @Override
    protected void onPostExecute(List<DarwinPolygon> darwinPolygons){
        helperAsync.onPostHelperExecute(darwinPolygons);
    }
}
