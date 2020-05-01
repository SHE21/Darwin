package com.darwin.aigus.darwin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DataBaseDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

import java.text.ParseException;

public class ActivityLogin extends AppCompatActivity {
    private EditText nameUser, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getBaseContext());
        if(preferencesDarwin.getStatusLogUser()){
            Intent intent = new Intent(getBaseContext(), ActivityHome.class);
            startActivity(intent);
            finish();
        }

        Button btnNewAccount = findViewById(R.id.btnNewAccount);
        Button btnLogar = findViewById(R.id.btnLogar);
        Button btnForgotPass = findViewById(R.id.btnForgotPass);

        nameUser = findViewById(R.id.nameUser);
        password = findViewById(R.id.password);

        DelegateDarwin dl = new DelegateDarwin(getBaseContext());

        if(dl.selectUserCount() != 0){
            btnNewAccount.setVisibility(View.GONE);
            btnForgotPass.setVisibility(View.VISIBLE);
        }

        btnForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActivityForgotPass.class);
            startActivity(intent);
        });

        btnNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ActivityNewAccount.class);
            startActivity(intent);
        });

        btnLogar.setOnClickListener(v -> {
            String nomeUser = nameUser.getText().toString();
            String senha = password.getText().toString();

            DelegateDarwin delegateDarwin = new DelegateDarwin(getBaseContext());
            PreferencesDarwin preferencesDarwin1 = new PreferencesDarwin(getBaseContext());

            if (!nomeUser.isEmpty() || !senha.isEmpty()){
                if (delegateDarwin.getUserCountOne(nomeUser, senha) != 0){
                    UserDarwin user;
                    try {
                        user = delegateDarwin.getUserToLog(nomeUser, senha);
                        preferencesDarwin1.setUserAccount(user.getId_user());
                        Intent intent = new Intent(getBaseContext(), ActivityHome.class);
                        startActivity(intent);
                        finish();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(ActivityLogin.this, R.string.errorPass, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ActivityLogin.this, R.string.errorCampoBranco, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
