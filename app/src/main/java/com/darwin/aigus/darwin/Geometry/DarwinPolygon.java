package com.darwin.aigus.darwin.Geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SANTIAGO on 17/01/2018.
 */

public class DarwinPolygon {
    private int _id;
    private String id_mask;
    private Double lat;
    private Double lon;

    public DarwinPolygon(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public DarwinPolygon() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId_mask() {
        return id_mask;
    }

    public void setId_mask(String id_mask) {
        this.id_mask = id_mask;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public static List<DarwinPolygon> getLatLong(String stringPolygon){
        List<DarwinPolygon> listDarwinPolygon = new ArrayList<>();
        List<String> coordsPares = DarwinPolygon.getCoordinates(stringPolygon);
        List<String> lon = DarwinPolygon.listLong(coordsPares);
        List<String> lat = DarwinPolygon.listLat(coordsPares);

        for(int i = 0; i < lat.size(); i++){
            String x = lat.get(i);
            String y = lon.get(i);
            if(!x.equalsIgnoreCase("")){
                listDarwinPolygon.add(new DarwinPolygon(Double.parseDouble(x), Double.parseDouble(y)));
            }
        }
        return listDarwinPolygon;
    }

    private static List<String> getCoordinates(String kmlFile){
        List<String> cordinates = new ArrayList<>();
        String stringCleared = cleanString(kmlFile);
        //retorn em forma de lista
        List<String> coordsPares = coordsPares(stringCleared);

        List<String> coord =  new ArrayList<>();
        for(int i = 0; i < coordsPares.size(); i++){
            String s = coordsPares.get(i);
            String[] str = s.split(",");
            coord.add(str[0]);
        }
        return coord;
    }
    //
    static String cleanString(String string){
        int lenght = string.length();
        String s = string.substring(0, lenght-1);

        String first = s.replace(",", " ");
        String second = first.replace(" 0", "");
        return second.replace(" 0 ", " ");
    }
    //LISTA DE COORDENDAS
    static List<String> coordsPares(String string){
        List<String> groupString = new ArrayList<>();
        String[] s = string.split(" ");
        groupString.addAll(Arrays.asList(s));
        return groupString;
    }
    //LISTA DE LATITUDE
    private static List<String> listLat(List<String> coordsPares){
        List<String> lat = new ArrayList<>();
        for(int i = 0; i < coordsPares.size()-1; i++){
            lat.add(coordsPares.get(i++));
        }
        return lat;
    }
    //LISTA DE LONGITUDE
    private static List<String> listLong(List<String> coordsPares){
        List<String> lon = new ArrayList<>();
        for(int i = 0; i < coordsPares.size()-1; i++){
            lon.add(coordsPares.get(++i));

        }
        return lon;
    }
}
