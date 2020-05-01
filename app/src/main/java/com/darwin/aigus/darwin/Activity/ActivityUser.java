package com.darwin.aigus.darwin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;
import com.darwin.aigus.darwin.Async.AsyncGetUser;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

public class ActivityUser extends AppCompatActivity {
    private TextView userName, userEmail;
    private PreferencesDarwin preferencesDarwin;

    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showUser();

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        Button btnAtivar = findViewById(R.id.btnAtive);
        ImageButton btnEdiUser = findViewById(R.id.btnEditUser);

        btnAtivar.setOnClickListener(onClickListenerAtive());
        btnEdiUser.setOnClickListener(onClickListenerEditUser());

        Switch log = findViewById(R.id.switch0);
        log.setChecked(preferencesDarwin.getStatusLogUser());
        log.setOnClickListener(v -> {
            String m;
            boolean state = preferencesDarwin.managerUserLog();
            boolean b = preferencesDarwin.getStatusLogUser();
            if (b) {
                m = getBaseContext().getString(R.string.ativado);
            } else {
                m = getBaseContext().getString(R.string.desativado);
            }
            if (state) {
                Toast.makeText(getBaseContext(), "Permanecer logado " + m, Toast.LENGTH_SHORT).show();
            }
        });


        Switch switch2 = findViewById(R.id.switch2);
        switch2.setChecked(preferencesDarwin.getStatusNotify());
        switch2.setOnClickListener(v -> {
            String m;
            boolean state = preferencesDarwin.menagerUserNotify();
            boolean b = preferencesDarwin.getStatusNotify();
            if (b) {m = getBaseContext().getString(R.string.ativado);}else{ m = getBaseContext().getString(R.string.desativado);}
            if (state){
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.notify)+" " + m + "!", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void showUser(){
        preferencesDarwin = new PreferencesDarwin(getBaseContext());
        String user = preferencesDarwin.getUserAccount();
        AsyncGetUser asyncGetUser = new AsyncGetUser(new DelegateDarwin(getBaseContext()), userDarwin -> {
            String nome = userDarwin.getFirstName() + " " + userDarwin.getLastName();
            userName.setText(nome);
            userEmail.setText(userDarwin.getEmail());
        });
        asyncGetUser.execute(user);
    }

    public View.OnClickListener onClickListenerAtive(){return v -> {
        Intent intent = new Intent(getBaseContext(), ActivityLicense.class);
        startActivity(intent);
    };}

    public View.OnClickListener onClickListenerEditUser(){return v -> {
        Intent intent = new Intent(getBaseContext(), ActivityEditUser.class);
        startActivity(intent);
    };}

    @Override
    protected void onRestart() {
        super.onRestart();
        showUser();
    }
}
