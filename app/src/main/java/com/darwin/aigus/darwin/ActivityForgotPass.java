package com.darwin.aigus.darwin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darwin.aigus.darwin.AndroidUltils.ValidateEmail;
import com.darwin.aigus.darwin.Async.AsyncSandEmail;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class ActivityForgotPass extends AppCompatActivity {
    private EditText fildEmail;
    private TextView alertError, titleMensage, menssageAlert;
    private LinearLayout contMenssage;
    private ProgressBar progressBar;

    private static final String EMAIL_FROM = "santiagolenil@gmail.com";
    private static final String SENHA_EMAIL = "shekespeare";
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.revogarAcesso);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fildEmail = findViewById(R.id.fildEmail);
        Button btnSand = findViewById(R.id.btnSand);
        alertError = findViewById(R.id.alertError);
        menssageAlert = findViewById(R.id.menssageAlert);
        titleMensage = findViewById(R.id.titleMensage);
        contMenssage = findViewById(R.id.contMenssage);
        progressBar = findViewById(R.id.progress);

        btnSand.setOnClickListener(onClickListener());
        alertError.setOnClickListener(v -> alertError.setVisibility(View.INVISIBLE));

    }

    private View.OnClickListener onClickListener(){
        return v -> {
            alertError.setVisibility(View.INVISIBLE);
            String emailTo = fildEmail.getText().toString();

            if (!emailTo.isEmpty()){

                if (ValidateEmail.validateEmail(emailTo)) {
                    String nomeDeUsuario = getResources().getString(R.string.NomeUser);
                    String senha = getResources().getString(R.string.pass);
                    String noReplay = getResources().getString(R.string.noReplay);

                    DelegateDarwin delegateDarwin = new DelegateDarwin(getBaseContext());
                    UserDarwin userDarwin = delegateDarwin.getUserByEmail(emailTo);
                    if (userDarwin != null) {

                        String contentEmail = "<b>"+nomeDeUsuario+":</b> " + userDarwin.getFirstName() + "<br><b>"+senha+":</b>" + userDarwin.getPass() +"<br>" + noReplay;
                        String ASSUNTO = getResources().getString(R.string.assuntoEmailToUser);

                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.socketFactory.port", "465");
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");

                        session = Session.getDefaultInstance(props, new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(EMAIL_FROM, SENHA_EMAIL);
                            }
                        });

                        AsyncSandEmail sandEmail = new AsyncSandEmail(session, helperAsyn());
                        sandEmail.execute(EMAIL_FROM, emailTo, ASSUNTO, contentEmail);

                    } else {
                        alertError.setVisibility(View.VISIBLE);
                        alertError.setText(R.string.emailNotFound);
                    }

                }else{
                    alertError.setVisibility(View.VISIBLE);
                    alertError.setText(R.string.errorEmail);
                }

            }else{
                alertError.setVisibility(View.VISIBLE);
                alertError.setText(R.string.errorCampoBranco);
            }
        };
    }

    private AsyncSandEmail.HelperAsyn helperAsyn(){
        return new AsyncSandEmail.HelperAsyn() {
            @Override
            public void onPreHelperExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPosHelperExecute(boolean bool) {
                contMenssage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                if (bool){
                    titleMensage.setTextColor(getResources().getColor(R.color.colorPrimary));
                    titleMensage.setText(R.string.sucesso);
                    menssageAlert.setText(R.string.mSucesseSandEmail);
                    fildEmail.setText("");

                }else{
                    titleMensage.setTextColor(getResources().getColor(R.color.colorError));
                    titleMensage.setText(R.string.error);
                    menssageAlert.setText(R.string.erroToSandEmail);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
               finish();
               return true;
        }

        return true;
    }
}
