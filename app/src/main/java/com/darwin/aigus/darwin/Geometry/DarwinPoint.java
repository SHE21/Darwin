package com.darwin.aigus.darwin.Geometry;

/**
 * Created by SANTIAGO on 17/01/2018.
 */

public class DarwinPoint {
    private int _id;
    private String id_mask;
    private Double lat;
    private Double lon;

    public DarwinPoint(){}

    public DarwinPoint(int _id, String id_mask, Double lat, Double lon) {
        this._id = _id;
        this.id_mask = id_mask;
        this.lat = lat;
        this.lon = lon;
    }

    public DarwinPoint(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
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
}
