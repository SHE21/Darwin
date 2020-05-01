package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO on 21/11/2017.
 */

public class Especie implements Serializable {
    private int id;
    private String id_mask_esp;
    private String id_mask_lev;
    private String id_mask_am;
    private String es_name;
    private String es_name_cient;
    private String es_familia;
    private double es_height;
    private double es_diameter;
    private String esp_data;

    public Especie() {}

    public Especie(String id_mask_lev, String id_mask_am, String es_name, double es_height, double es_diameter, String esp_data) {
        this.id = id;
        this.id_mask_lev = id_mask_lev;
        this.id_mask_am = id_mask_am;
        this.es_name = es_name;
        this.es_height = es_height;
        this.es_diameter = es_diameter;
        this.esp_data = esp_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_mask_esp() {
        return id_mask_esp;
    }

    public void setId_mask_esp(String id_mask_esp) {
        this.id_mask_esp = id_mask_esp;
    }

    public String getId_mask_lev() {
        return id_mask_lev;
    }

    public void setId_mask_lev(String id_mask_lev) {
        this.id_mask_lev = id_mask_lev;
    }

    public String getId_mask_am() {
        return id_mask_am;
    }

    public void setId_mask_am(String id_mask_am) {
        this.id_mask_am = id_mask_am;
    }

    public String getEs_name() {
        return es_name;
    }

    public void setEs_name(String es_name) {
        this.es_name = es_name;
    }

    public double getEs_height() {
        return es_height;
    }

    public void setEs_height(double es_height) {
        this.es_height = es_height;
    }

    public double getEs_diameter() {
        return es_diameter;
    }

    public void setEs_diameter(double es_diameter) {
        this.es_diameter = es_diameter;
    }

    public String getEsp_data() {
        return esp_data;
    }

    public void setEsp_data(String esp_data) {
        this.esp_data = esp_data;
    }

    public String getEs_name_cient() {
        return es_name_cient;
    }

    public void setEs_name_cient(String es_name_cient) {
        this.es_name_cient = es_name_cient;
    }

    public String getEs_familia() {
        return es_familia;
    }

    public void setEs_familia(String es_familia) {
        this.es_familia = es_familia;
    }
}
