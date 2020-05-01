package com.darwin.aigus.darwin.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.darwin.aigus.darwin.AndroidUltils.DateDarwin;
import com.darwin.aigus.darwin.AndroidUltils.KmlPolygon;
import com.darwin.aigus.darwin.Geometry.DarwinPolygon;
import com.darwin.aigus.darwin.Modelos.Amostra;
import com.darwin.aigus.darwin.Modelos.Especie;
import com.darwin.aigus.darwin.Modelos.Levantamento;
import com.darwin.aigus.darwin.Modelos.User;
import com.darwin.aigus.darwin.Modelos.UserDarwin;

import java.util.ArrayList;
import java.util.List;

public class DataBaseDarwin extends SQLiteOpenHelper {
    private static final String NAME_DB = "darwin";
    private static final int VERSION_DB = 2;
    private static final String LEVANTAMENTO_TABLE = "levantamento_dar";
    private static final String AMOSTRA_TABLE = "amostra_dar";
    private static final String ESPECIE_TABLE = "especie_dar";
    private static final String USER_TABLE = "user_dar";
    private static final String USER_DARWIN = "user_darwin";
    private static final String GEO_LEV = "geo_lev";

    public DataBaseDarwin(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //query SQL
        String query_lev = "CREATE TABLE levantamento_dar (_id INTEGER PRIMARY KEY, id_mask TEXT, lev_name TEXT, lev_geodata TEXT, lev_date TEXT)";
        String query_amost = "CREATE TABLE amostra_dar (_id INTEGER PRIMARY KEY, id_mask TEXT, id_mask_lev TEXT, am_name TEXT, am_geodata_lat TEXT, am_geodata_long TEXT, am_date TEXT)";
        String query_especie = "CREATE TABLE especie_dar (_id INTEGER PRIMARY KEY, id_mask_esp TEXT, id_mask_lev TEXT, id_mask_am TEXT, es_name TEXT, es_name_cient TEXT, es_familia TEXT, es_height REAL, es_diameter REAL, es_date TEXT)";
        String query_user = "CREATE TABLE user_dar (_id INTEGER PRIMARY KEY, idUserMask TEXT, firstName TEXT, lastName TEXT, emailUser TEXT, passUser TEXT)";
        String query_geolev = "CREATE TABLE geo_lev (_id INTEGER PRIMARY KEY, id_mask TEXT, polyLat TEXT, polyLon TEXT)";
        //cria as tabelas darwin
        db.execSQL(query_lev);
        db.execSQL(query_amost);
        db.execSQL(query_especie);
        db.execSQL(query_user);
        db.execSQL(query_geolev);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertGeoLev(DarwinPolygon darwinPolygon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_mask", darwinPolygon.getId_mask());
        values.put("polyLat", String.valueOf(darwinPolygon.getLat()));
        values.put("polyLon", String.valueOf(darwinPolygon.getLon()));

        db.insert(GEO_LEV, null, values);
    }

    public void deleteGeodata(Levantamento levantamento){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GEO_LEV, "id_mask = ?", new String[]{String.valueOf(levantamento.getLev_id_mask())});
        db.close();
    }

    public List<KmlPolygon> getGeoLev(String id_mask){
        SQLiteDatabase db = this.getReadableDatabase();
        List<KmlPolygon> kmlPolygonList = new ArrayList<>();
        String query = "SELECT * FROM " + GEO_LEV + " WHERE id_mask = '" + id_mask + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                KmlPolygon kmlPolygon = new KmlPolygon();
                kmlPolygon.set_id(Integer.parseInt(cursor.getString(0)));
                kmlPolygon.setId_mask(cursor.getString(1));
                kmlPolygon.setLat(Double.parseDouble(cursor.getString(2)));
                kmlPolygon.setLon(Double.parseDouble(cursor.getString(3)));

                kmlPolygonList.add(kmlPolygon);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return kmlPolygonList;
    }

