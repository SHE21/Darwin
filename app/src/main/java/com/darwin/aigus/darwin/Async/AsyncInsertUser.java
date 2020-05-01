package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;

import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

public class AsyncInsertUser extends AsyncTask<UserDarwin,Void, Long> {
    private UserInsertSucesse insertSucesse;
    private DelegateDarwin delegateDarwin;

    public AsyncInsertUser(UserInsertSucesse insertSucesse, DelegateDarwin delegateDarwin) {
        this.insertSucesse = insertSucesse;
        this.delegateDarwin = delegateDarwin;
    }

    public interface UserInsertSucesse{
        void userInsertSucesse(Long result);
    }

    @Override
    protected Long doInBackground(UserDarwin... user) {
        return delegateDarwin.insertUser(user[0]);
    }

    @Override
    protected void onPostExecute(Long result){
        insertSucesse.userInsertSucesse(result);
    }
}
