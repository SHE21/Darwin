package com.darwin.aigus.darwin.Activity;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.darwin.aigus.darwin.AndroidUltils.PreferencesDarwin;
import com.darwin.aigus.darwin.AndroidUltils.ValidateEmail;
import com.darwin.aigus.darwin.Modelos.UserDarwin;
import com.darwin.aigus.darwin.R;
import com.darwin.aigus.darwin.SQLite.DelegateDarwin;

import java.util.Calendar;

public class ActivityEditUser extends AppCompatActivity {
    private EditText firstName, lastName, instUser, emailUser, statedUser,cityUser, passUser;
    private TextView birthDay;
    private Spinner profUser;
    private RadioGroup radioGroup;
    private RadioButton feminino, masculino;
    private AutoCompleteTextView countryUser;
    private CheckBox termos;
    private boolean connected;

    private Drawable fildNull;
    private Drawable filds;

    private TextView title, menssage, alertError;
    private LinearLayout displayError;
    private String idUser, passUserDb;
    private DelegateDarwin delegateDarwin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.editAccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PreferencesDarwin preferencesDarwin  = new PreferencesDarwin(getBaseContext());
        idUser = preferencesDarwin.getUserAccount();

        delegateDarwin = new DelegateDarwin(getBaseContext());
        UserDarwin userDarwins = delegateDarwin.getUserById(idUser);

        fildNull = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_error, null);
        filds = ResourcesCompat.getDrawable(getResources(), R.drawable.shape_background_white_border, null);

        passUserDb = userDarwins.getPass();

        displayError = findViewById(R.id.displayError);
        title = findViewById(R.id.title);
        menssage =  findViewById(R.id.menssage);
        alertError =  findViewById(R.id.alertError);

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
        termos = findViewById(R.id.termos);

        //SPINNER PROFISSÕES
        ArrayAdapter<CharSequence> adapterProfUser = ArrayAdapter.createFromResource(this, R.array.arrayProfission, android.R.layout.simple_spinner_item);
        adapterProfUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profUser.setAdapter(adapterProfUser);

        //AUTOCOMPLETE COUNTRIES
        ArrayAdapter<CharSequence> adapterCountries = ArrayAdapter.createFromResource(this, R.array.arrayCountries, android.R.layout.simple_dropdown_item_1line);
        countryUser.setAdapter(adapterCountries);

        //NO CONNECTION
        LinearLayout noConnection = findViewById(R.id.displayWarn);
        TextView title = findViewById(R.id.title);
        TextView menssage = findViewById(R.id.menssage);

        Button btnSave = findViewById(R.id.btnSaveEdit);

        if (userDarwins.getGenerus().equals("Masculino")){
            masculino.setChecked(true);}

        if (userDarwins.getGenerus().equals("Feminino")){
            feminino.setChecked(true);}

        firstName.setText(userDarwins.getFirstName());
        birthDay.setText(userDarwins.getBirthDay());
        lastName.setText(userDarwins.getLastName());
        emailUser.setText(userDarwins.getEmail());

        profUser.setSelection(getProfission(userDarwins.getProfession()));
        instUser.setText(userDarwins.getInstitution());

        countryUser.setText(userDarwins.getCountry());
        statedUser.setText(userDarwins.getStated());
        cityUser.setText(userDarwins.getCity());

        //listener de errors
        firstName.setOnFocusChangeListener((v, hasFocus) -> v.setBackground(filds));
        lastName.setOnFocusChangeListener(onFocusChangeListener());
        birthDay.setOnClickListener(onClickBirthDay());
        emailUser.setOnFocusChangeListener(onFocusChangeListener());

        //profUser.setOnClickListener(onClickListenerAdapter());
        instUser.setOnFocusChangeListener(onFocusChangeListener());
        countryUser.setOnFocusChangeListener(onFocusChangeListener());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {radioGroup.setBackground(filds); });
        statedUser.setOnFocusChangeListener(onFocusChangeListener());
        cityUser.setOnFocusChangeListener(onFocusChangeListener());
        passUser.setOnFocusChangeListener(onFocusChangeListener());

        //birthDay.setOnClickListener(onClickBirthDay());

        btnSave.setOnClickListener(v -> {
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

            String profissao = profUser.getSelectedItem().toString();
            String instituicao = instUser.getText().toString();

            String country = countryUser.getText().toString();
            String stated = statedUser.getText().toString();
            String city = cityUser.getText().toString();

            String passuser = passUser.getText().toString();

            if (name.isEmpty()|| sobrename.isEmpty()|| niver.isEmpty()|| generus.isEmpty()|| profissao.isEmpty()|| instituicao.isEmpty()||country.isEmpty()|| city.isEmpty()|| stated.isEmpty()|| emailuser.isEmpty()|| passuser.isEmpty()){
                alertError.setText(R.string.errorCampoBranco);
                alertError.setOnClickListener(onClickInvisibility());
                if (name.isEmpty())firstName.setBackground(fildNull);
                if (sobrename.isEmpty())lastName.setBackground(fildNull);
                if (niver.isEmpty())birthDay.setBackground(fildNull);
                if (emailuser.isEmpty())emailUser.setBackground(fildNull);
                if (profissao.isEmpty())profUser.setBackground(fildNull);
                if (instituicao.isEmpty())instUser.setBackground(fildNull);
                if (country.isEmpty())countryUser.setBackground(fildNull);
                if (stated.isEmpty())statedUser.setBackground(fildNull);
                if (city.isEmpty())cityUser.setBackground(fildNull);
                if (generus.isEmpty())radioGroup.setBackground(fildNull);
                if (passuser.isEmpty()){passUser.setBackground(fildNull); alertError.setText(R.string.errorNoPass);}

                alertError.setVisibility(View.VISIBLE);

            }else{

                if (ValidateEmail.validateEmail(emailuser)) {

                    if (passuser.equals(passUserDb)) {
                        UserDarwin userDarwin = new UserDarwin();

                        userDarwin.setId_user(idUser);

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

                        int i = delegateDarwin.updateUser(userDarwin);

                        if (i != 0) {
                            Toast.makeText(ActivityEditUser.this, R.string.saved, Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            title.setText(R.string.errorAlgoErrado);
                            menssage.setText(R.string.erroUpdate);
                        }

                    } else {
                        alertError.setVisibility(View.VISIBLE);
                        alertError.setText(R.string.errorPass);
                    }

                }else{
                    alertError.setVisibility(View.VISIBLE);
                    alertError.setText(R.string.errorEmail);
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(){
        return (view, year, month, dayOfMonth) -> {
            String data = dayOfMonth + "/" + month + "/" + year;
            birthDay.setText(data);

        };
    }

    private View.OnClickListener onClickInvisibility(){
        return v -> v.setVisibility(View.INVISIBLE);
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

                DatePickerDialog dialogDate = new DatePickerDialog(ActivityEditUser.this,android.R.style.Theme_DeviceDefault_Dialog, onDateSetListener(), year, month, day);
                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                dialogDate.show();
                v.setBackground(filds);
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_cancel:
                finish();
            break;

            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    public int getProfission(String profisao){
        int o = 0;
        String[] s = getResources().getStringArray(R.array.arrayProfission);

        for (int i = 0; i < s.length; i++){
            if (s[i].equals(profisao)){
                o = i;
            }
        }
        return o;
    }
}
