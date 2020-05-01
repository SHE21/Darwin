package com.darwin.aigus.darwin.AndroidUltils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class AndroidSettings {
    //VERIFICA OS STATUS DE CONEXÃO COM A INTERNET
    public static boolean statusConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    //VERIFICA SE ESTÁ AUTORIZADO A FAZER LEITURA E ESCRITA NA MEMORIA EXTERNA DO DISPOSITIVO
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
