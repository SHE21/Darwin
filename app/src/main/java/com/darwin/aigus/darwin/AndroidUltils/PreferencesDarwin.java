package com.darwin.aigus.darwin.AndroidUltils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesDarwin{
    private Context context;
    private final String LOG_DARWIN = "log_darwin";
    private final String ACCOUNT_DARWIN = "account_darwin";
    private final String NOTIFY_DARWIN = "notify_darwin";
    private final String LICENCA_DARWIN = "licenca_darwin";

    private SharedPreferences getPreferences() {
        final String PREFER_DARWIN = "prefer_darwin";
        return context.getSharedPreferences(PREFER_DARWIN, Context.MODE_PRIVATE);
    }

    public PreferencesDarwin(Context context) {
        this.context = context;
    }
    //RETORNA STATUS DO LOG
    public boolean getStatusLogUser(){
        return getUserLog();
    }

    //GERENCIADOR DE LOG// retorna TRUE se as modificações foram  feitas
    public boolean managerUserLog(){
        if (getUserLog()){
            return setUserLogFalse();
        }else{
            return setUserLogTrue();
        }
    }

    private boolean setUserLogTrue(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(LOG_DARWIN, true);
        return editor.commit();
    }

    private boolean setUserLogFalse(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(LOG_DARWIN, false);
        return editor.commit();
    }

    private boolean getUserLog(){
        return getPreferences().getBoolean(LOG_DARWIN, false);
    }

    public boolean getStatusNotify(){
        return getUserNotify();
    }
    //gerencia as notificações
    public boolean menagerUserNotify(){
        if(getUserNotify()){
            return setUserNotifyFalse();
        }else{
            return setUserNotifyTrue();
        }
    }
    //ativa notificações
    private boolean setUserNotifyTrue(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(NOTIFY_DARWIN, true);
        return editor.commit();
    }
    //desativa notificações
    private boolean setUserNotifyFalse(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(NOTIFY_DARWIN, false);
        return editor.commit();
    }
    //pega o status da notificação
    private boolean getUserNotify(){
        return getPreferences().getBoolean(NOTIFY_DARWIN, false);
    }

    public boolean setUserAccount(String idUserMask){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(ACCOUNT_DARWIN, idUserMask);
        return editor.commit();
    }

    public String getUserAccount(){
        return getPreferences().getString(ACCOUNT_DARWIN, null);
    }
}
