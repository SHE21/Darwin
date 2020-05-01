package com.darwin.aigus.darwin.AndroidUltils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KmlPolygon {
    private int _id;
    private String id_mask;
    private Double lat;
    private Double lon;

    public KmlPolygon() {
    }

    public KmlPolygon(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
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

    public static List<KmlPolygon> getCoordinates(String kmlFile, String name){
        List<KmlPolygon> kmlPolygonList = new ArrayList<>();

        List<String> listaCoords = coordsPares(cleanString(kmlFile));
        List<String> listLat = listLat(listaCoords);
        List<String> listLong = listLong(listaCoords);

        for(int d = 0; d < listLat.size(); d++) {
            KmlPolygon kmlPolygon = new KmlPolygon();
            kmlPolygon.setId_mask(name);
            kmlPolygon.setLat(Double.parseDouble(listLat.get(d)));
            kmlPolygon.setLon(Double.parseDouble(listLong.get(d)));

            kmlPolygonList.add(kmlPolygon);
        }
        return kmlPolygonList;
    }

    private static String cleanString(String string){
        String first = string.replace(",", " ");
        String second = first.replace(" 0", "");
        return second.replace(" 0 ", " ");
    }
    //LISTA DE COORDENDAS
    private static List<String> coordsPares(String string){
        List<String> groupString = new ArrayList<>();
        String[] s = string.split(" ");
        groupString.addAll(Arrays.asList(s));
        return groupString;
    }
    //LISTA DE LOTITUDE
    private static List<String> listLat(List<String> string){
        List<String> listLat = new ArrayList<>();

        for(int i = 0; i < string.size(); i++){
            listLat.add(string.get(i++));
        }
        return listLat;
    }
    //LISTA DE LONGITUDE
    private static List<String> listLong(List<String> string){
        List<String> listLong = new ArrayList<>();

        for(int i = 0; i < string.size(); ++i){
            listLong.add(string.get(++i));
        }
        return listLong;
    }
}
