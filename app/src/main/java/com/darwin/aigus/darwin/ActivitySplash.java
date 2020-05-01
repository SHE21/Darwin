package com.darwin.aigus.darwin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.getIndeterminateDrawable()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(() -> {
            PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getBaseContext());
            if (preferencesDarwin.getStatusLogUser()){
                Intent intent = new Intent(getBaseContext(), ActivityHome.class);
                startActivity(intent);

            }else{
                Intent intent = new Intent(getBaseContext(), ActivityLogin.class);
                startActivity(intent);
            }

            finish();
        }, 1000);
    }
}
