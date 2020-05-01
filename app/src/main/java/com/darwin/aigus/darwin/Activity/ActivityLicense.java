package com.darwin.aigus.darwin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityLicense extends AppCompatActivity {
    TextView result, font;
    float s = 100;
    List<Float> t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisenca);
        t = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Planos de Uso");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        result = findViewById(R.id.result);
        font = findViewById(R.id.font);
        Button bnt = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.btn2);

        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = s + 50;
                if (s <= 1000){
                    t.add(s);
                    result.setText(String.valueOf(s) + " m");

                }else{
                    s = 100;
                    result.setText(String.valueOf(s) + "m");
                    Toast.makeText(ActivityLicense.this, "Limite escedido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (float i: t){
                    Toast.makeText(ActivityLicense.this, "BUMEROS " + i, Toast.LENGTH_SHORT).show();
                }
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
}
