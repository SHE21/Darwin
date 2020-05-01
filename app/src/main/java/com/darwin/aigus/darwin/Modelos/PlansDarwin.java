package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;

/**
 * Created by SANTIAGO on 15/02/2018.
 */

public class PlansDarwin implements Serializable {
    private int id;
    private String idPlan;
    private String namePlan;
    private String typePlan;
    private double valuePlan;

    public PlansDarwin(){}

    public PlansDarwin(int id, String idPlan, String namePlan, String typePlan, double valuePlan) {
        this.id = id;
        this.idPlan = idPlan;
        this.namePlan = namePlan;
        this.typePlan = typePlan;
        this.valuePlan = valuePlan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getNamePlan() {
        return namePlan;
    }

    public void setNamePlan(String namePlan) {
        this.namePlan = namePlan;
    }

    public String getTypePlan() {
        return typePlan;
    }

    public void setTypePlan(String typePlan) {
        this.typePlan = typePlan;
    }

    public double getValuePlan() {
        return valuePlan;
    }

    public void setValuePlan(double valuePlan) {
        this.valuePlan = valuePlan;
    }
}