    public int getGeoLevCount(String id_mask){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + GEO_LEV + " WHERE id_mask = '" + id_mask + "'";
        Cursor cursor = db.rawQuery(query, null);
        int t = cursor.getCount();
        cursor.close();
        return  t;
    }
    //delete dados geograficos
    public int deleteGeoLev(String id_mask){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GEO_LEV, "id_mask = ?", new String[]{id_mask});
    }

    public List<KmlPolygon> getGeoLevAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<KmlPolygon> kmlPolygonList = new ArrayList<>();
        String query = "SELECT * FROM " + GEO_LEV;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                KmlPolygon kmlPolygon = new KmlPolygon();
                kmlPolygon.set_id(Integer.parseInt(cursor.getString(0)));
                kmlPolygon.setId_mask(cursor.getString(1));
                kmlPolygon.setLat(Double.parseDouble(cursor.getString(2)));
                kmlPolygon.setLon(Double.parseDouble(cursor.getString(3)));

                kmlPolygonList.add(kmlPolygon);//NÃO ESQUECER NUNCA DISSO

            }while (cursor.moveToNext());
        }
        cursor.close();
        return kmlPolygonList;
    }

    public long insertLevantamento(Levantamento levantamento){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_mask", levantamento.getLev_id_mask());
        values.put("lev_name", levantamento.getLev_name());
        values.put("lev_geodata", levantamento.getLev_geodata());
        values.put("lev_date", levantamento.getLev_date());

        return db.insert(LEVANTAMENTO_TABLE, null, values);
    }

    //INSERT AS AMOSTRAS NO BANDO DE DADOS
    public long insertAmostra(Amostra amostra){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_mask", amostra.getId_mask());
        values.put("id_mask_lev", amostra.getId_mask_lev());
        values.put("am_name", amostra.getAm_name());
        values.put("am_geodata_lat", String.valueOf(amostra.getAm_geodata_lat()));
        values.put("am_geodata_long", String.valueOf(amostra.getAm_geodata_long()));
        values.put("am_date", amostra.getAm_date());

        return db.insert(AMOSTRA_TABLE, null, values);
    }

    public Levantamento selectLevBayId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LEVANTAMENTO_TABLE, new String[]{"_id", "lev_name", "lev_geodata", "lev_date"}, "_id = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Levantamento levantamento = new Levantamento();

        if (cursor.moveToFirst()) {
            levantamento.setId(Integer.parseInt(cursor.getString(0)));//set id_lev
            levantamento.setLev_id_mask(cursor.getString(1));
            levantamento.setLev_name(cursor.getString(2));
            levantamento.setLev_geodata(cursor.getString(3));
            levantamento.setLev_date(cursor.getString(4));

        }
        cursor.close();
        return levantamento;
    }

    //SELECT TODOS OS LEVANTAMENTOS
    public List<Levantamento> getAllLevantamento(){
        List<Levantamento> levantamentoList = new ArrayList<Levantamento>();
        String query = "SELECT * FROM " + LEVANTAMENTO_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Levantamento levantamento = new Levantamento();
                levantamento.setId(Integer.parseInt(cursor.getString(0)));//set id_lev
                levantamento.setLev_id_mask(cursor.getString(1));
                levantamento.setLev_name(cursor.getString(2));
                levantamento.setLev_geodata(cursor.getString(3));
                levantamento.setLev_date(cursor.getString(4));

                levantamentoList.add(levantamento);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return levantamentoList;
    }

    //total de levantamentos
    public int countLev(){
        String query = "SELECT * FROM " + LEVANTAMENTO_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public int countAmostra(String id_mask_lev){
        String query = "SELECT * FROM " + AMOSTRA_TABLE + " WHERE id_mask_lev = '" + id_mask_lev +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }
    //total especies
    public int countEspecie(String id_mask_am){
        String query = "SELECT * FROM " + ESPECIE_TABLE + " WHERE id_mask_am = '" + id_mask_am + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public int countEspecieByLev(String id_mask_lev){
        String query = "SELECT * FROM " + ESPECIE_TABLE + " WHERE id_mask_lev = '" + id_mask_lev + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    //DELETA UM LEVANTAMENTO
    public void deleteLevById(Levantamento levantamento){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LEVANTAMENTO_TABLE, "_id = ?", new String[]{String.valueOf(levantamento.getId())});
        db.close();
    }
    //EDITA LEVANTAMENTO
    public void updateLev(String id_lev, String newName){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lev_name", newName);

        db.update(LEVANTAMENTO_TABLE, values, "id_mask = ?", new String[]{id_lev});
    }

    //EDITAR ESPECIE es_name TEXT, es_name_cient TEXT, es_familia TEXT, es_height REAL, es_diameter REAL, es_date TEXT
    public void updateEspecie(Especie especie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("es_name", especie.getEs_name());
        values.put("es_name_cient", especie.getEs_name_cient());
        values.put("es_familia", especie.getEs_familia());
        values.put("es_height", especie.getEs_height());
        values.put("es_diameter", especie.getEs_diameter());
        values.put("es_date", especie.getEsp_data());

        db.update(ESPECIE_TABLE, values, "_id = ?", new String[]{String.valueOf(especie.getId())});
    }

    //_id INTEGER PRIMARY KEY, id_lev INTEGER, am_name TEXT, am_geodata_lat DOUBLE, am_geodata_long DOUBLE, am_date TEXT
    public List<Amostra> getAmostras(String id_mask_lev){
        List<Amostra> amostraList =  new ArrayList<>();
        String query = "SELECT * FROM " + AMOSTRA_TABLE + " WHERE id_mask_lev = '" + id_mask_lev + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Amostra amostra = new Amostra();
                amostra.setId(Integer.parseInt(cursor.getString(0)));
                amostra.setId_mask(cursor.getString(1));
                amostra.setId_mask_lev(cursor.getString(2));
                amostra.setAm_name(cursor.getString(3));
                amostra.setAm_geodata_lat(Double.parseDouble(cursor.getString(4)));
                amostra.setAm_geodata_long(Double.parseDouble(cursor.getString(5)));
                amostra.setAm_date(cursor.getString(6));

                amostraList.add(amostra);

            }while (cursor.moveToNext());
        }
        return amostraList;
    }
    //DELETAR AMOSTRA
    public void deleteAmostra(Amostra amostra){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AMOSTRA_TABLE, "_id = ?", new String[]{String.valueOf(amostra.getId())});
        db.close();
    }

    public int deleteAllAmostra(Levantamento levantamento){
        SQLiteDatabase db = this.getWritableDatabase();
        int i =  db.delete(AMOSTRA_TABLE, "id_mask_lev = ?", new String[]{String.valueOf(levantamento.getLev_id_mask())});
        db.close();
        return i;
    }
    //EDITAR AMOSTRA
    public int updateAmostra(Amostra amostra){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("am_name", amostra.getAm_name());
        values.put("am_geodata_lat", amostra.getAm_geodata_lat());
        values.put("am_geodata_long", amostra.getAm_geodata_long());
        values.put("am_date", "Editado " + DateDarwin.getDate());

        int t = db.update(AMOSTRA_TABLE, values, "_id = ?", new String[]{String.valueOf(amostra.getId())});
        db.close();
        return t;
    }

    public void insertEspecie(Especie especie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_mask_esp", especie.getId_mask_esp());
        values.put("id_mask_lev", especie.getId_mask_lev());
        values.put("id_mask_am", especie.getId_mask_am());
        values.put("es_name", especie.getEs_name());
        values.put("es_name_cient", especie.getEs_name_cient());
        values.put("es_familia", especie.getEs_familia());
        values.put("es_height", especie.getEs_height());
        values.put("es_diameter", especie.getEs_diameter());
        values.put("es_date", especie.getEsp_data());

        db.insert(ESPECIE_TABLE, null, values);
    }
    public List<Especie> getEspecies(String id_mask_am){
        List<Especie> especieList = new ArrayList<>();
        String query = "SELECT * FROM " + ESPECIE_TABLE + " WHERE id_mask_am = '" + id_mask_am + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //_id INTEGER PRIMARY KEY, id_mask_lev TEXT, id_mask_am TEXT, es_name TEXT, es_height REAL, es_diameter REAL, es_date TEXT
        if (cursor.moveToFirst()){
            do {
                Especie especie = new Especie();
                especie.setId(Integer.parseInt(cursor.getString(0)));
                especie.setId_mask_esp(cursor.getString(1));
                especie.setId_mask_lev(cursor.getString(2));
                especie.setId_mask_am(cursor.getString(3));
                especie.setEs_name(cursor.getString(4));
                especie.setEs_name_cient(cursor.getString(5));
                especie.setEs_familia(cursor.getString(6));
                especie.setEs_height(Double.parseDouble(cursor.getString(7)));
                especie.setEs_diameter(Double.parseDouble(cursor.getString(8)));
                especie.setEsp_data(cursor.getString(9));

                especieList.add(especie);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return especieList;
    }

    public List<Especie> getEspeciesByLevant(String idLevant){
        List<Especie> especieList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ESPECIE_TABLE, new String[]{"_id", "id_mask_lev", "id_mask_am", "es_name", "es_height", "es_diameter", "es_diameter", "es_date"}, "id_mask_lev = ?", new String[]{idLevant}, null, null, null, null);
        //_id INTEGER PRIMARY KEY, id_mask_lev TEXT, id_mask_am TEXT, es_name TEXT, es_height REAL, es_diameter REAL, es_date TEXT
        if (cursor.moveToFirst()){
            do {
                Especie especie = new Especie();
                especie.setId(Integer.parseInt(cursor.getString(0)));
                especie.setId_mask_esp(cursor.getString(1));
                especie.setId_mask_lev(cursor.getString(2));
                especie.setId_mask_am(cursor.getString(3));
                especie.setEs_name(cursor.getString(4));
                especie.setEs_name_cient(cursor.getString(5));
                especie.setEs_familia(cursor.getString(6));
                especie.setEs_height(Double.parseDouble(cursor.getString(7)));
                especie.setEs_diameter(Double.parseDouble(cursor.getString(8)));
                especie.setEsp_data(cursor.getString(9));

                especieList.add(especie);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return especieList;
    }
    //PEGAR TODAS AS ESPECIES
    public List<Especie> getAllEspecies(){
        List<Especie> especieList = new ArrayList<>();
        String query = "SELECT * FROM " + ESPECIE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Especie especie = new Especie();
                especie.setId(Integer.parseInt(cursor.getString(0)));
                especie.setId_mask_esp(cursor.getString(1));
                especie.setId_mask_lev(cursor.getString(2));
                especie.setId_mask_am(cursor.getString(3));
                especie.setEs_name(cursor.getString(4));
                especie.setEs_name_cient(cursor.getString(5));
                especie.setEs_familia(cursor.getString(6));
                especie.setEs_height(Double.parseDouble(cursor.getString(7)));
                especie.setEs_diameter(Double.parseDouble(cursor.getString(8)));
                especie.setEsp_data(cursor.getString(9));

                especieList.add(especie);

            }while (cursor.moveToNext());
        }
        cursor.close();

        return especieList;
    }
    //DELETAR ESPECIES POR ID
    public void deleteEspecie(Especie especie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ESPECIE_TABLE, "_id = ?", new String[]{String.valueOf(especie.getId())});
        db.close();
    }
    //DELETAR ESPEICES POR AMOSTRA
    public void deleteEspecieByAmostra(Amostra amostra){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ESPECIE_TABLE, "id_mask_am = ?", new String[]{String.valueOf(amostra.getId_mask())});
        db.close();
    }
    public int deleteEspecieByLev(String id_mask_lev){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(ESPECIE_TABLE, "id_mask_lev = ?", new String[]{String.valueOf(id_mask_lev)});
        db.close();
        return i;
    }
    /*
      //CRUD COM A TABELA --> USER <--
      //INSERI UM USER NO BANCO DE DADOS
      //id INTEGER PRIMARY KEY, userName TEXT, firstName TEXT, lastName TEXT, emailUser TEXT, passUser TEXT
    */

    public List<User> selectAllUser(){
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                User user = new User();
                user.setIdUser(Integer.parseInt(cursor.getString(0)));
                user.setIdUserMask(cursor.getString(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                user.setEmailUser(cursor.getString(4));

                userList.add(user);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public User selectUser(String idMask){
        User user = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE idUserMask = '" + idMask + "'";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                user.setIdUser(Integer.parseInt(cursor.getString(0)));
                user.setIdUserMask(cursor.getString(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                user.setEmailUser(cursor.getString(4));
                user.setPassUser(cursor.getString(5));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return user;
    }

    public User selectUserToLog(String firstName, String pass) {
        User user = new User();
        //idUserMask TEXT, firstName TEXT, lastName TEXT, emailUser TEXT, passUser TEXT
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(USER_TABLE, new String[]{"_id", "idUserMask", "firstName", "lastName", "emailUser", "passUser"}, "firstName=? AND passUser=?",
                new String[]{firstName, pass}, null, null, null, null);

        if (cursor.moveToFirst()) {
            user.setIdUser(Integer.parseInt(cursor.getString(0)));
            user.setIdUserMask(cursor.getString(1));
            user.setFirstName(cursor.getString(2));
            user.setLastName(cursor.getString(3));
            user.setEmailUser(cursor.getString(4));
            user.setPassUser(cursor.getString(5));
        }
        cursor.close();
        return user;
    }

    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("emailUser", user.getEmailUser());

        return db.update(USER_TABLE, values,"idUserMask = ?", new String[]{user.getIdUserMask()});
    }

    public int selectUserCount(){
        String query = "SELECT * FROM " + USER_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int t = cursor.getCount();
        cursor.close();
        return t;
    }
}
