package com.darwin.aigus.darwin.Async;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AsyncSandEmail extends AsyncTask<String, Void, Boolean> {
    private Session session;
    private HelperAsyn helperAsyn;

    public interface HelperAsyn{
        void onPreHelperExecute();
        void onPosHelperExecute(boolean bool);
    }

    public AsyncSandEmail(Session session, HelperAsyn helperAsyn) {
        this.session = session;
        this.helperAsyn = helperAsyn;
    }

    @Override
    protected void onPreExecute(){helperAsyn.onPreHelperExecute();}

        @Override
    protected Boolean doInBackground(String... strings) {
            boolean b = false;

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(strings[0]));//emailFrom
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(strings[1]));//emailTo
                message.setSubject(strings[2]);//assunto
                message.setContent(strings[3], "text/html; charset=utf-8");//conteudo da mensagem;
                Transport.send(message);
                b = true;

            } catch(Exception e) {
                Log.d("EMAIL", " ->" + e);

            }
            return b;
    }

    @Override
    protected void onPostExecute(Boolean bool){
        helperAsyn.onPosHelperExecute(bool);
    }
}
