package com.btlandroid.music.model;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;

public class User implements Serializable {
    private int user_id;
    private String user_name;
    private String user_email;
    private String user_IdGoogle;
    private String user_IdFacebook;
    private String user_pass;
    private String user_url_image;
    private boolean status_active;

    public User() {
    }

    public User(String user_name, String user_IdFacebook, String user_url_image) {
        this.user_name = user_name;
        this.user_IdFacebook = user_IdFacebook;
        this.user_url_image = user_url_image;
    }

    public User(String user_name, String user_email, String user_IdGoogle, String user_url_image) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_IdGoogle = user_IdGoogle;
        this.user_url_image = user_url_image;
    }

    //    public User(String user_name, String user_email, String user_url_image) {
//        this.user_name = user_name;
//        this.user_email = user_email;
//        this.user_url_image = user_url_image;
//    }

    public User(String user_email, String user_pass) {
        this.user_email = user_email;
        this.user_pass = user_pass;
    }

    public User(String user_name, String user_email, String user_IdGoogle, String user_IdFacebook, String user_pass, String user_url_image, boolean status_active) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_IdGoogle = user_IdGoogle;
        this.user_IdFacebook = user_IdFacebook;
        this.user_pass = user_pass;
        this.user_url_image = user_url_image;
        this.status_active = status_active;
    }

    public User(int user_id, String user_name, String user_email, String user_IdGoogle, String user_IdFacebook, String user_pass, String user_url_image, boolean status_active) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_IdGoogle = user_IdGoogle;
        this.user_IdFacebook = user_IdFacebook;
        this.user_pass = user_pass;
        this.user_url_image = user_url_image;
        this.status_active = status_active;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_IdGoogle() {
        return user_IdGoogle;
    }

    public void setUser_IdGoogle(String user_IdGoogle) {
        this.user_IdGoogle = user_IdGoogle;
    }

    public String getUser_IdFacebook() {
        return user_IdFacebook;
    }

    public void setUser_IdFacebook(String user_IdFacebook) {
        this.user_IdFacebook = user_IdFacebook;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_url_image() {
        return user_url_image;
    }

    public void setUser_url_image(String user_url_image) {
        this.user_url_image = user_url_image;
    }

    public boolean isStatus_active() {
        return status_active;
    }

    public void setStatus_active(boolean status_active) {
        this.status_active = status_active;
    }

    public boolean isValidData() {
        return !TextUtils.isEmpty(getUser_email())
                && Patterns.EMAIL_ADDRESS.matcher(getUser_email()).matches()
                && getUser_pass().length() >= 8;
    }
}
