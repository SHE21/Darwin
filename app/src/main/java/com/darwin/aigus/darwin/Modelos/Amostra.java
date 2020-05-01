package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO on 21/11/2017.
 */

public class Amostra implements Serializable{
    private int id;
    private String am_name;
    private Double am_geodata_lat;//latitude
    private Double am_geodata_long;//longitude
    private String am_date;
    private String id_mask_lev;
    private String id_mask;
    private int am_num_especie;

    public Amostra(){}

    public Amostra(int id, String am_name, Double am_geodata_lat, Double am_geodata_long, String am_date, String id_mask_lev, String id_mask) {
        this.id = id;
        this.am_name = am_name;
        this.am_geodata_lat = am_geodata_lat;
        this.am_geodata_long = am_geodata_long;
        this.am_date = am_date;
        this.id_mask_lev = id_mask_lev;
        this.id_mask = id_mask;
    }

    public int getAm_num_especie() {
        return am_num_especie;
    }

    public void setAm_num_especie(int am_num_especie) {
        this.am_num_especie = am_num_especie;
    }

    public String getId_mask_lev() {
        return id_mask_lev;
    }

    public void setId_mask_lev(String id_mask_lev) {
        this.id_mask_lev = id_mask_lev;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAm_name() {
        return am_name;
    }

    public void setAm_name(String am_name) {
        this.am_name = am_name;
    }

    public Double getAm_geodata_lat() {
        return am_geodata_lat;
    }

    public void setAm_geodata_lat(Double am_geodata_lat) {
        this.am_geodata_lat = am_geodata_lat;
    }

    public Double getAm_geodata_long() {
        return am_geodata_long;
    }

    public void setAm_geodata_long(Double am_geodata_long) {
        this.am_geodata_long = am_geodata_long;
    }

    public String getAm_date() {
        return am_date;
    }

    public void setAm_date(String am_date) {
        this.am_date = am_date;
    }

    public String getId_mask() {
        return id_mask;
    }

    public void setId_mask(String id_mask) {
        this.id_mask = id_mask;
    }
}
