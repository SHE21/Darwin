package com.darwin.aigus.darwin.AndroidUltils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadTxt{
    private String fileName;
    private List<String> line;
    private Context context;

    public ReadTxt(String fileName, Context context) {
        this.fileName = fileName;
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    public Context getContext() {
        return context;
    }

    public List<String> getListFromFile(){
        List<String> line = new ArrayList<>();

        try{
            InputStream inputStream = getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String string;

            while ((string = bufferedReader.readLine())!= null){
                line.add(string);
            }

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            line = null;
        }

        return line;
    }

    public List<String> getListBiomasFromFile(){
        List<String> line = new ArrayList<>();
        ManegerFiles manegerFiles = new ManegerFiles(manegerFile());
        File file = manegerFiles.getDirSDCard("Biomas", getFileName());
        if (file != null) {

            try {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String string;

                while ((string = bufferedReader.readLine()) != null) {
                    line.add(string);
                }

                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                line = null;
            }

        }else{
            line = null;
        }

        return line;
    }

    public String[] convertArray(List<String> string){
        String[] s = new String[string.size()];

        for(int i = 0; i < string.size(); i++){
            s[i] = string.get(i);
        }
        return s;
    }

    public boolean inserNewLine(String newLine) throws IOException{
        ManegerFiles manegerFiles =  new ManegerFiles(manegerFile());
        File file = manegerFiles.getDirDarwin();
        boolean insert;
        try{
        FileWriter fileWrite = new FileWriter(file  + File.separator + getFileName(), true);
            try (BufferedWriter arquivo = new BufferedWriter(fileWrite)) {
                arquivo.write(newLine);
                arquivo.newLine();
                arquivo.close();
            }
            insert = true;

        }catch (IOException e) {
            Log.v("ERRO BIOMAS: ",  "" + e);
            insert = false;
        }
        return insert;
    }
    //LISTA DE FAMILIA EM LIST
    public List<String> getFamilyFromFile(){
        List<String> line = new ArrayList<>();

        try{
            AssetManager assetManager = getContext().getResources().getAssets();
            InputStream inputStream = assetManager.open(getFileName());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String string;

            while ((string = bufferedReader.readLine())!= null){
                line.add(string);
            }

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            line = null;
        }

        return line;
    }
    //LISTA DE FAMILIA EM ARRAY
    public String[] convertToArray(List<String> string){
        int t = string.size();
        String[] s = new String[t];

        for(int i = 0; i < string.size(); i++){
            s[i] = string.get(i);

        }
        return s;
    }

    public List<String> getEspecieFromFile(){
        List<String> line = new ArrayList<>();
        try{
            AssetManager assetManager = getContext().getResources().getAssets();
            InputStream inputStream = assetManager.open(getFileName());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String string;

            while ((string = bufferedReader.readLine())!= null){
                String[] s = string.split("-");
                line.add(s[1]);
            }

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            line = null;
        }
        return line;
    }

    public InputStream getInputStream(){
        AssetManager assetManager = getContext().getResources().getAssets();
        InputStream input;
        try {
            input = assetManager.open(getFileName());
        } catch (IOException e) {
            input = null;
        }

        return input;
    }

    public boolean getBooleanFile(){
        boolean b;

        try {
            AssetManager assetManager = getContext().getResources().getAssets();
            InputStream input = assetManager.open(getFileName());
            b = true;
        } catch (IOException e) {
            b = false;
        }

        return b;
    }

    private ManegerFiles.HelperManegerFile manegerFile(){
        return new ManegerFiles.HelperManegerFile() {
            @Override
            public void dirNotFound() {

            }

            @Override
            public void getPermission() {

            }

            @Override
            public void createFile() {

            }
        };
    }
}
