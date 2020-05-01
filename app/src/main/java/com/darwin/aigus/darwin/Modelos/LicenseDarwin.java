package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SANTIAGO on 15/02/2018.
 */

public class LicenseDarwin implements Serializable{
    private int id;
    private String keyGeneral;
    private String idPlan;
    private String idUser;
    private String idDevice;
    private Date dateStart;
    private Date dateEnd;
    private boolean status;
    private String dominio;

    public LicenseDarwin(){}

    public LicenseDarwin(int id, String keyGeneral, String idPlan, String idUser, String idDevice, Date dateStart, Date dateEnd, boolean status, String dominio) {
        this.id = id;
        this.keyGeneral = keyGeneral;
        this.idPlan = idPlan;
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
        this.dominio = dominio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyGeneral() {
        return keyGeneral;
    }

    public void setKeyGeneral(String keyGeneral) {
        this.keyGeneral = keyGeneral;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }
}
