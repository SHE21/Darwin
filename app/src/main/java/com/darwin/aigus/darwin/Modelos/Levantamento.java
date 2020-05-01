package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO on 21/11/2017.
 */

public class Levantamento implements Serializable {
    private int id;
    private String lev_id_mask;
    private String lev_name;
    private String lev_geodata;
    private String lev_date;

    public Levantamento() {}

    public Levantamento(int id, String lev_name, String lev_geodata, String lev_date) {
        this.id = id;
        this.lev_name = lev_name;
        this.lev_geodata = lev_geodata;
        this.lev_date = lev_date;
    }

    public String getLev_id_mask() {
        return lev_id_mask;
    }

    public void setLev_id_mask(String lev_id_mask) {
        this.lev_id_mask = lev_id_mask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLev_name() {
        return lev_name;
    }

    public void setLev_name(String lev_name) {
        this.lev_name = lev_name;
    }

    public String getLev_geodata() {
        return lev_geodata;
    }

    public void setLev_geodata(String lev_geodata) {
        this.lev_geodata = lev_geodata;
    }

    public String getLev_date() {
        return lev_date;
    }

    public void setLev_date(String lev_date) {
        this.lev_date = lev_date;
    }

    public static List<Levantamento> getLev(){
        List<Levantamento> levantamentoList = new ArrayList<Levantamento>();
        levantamentoList.add(new Levantamento(1, "Fazenda são jose", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(2, "Fazenda Mariana", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(3, "Fazenda Miguel Joel", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(4, "Fazenda Sntiafo de Compostela", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(5, "Fazenda Seja", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(6, "Fazenda Ana Claudia", "datajnsdnjdfaff", "12-23-2871"));
        levantamentoList.add(new Levantamento(7, "Fazenda são jose", "datajnsdnjdfaff", "12-23-2871"));
        return levantamentoList;
    }
}
