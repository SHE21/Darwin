package com.darwin.aigus.darwin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darwin.aigus.darwin.AndroidUltils.AndroidSettings;
import com.darwin.aigus.darwin.AndroidUltils.DateDarwin;
import com.darwin.aigus.darwin.AndroidUltils.GenerateId;
import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;
import com.darwin.aigus.darwin.AndroidUltils.ValidateEmail;
import com.darwin.aigus.darwin.Async.AsyncInsertUser;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

import java.util.Calendar;

public class ActivityNewAccount extends AppCompatActivity {
    private String idUserSuper;

    private EditText firstName, lastName, instUser, emailUser, statedUser,cityUser, passUser, passAgainUser;
    private TextView birthDay;
    private Spinner profUser;
    private RadioGroup radioGroup;
    private RadioButton feminino, masculino;
    private CheckBox termos;
    private AutoCompleteTextView countryUser;
    private Context context;
    private Drawable filds, fildNull;

    public Context getContext() {
        return this;
    }

    public void setContext() {
        this.context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.registrese);

        //VERIFICA SE HÁ CONECÇÃO DOS DISPOSITIVO COM A INTERNET
        boolean connected = AndroidSettings.statusConnection(getBaseContext());
        //NO CONNECTION
        LinearLayout noConnection = findViewById(R.id.displayWarn);
        TextView title = findViewById(R.id.title);
        TextView menssage = findViewById(R.id.menssage);

        if (!connected){
            title.setText(R.string.noConnection);
            menssage.setText(R.string.mnoConnection);
            noConnection.setVisibility(View.VISIBLE);
            noConnection.setOnClickListener(v -> noConnection.setVisibility(View.GONE));
        }

        TextView alertError = findViewById(R.id.alertError);
        alertError.setOnClickListener(v -> alertError.setVisibility(View.INVISIBLE));

