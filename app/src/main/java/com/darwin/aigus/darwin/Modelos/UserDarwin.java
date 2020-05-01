package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;

public class UserDarwin implements Serializable {
    private int id;
    private String idUser;
    private String firstName;
    private String lastName;
    private String generus;
    private String birthDay;
    private String email;
    private String country;
    private String stated;
    private String city;
    private String profession;
    private String institution;
    private String pass;
    private String date;
    private String dateEdit;

    public UserDarwin(){}

    public UserDarwin(String idUser, String firstName, String lastName, String generus, String birthDay, String email, String country, String stated, String city, String profession, String institution, String pass, String date, String dateEdit) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.generus = generus;
        this.birthDay = birthDay;
        this.email = email;
        this.country = country;
        this.stated = stated;
        this.city = city;
        this.profession = profession;
        this.institution = institution;
        this.pass = pass;
        this.date = date;
        this.dateEdit = dateEdit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_user() {
        return idUser;
    }

    public void setId_user(String id_user) {
        this.idUser = id_user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGenerus() {
        return generus;
    }

    public void setGenerus(String generus) {
        this.generus = generus;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStated() {
        return stated;
    }

    public void setStated(String stated) {
        this.stated = stated;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(String dateEdit) {
        this.dateEdit = dateEdit;
    }
}
