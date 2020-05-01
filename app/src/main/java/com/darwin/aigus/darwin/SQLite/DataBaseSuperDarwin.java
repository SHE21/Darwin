package com.darwin.aigus.darwin.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.darwin.aigus.darwin.AndroidUltils.DateDarwin;
import com.darwin.aigus.darwin.Modelos.LicenseDarwin;
import com.darwin.aigus.darwin.Modelos.PlansDarwin;
import com.darwin.aigus.darwin.Modelos.UserDarwin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseSuperDarwin extends SQLiteOpenHelper {
    private static final String NAME_DB = "darwin_super";
    private static final int VERSION_DB = 1;
    private static final String USER_DARWIN = "user_darwin";
    private static final String PLANS_DARWIN = "plans_darwin";
    private static final String LICENSE_DARWIN = "license_darwin";
    private SQLiteDatabase db = this.getWritableDatabase();

    DataBaseSuperDarwin(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql de criação da tabela USER_DARWIN
        String QueryUserDarwin = "CREATE TABLE "+ USER_DARWIN +
                " (_id INTEGER PRIMARY KEY," +
                " idUser TEXT," +
                " firstName TEXT," +
                " lastName TEXT," +
                " generus TEXT," +
                " birthDay TEXT," +
                " email TEXT," +
                " country TEXT," +
                " stated TEXT," +
                " city TEXT," +
                " profession TEXT," +
                " institution TEXT," +
                " pass TEXT," +
                " date TEXT," +
                " dateEdit TEXT)";

        //sql de criação da tabela PLANS_DARWIN
        String QueryPlansDarwin = "CREATE TABLE "+ PLANS_DARWIN +
                " (_id INTEGER PRIMARY KEY," +
                " idPlan TEXT," +
                " namePlan TEXT," +
                " typePlan TEXT," +
                " valuePlan REAL)";

        //sql de criação da tabela LICENSE_DARWIN
        String QueryLicenseDarwin = "CREATE TABLE "+ LICENSE_DARWIN +
                " (_id INTEGER PRIMARY KEY," +
                " keyGenaral TEXT," +
                " status NUMERIC," +
                " dominio TEXT," +
                " idPlan TEXT," +
                " idUser TEXT," +
                " idDevice TEXT," +
                " dateStart TEXT," +
                " dateEnd TEXT)";

        db.execSQL(QueryUserDarwin);
        db.execSQL(QueryPlansDarwin);
        db.execSQL(QueryLicenseDarwin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    long insertIntermediateUser(UserDarwin userDarwin){
        return insertUserDarwin(userDarwin);
    }

    ///////// START INSERTS //////////////////////////////////////
    private long insertUserDarwin(UserDarwin userDarwin){
        ContentValues values = new ContentValues();
        values.put("idUser", userDarwin.getId_user());
        values.put("firstName", userDarwin.getFirstName());
        values.put("lastName", userDarwin.getLastName());
        values.put("generus", userDarwin.getGenerus());
        values.put("birthDay", userDarwin.getBirthDay());
        values.put("email", userDarwin.getEmail());
        values.put("country", userDarwin.getCountry());
        values.put("stated", userDarwin.getStated());
        values.put("city", userDarwin.getCity());
        values.put("profession", userDarwin.getProfession());
        values.put("institution", userDarwin.getInstitution());
        values.put("pass", userDarwin.getPass());
        values.put("date", DateDarwin.getDate());
        values.put("dateEdit", "");

        return db.insert(USER_DARWIN, null, values);
    }

    public long insertPlansDarwin(PlansDarwin plans){
        ContentValues values = new ContentValues();
        values.put("idPlan", plans.getIdPlan());
        values.put("namePlan",plans.getNamePlan());
        values.put("typePlan", plans.getTypePlan());
        values.put("valeuPlan", plans.getValuePlan());

        return db.insert(PLANS_DARWIN, null, values);
    }

    public long insertLicenseDarwin(LicenseDarwin license){
        ContentValues values = new ContentValues();
        values.put("keyGenaral", license.getKeyGeneral());
        values.put("idPlan", license.getIdPlan());
        values.put("idUser", license.getIdUser());
        values.put("status", license.isStatus());
        values.put("dominio", license.getDominio());
        values.put("idDevice", license.getIdDevice());
        values.put("dateStart", String.valueOf(license.getDateStart()));
        values.put("dateEnd", String.valueOf(license.getDateEnd()));

        return db.insert(LICENSE_DARWIN, null, values);
    }
    ///////// END INSERTS /////////////////////////////////////////

    ///////// START SELECTS //////////////////////////////////////
    List<UserDarwin> getAllUsers() throws ParseException {
        String query = "SELECT*FROM " + USER_DARWIN;
        List<UserDarwin> users = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                UserDarwin user = new UserDarwin();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setId_user(cursor.getString(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                user.setGenerus(cursor.getString(4));
                user.setBirthDay(cursor.getString(5));
                user.setEmail(cursor.getString(6));
                user.setCountry(cursor.getString(7));
                user.setStated(cursor.getString(8));
                user.setCity(cursor.getString(9));
                user.setProfession(cursor.getString(10));
                user.setInstitution(cursor.getString(11));
                user.setPass(cursor.getString(12));
                user.setDate(cursor.getString(13));
                user.setDateEdit(cursor.getString(14));

                users.add(user);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return users;
    }

    UserDarwin getUserById(String idUser) {
        Cursor cursor = db.query(USER_DARWIN, new String[]{"_id", "idUser", "firstName", "lastName", "generus", "birthDay", "email", "country", "stated", "city", "profession", "institution", "pass", "date", "dateEdit"}, "idUser = ?",
                new String[]{idUser}, null, null, null, null);

        UserDarwin user = new UserDarwin();

        if (cursor.moveToFirst()){
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setId_user(cursor.getString(1));
            user.setFirstName(cursor.getString(2));
            user.setLastName(cursor.getString(3));
            user.setGenerus(cursor.getString(4));
            user.setBirthDay(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setCountry(cursor.getString(7));
            user.setStated(cursor.getString(8));
            user.setCity(cursor.getString(9));
            user.setProfession(cursor.getString(10));
            user.setInstitution(cursor.getString(11));
            user.setPass(cursor.getString(12));
            user.setDate(cursor.getString(13));
            user.setDateEdit(cursor.getString(14));
        }
        cursor.close();

        return user;
    }

    UserDarwin getUserToLog(String userName, String pass) throws ParseException {
        Cursor cursor = db.query(USER_DARWIN, new String[]{"_id", "idUser", "firstName", "lastName", "generus", "birthDay", "email", "country", "stated", "city", "profession", "institution", "pass", "date", "dateEdit"}, "firstName =? AND pass=?",
                new String[]{userName, pass}, null, null, null, null);

        UserDarwin user = new UserDarwin();

        if (cursor.moveToFirst()){
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setId_user(cursor.getString(1));
            user.setFirstName(cursor.getString(2));
            user.setLastName(cursor.getString(3));
            user.setGenerus(cursor.getString(4));
            user.setBirthDay(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setCountry(cursor.getString(7));
            user.setStated(cursor.getString(8));
            user.setCity(cursor.getString(9));
            user.setProfession(cursor.getString(10));
            user.setInstitution(cursor.getString(11));
            user.setPass(cursor.getString(12));
            user.setDate(cursor.getString(13));
            user.setDateEdit(cursor.getString(14));
        }
        cursor.close();
        return user;
    }

    UserDarwin getUserByEmail(String email){
        Cursor cursor = db.query(USER_DARWIN, new String[]{"_id", "idUser", "firstName", "lastName", "generus", "birthDay", "email", "country", "stated", "city", "profession", "institution", "pass", "date", "dateEdit"}, "email=?",
                new String[]{email}, null, null, null, null);

        UserDarwin user = null;

        if (cursor.moveToFirst()){
            user = new UserDarwin();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setId_user(cursor.getString(1));
            user.setFirstName(cursor.getString(2));
            user.setLastName(cursor.getString(3));
            user.setGenerus(cursor.getString(4));
            user.setBirthDay(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setCountry(cursor.getString(7));
            user.setStated(cursor.getString(8));
            user.setCity(cursor.getString(9));
            user.setProfession(cursor.getString(10));
            user.setInstitution(cursor.getString(11));
            user.setPass(cursor.getString(12));
            user.setDate(cursor.getString(13));
            user.setDateEdit(cursor.getString(14));
        }
        cursor.close();
        return user;
    }
    //"_id", "idUser", "firstName", "lastName", "generus", "birthDay", "email", "country", "stated", "city", "profession", "institution", "pass", "date", "dateEdit"
    int updateUser(UserDarwin userDarwin){
        ContentValues values = new ContentValues();
        values.put("firstName", userDarwin.getFirstName());
        values.put("lastName", userDarwin.getLastName());
        values.put("generus", userDarwin.getGenerus());
        values.put("birthDay", userDarwin.getBirthDay());
        values.put("email", userDarwin.getEmail());
        values.put("country", userDarwin.getCountry());
        values.put("city", userDarwin.getCity());
        values.put("profession", userDarwin.getProfession());
        values.put("institution", userDarwin.getInstitution());
        values.put("dateEdit", userDarwin.getDateEdit());

        return db.update(USER_DARWIN, values, "idUser = ?", new String[]{userDarwin.getId_user()});
    }

    int selectUserCountOne(String user, String pass){
        Cursor cursor = db.query(USER_DARWIN, new String[]{"idUser"}, "firstName = ? AND pass = ?", new String[]{user, pass}, null, null, null);
        int t = cursor.getCount();
        cursor.close();
        return t;
    }

    int selectUserCount(){
        Cursor cursor = db.query(USER_DARWIN, null, null, null, null, null, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    int selectUserByEmail(String email){
        Cursor cursor = db.query(USER_DARWIN, new String[]{"email"}, "email = ?", new String[]{email}, null, null, null);
        int t = cursor.getCount();
        cursor.close();
        return t;
    }


    //PEGAR TODOS OS PLANOS
    public List<PlansDarwin> getAllPlans(){
        List<PlansDarwin> plans = new ArrayList<>();
        String query = "SELECT*FROM " + PLANS_DARWIN;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                PlansDarwin plansDarwin = new PlansDarwin();
                plansDarwin.setId(Integer.parseInt(cursor.getString(0)));
                plansDarwin.setIdPlan(cursor.getString(1));
                plansDarwin.setNamePlan(cursor.getString(2));
                plansDarwin.setTypePlan(cursor.getString(3));
                plansDarwin.setValuePlan(Double.parseDouble(cursor.getString(4)));

                plans.add(plansDarwin);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return plans;
    }
    //PLANO POR LICENCA
    public PlansDarwin getPlanByLicense(LicenseDarwin license){
        Cursor cursor = db.query(LICENSE_DARWIN, new String[]{"_id", "idPlan", "namePlan", "typePlan", "valeuPlan"}, "idPlan = ?", new String[]{license.getIdPlan()}, null, null, null, null);
        PlansDarwin plansDarwin = new PlansDarwin();

        if (cursor.moveToFirst()){
            plansDarwin.setId(Integer.parseInt(cursor.getString(0)));
            plansDarwin.setIdPlan(cursor.getString(1));
            plansDarwin.setNamePlan(cursor.getString(2));
            plansDarwin.setTypePlan(cursor.getString(3));
            plansDarwin.setValuePlan(Double.parseDouble(cursor.getString(4)));
        }
        cursor.close();
        return plansDarwin;
    }
}
