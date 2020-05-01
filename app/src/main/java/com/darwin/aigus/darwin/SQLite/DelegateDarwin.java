package com.darwin.aigus.darwin.SQLite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.darwin.aigus.darwin.Modelos.UserDarwin;

import java.text.ParseException;
import java.util.List;

public class DelegateDarwin{
    private Context context;

    public DelegateDarwin(Context context) {
        this.context = context;
    }

    @NonNull
    private DataBaseSuperDarwin getDb() {
        return new DataBaseSuperDarwin(getContext());
    }

    //METODO INSERT USER
    public long insertUser(UserDarwin userDarwin){
        return getDb().insertIntermediateUser(userDarwin);
    }

    //METODO GET ALL USERS
    public List<UserDarwin> getAllUsers(){
        List<UserDarwin> users = null;

        try {
            users = getDb().getAllUsers();

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("DELEGATE:","RESULT: " + e);
        }
        return users;
    }

    public UserDarwin getUserById(String idUser){
        return getDb().getUserById(idUser);
    }

    public UserDarwin getUserToLog(String userName, String pass) throws ParseException {
        return getDb().getUserToLog(userName, pass);
    }

    public int getUserCountOne(String userName, String pass){
        return getDb().selectUserCountOne(userName, pass);
    }

    public int selectUserCount(){
        return getDb().selectUserCount();
    }

    public UserDarwin getUserByEmail(String email){
        return getDb().getUserByEmail(email);
    }

    public int updateUser(UserDarwin user){
        return getDb().updateUser(user);
    }

    private Context getContext() {
        return context;
    }
}