        fildNull = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_error, null);
        filds = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_background_white_border, null);

        //DADOS PESSOAIS
        firstName = findViewById(R.id.firstName);//nome
        lastName = findViewById(R.id.lastName);//sobrenome
        radioGroup = findViewById(R.id.radioGroup);//generus
        feminino = findViewById(R.id.feminino);//feminino
        masculino = findViewById(R.id.masculino);//masculino
        birthDay = findViewById(R.id.birthDay);//aniversário
        emailUser = findViewById(R.id.emailUser);//email

        //DADOS PROFISSIONAIS
        profUser = findViewById(R.id.profUser);//profissão
        instUser = findViewById(R.id.instUser);//instituição

        //ENDEREÇO
        countryUser = findViewById(R.id.countryUser);//país
        statedUser = findViewById(R.id.statedUser);//estado
        cityUser = findViewById(R.id.cityUser);//cidade

        //SENHA
        passUser = findViewById(R.id.passUser);//senha
        passAgainUser = findViewById(R.id.passAgainUser);//senha de novo
        termos = findViewById(R.id.termos);

        //listener de errors
        firstName.setOnFocusChangeListener(onFocusChangeListener());
        lastName.setOnFocusChangeListener(onFocusChangeListener());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> radioGroup.setBackground(null));
        birthDay.setOnClickListener(onClickBirthDay());
        emailUser.setOnFocusChangeListener(onFocusChangeListener());

        instUser.setOnFocusChangeListener(onFocusChangeListener());
        countryUser.setOnFocusChangeListener(onFocusChangeListener());

        statedUser.setOnFocusChangeListener(onFocusChangeListener());
        cityUser.setOnFocusChangeListener(onFocusChangeListener());
        passUser.setOnFocusChangeListener(onFocusChangeListener());
        passAgainUser.setOnFocusChangeListener(onFocusChangeListener());

        //SPINNER PROFISSÕES
        ArrayAdapter<CharSequence> adapterProfUser = ArrayAdapter.createFromResource(this, R.array.arrayProfission, android.R.layout.simple_spinner_item);
        adapterProfUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profUser.setAdapter(adapterProfUser);

        //AUTOCOMPLETE COUNTRIES
        ArrayAdapter<CharSequence> adapterCountries = ArrayAdapter.createFromResource(this, R.array.arrayCountries, android.R.layout.simple_dropdown_item_1line);
        countryUser.setAdapter(adapterCountries);

        Button  btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener((View v) -> {
            String generus = "";
            int generusChecked = radioGroup.getCheckedRadioButtonId();
            if(generusChecked == feminino.getId()){
                generus = feminino.getText().toString();

            } else if (generusChecked == masculino.getId()){
                generus = masculino.getText().toString();
            }

            String name = firstName.getText().toString();
            String sobrename = lastName.getText().toString();
            String niver = birthDay.getText().toString();
            String emailuser = emailUser.getText().toString();

            String profissao;
            String pp = getResources().getString(R.string.selectProfission);
            String p = profUser.getSelectedItem().toString();
            if (p.equals(pp)){profissao = ""; }else{ profissao = profUser.getSelectedItem().toString();}
            String instituicao = instUser.getText().toString();

            String country = countryUser.getText().toString();
            String stated = statedUser.getText().toString();
            String city = cityUser.getText().toString();

            String passuser = passUser.getText().toString();
            String passagain = passAgainUser.getText().toString();

            //SE OS CAMPOS ESTÃO EM BRANCO
            if (name.isEmpty()|| sobrename.isEmpty()|| niver.isEmpty()|| generus.isEmpty()|| profissao.isEmpty()|| instituicao.isEmpty()||country.isEmpty()|| city.isEmpty()|| stated.isEmpty()|| emailuser.isEmpty()|| passuser.isEmpty() || passagain.isEmpty()){
                alertError.setVisibility(View.VISIBLE);
                alertError.setText(R.string.errorCampoBranco);

                if (name.isEmpty())firstName.setBackground(fildNull);
                if (sobrename.isEmpty())lastName.setBackground(fildNull);
                if (generus.isEmpty())radioGroup.setBackground(fildNull);
                if (niver.isEmpty())birthDay.setBackground(fildNull);
                if (emailuser.isEmpty())emailUser.setBackground(fildNull);
                if (profissao.isEmpty())profUser.setBackground(fildNull);
                if (instituicao.isEmpty())instUser.setBackground(fildNull);
                if (country.isEmpty())countryUser.setBackground(fildNull);
                if (stated.isEmpty())statedUser.setBackground(fildNull);
                if (city.isEmpty())cityUser.setBackground(fildNull);
                if (passuser.isEmpty()){passUser.setBackground(fildNull);}
                if (passagain.isEmpty()){passAgainUser.setBackground(fildNull);}

            }else{
                //SE A SENHA TEM MAIS DE 8 CARACTERES
                if (passuser.length() >= 8) {

                    //SE AS SENHAS SÃO IGUAIS
                    if (!passuser.equals(passagain)) {
                        alertError.setVisibility(View.VISIBLE);
                        alertError.setText(R.string.errorpass);

                    } else {

                        //SEO EMAIL É VÁLIDO
                        if (ValidateEmail.validateEmail(emailuser)) {
                            idUserSuper = GenerateId.generateId();
                            UserDarwin userDarwin = new UserDarwin();
                            userDarwin.setId_user(idUserSuper);
                            userDarwin.setFirstName(name);
                            userDarwin.setLastName(sobrename);
                            userDarwin.setGenerus(generus);
                            userDarwin.setBirthDay(niver);
                            userDarwin.setEmail(emailuser);

                            userDarwin.setProfession(profissao);
                            userDarwin.setInstitution(instituicao);

                            userDarwin.setCountry(country);
                            userDarwin.setStated(stated);
                            userDarwin.setCity(city);

                            userDarwin.setPass(passuser);
                            userDarwin.setDate(DateDarwin.getDate());

                            AsyncInsertUser asyncInsertUser = new AsyncInsertUser(userInsertSucesse(), new DelegateDarwin(getContext()));
                            asyncInsertUser.execute(userDarwin);

                        } else {
                            alertError.setVisibility(View.VISIBLE);
                            alertError.setText(R.string.errorEmail);
                        }
                    }
                }else{
                    alertError.setVisibility(View.VISIBLE);
                    alertError.setText(R.string.maior8pass);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_new_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_info:

                break;

            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    private AsyncInsertUser.UserInsertSucesse userInsertSucesse(){
        return result -> {
            if (result != 0) {
                PreferencesDarwin preferencesDarwin = new PreferencesDarwin(getContext());
                if (preferencesDarwin.setUserAccount(idUserSuper)) {
                    preferencesDarwin.managerUserLog();
                    goToHome();
                    Toast.makeText(ActivityNewAccount.this, R.string.ToastCreateAccount, Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(ActivityNewAccount.this, R.string.dataBaseErro, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ActivityNewAccount.this, R.string.dataBaseErro, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(){
        return (view, year, month, dayOfMonth) -> {
            String data = dayOfMonth + "/" + month + "/" + year;
            birthDay.setText(data);

        };
    }

    private View.OnFocusChangeListener onFocusChangeListener(){
        return (v, hasFocus) -> {
            if (hasFocus)
                v.setBackground(filds);
        };
    }

    private View.OnClickListener onClickBirthDay(){
        return v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialogDate = new DatePickerDialog(ActivityNewAccount.this,android.R.style.Theme_DeviceDefault_Dialog, onDateSetListener(), year, month, day);
            dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            dialogDate.show();
            v.setBackground(filds);
        };
    }

    public void goToHome(){
        Intent intent = new Intent(this, ActivityHome.class);
        startActivity(intent);
    }
}
