package com.darwin.aigus.darwin.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.R;

public class ActivityDetalheEspecie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_especie);

        Especie especie = (Especie)getIntent().getSerializableExtra("esp");
        Amostra amostra = (Amostra)getIntent().getSerializableExtra("amost");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(especie.getEs_name());

        TextView nameEspecie = findViewById(R.id.nameEspecie);
        TextView nameCientifico = findViewById(R.id.nameCientifico);
        TextView familiaEspecie = findViewById(R.id.familiaEspecie);
        TextView heightEspecie = findViewById(R.id.heightEspecie);
        TextView diamterEspecie = findViewById(R.id.diamterEspecie);
        TextView dataEspecie = findViewById(R.id.dataEspecie);
        TextView nameAmostra = findViewById(R.id.nameAmostra);
        TextView nameLev = findViewById(R.id.nameLev);

        String diameter = String.valueOf(especie.getEs_diameter());
        String height = String.valueOf(especie.getEs_height());

        nameEspecie.setText(especie.getEs_name());
        nameCientifico.setText(especie.getEs_name_cient());
        familiaEspecie.setText(especie.getEs_familia());
        heightEspecie.setText(height);
        diamterEspecie.setText(diameter);
        dataEspecie.setText(especie.getEsp_data());
        String nameSAmostra = amostra.getAm_name()  + "/" + amostra.getId_mask();
        nameAmostra.setText(nameSAmostra);
        nameLev.setText(especie.getId_mask_lev());
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
