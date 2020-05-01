package com.darwin.aigus.darwin.Modelos;

import java.io.Serializable;

/**
 * Created by SANTIAGO on 28/11/2017.
 */

public class User implements Serializable{
    private int idUser;
    private String idUserMask;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailUser;
    private String passUser;

    public User(){}

    public User(String firstName, String lastName, String emailUser, String passUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailUser = emailUser;
        this.passUser = passUser;
    }

    public String getIdUserMask() {
        return idUserMask;
    }

    public void setIdUserMask(String idUserMask) {
        this.idUserMask = idUserMask;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

}
