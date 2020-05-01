package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;

import java.util.List;

public class AsyncCreateAmostraWithKml extends AsyncTask<String, Void, Boolean> {
    private DataBaseDarwin dataBaseDarwin;
    private List<Amostra> amostraList;
    private HelperAsync helperAsync;

    public AsyncCreateAmostraWithKml(DataBaseDarwin dataBaseDarwin, List<Amostra> amostraList, HelperAsync helperAsync) {
        this.dataBaseDarwin = dataBaseDarwin;
        this.amostraList = amostraList;
        this.helperAsync = helperAsync;
    }

    public interface HelperAsync{
        void onPreExecuteHelper();
        void onPosExecuteHelper(boolean boo);
    }

    @Override
    protected void onPreExecute() {
        helperAsync.onPreExecuteHelper();
    }

    @Override
    protected Boolean doInBackground(String... amostra) {
        boolean result = false;
        if (amostraList.size() != 0){
            for (int i = 0; i < amostraList.size(); i++) {
                dataBaseDarwin.insertAmostra(amostraList.get(i));
                result = true;
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean boo) {
        helperAsync.onPosExecuteHelper(boo);
    }
}
