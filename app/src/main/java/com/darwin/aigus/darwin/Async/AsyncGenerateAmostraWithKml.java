package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.AndroidUltils.DateDarwin;
import com.darwin.aigus.darwin.AndroidUltils.GenerateId;
import com.darwin.aigus.darwin.Geometry.DarwinPoint;
import com.darwin.aigus.darwin.Geometry.ReaderKml;
import com.darwin.aigus.darwin.Modelos.Amostra;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AsyncGenerateAmostraWithKml extends AsyncTask<String, Void, List<Amostra>> {
    private File fileDir;
    private HelperAsync helperAsync;

    public AsyncGenerateAmostraWithKml(File fileDir, HelperAsync helperAsync) {
        this.fileDir = fileDir;
        this.helperAsync = helperAsync;
    }

    public interface HelperAsync{
        void onPreExecuteHelper();
        void onPosExecuteHelper(List<Amostra> amostraList);
    }

    @Override
    protected void onPreExecute() {
        helperAsync.onPreExecuteHelper();
    }

    @Override
    protected List<Amostra> doInBackground(String... strings) {
        List<Amostra> amostraList = new ArrayList<>();
        ReaderKml readerKml = new ReaderKml(fileDir);
        List<DarwinPoint> pointList = readerKml.getPoint();
        String data = DateDarwin.getDate();

        if (pointList != null){
            for (int i = 0; i < pointList.size(); i++) {
                Double lon = pointList.get(i).getLon();
                Double lat = pointList.get(i).getLat();

                Amostra amostra = new Amostra(i, strings[0] + "_" + i, lat, lon, data, strings[1], GenerateId.generateId());
                amostraList.add(amostra);
            }
        }
        return amostraList;
    }

    @Override
    protected void onPostExecute(List<Amostra> amostras) {
        helperAsync.onPosExecuteHelper(amostras);

    }
}
