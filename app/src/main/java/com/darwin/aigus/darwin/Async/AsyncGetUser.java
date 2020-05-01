package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

public class AsyncGetUser extends AsyncTask<String, Void, UserDarwin> {
    private DelegateDarwin delegateDarwin;
    private UpdateInterface updateInterface;

    public interface UpdateInterface{
        void updateInterface(UserDarwin userDarwin);
    }

    public AsyncGetUser(DelegateDarwin delegateDarwin, UpdateInterface updateInterface) {
        this.delegateDarwin = delegateDarwin;
        this.updateInterface = updateInterface;
    }

    @Override
    protected UserDarwin doInBackground(String... idUser) {
        return delegateDarwin.getUserById(idUser[0]);
    }

    @Override
    protected void onPostExecute(UserDarwin userDarwin){
        updateInterface.updateInterface(userDarwin);
    }
}
