package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.AndroidUltils.ManegerFiles;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class AsyncCreateFileFromEspecies extends AsyncTask<String, Void, Boolean> {
    private ManegerFiles manegerFiles;
    private HelperAsyncFile helperAsyncFile;
    private Levantamento levantamento;
    private Amostra amostra;
    private List<Especie> especies;

    public AsyncCreateFileFromEspecies(ManegerFiles manegerFiles, HelperAsyncFile helperAsyncFile, Levantamento levantamento, Amostra amostra, List<Especie> especies) {
        this.manegerFiles = manegerFiles;
        this.helperAsyncFile = helperAsyncFile;
        this.levantamento = levantamento;
        this.amostra = amostra;
        this.especies = especies;
    }

    public interface HelperAsyncFile{
        void onPreHelperExecute();
        void onPosHelperExecute(boolean boo);
    }

    @Override
    protected void onPreExecute(){
        helperAsyncFile.onPreHelperExecute();
    }

    @Override
    protected Boolean doInBackground(String... files) {
        boolean status = false;
        byte[] bytes = manegerFiles.getBaytesEspecies(levantamento, amostra, especies);
        try {
            status = manegerFiles.createFileEspecies(bytes, files[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    protected void onPostExecute(Boolean bool){
        helperAsyncFile.onPosHelperExecute(bool);
    }
}
