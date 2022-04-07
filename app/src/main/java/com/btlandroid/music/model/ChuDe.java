package com.btlandroid.music.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChuDe implements Serializable {

    @SerializedName("idChuDe")
    @Expose
    private String idChuDe;
    @SerializedName("tenCD")
    @Expose
    private String tenChuDe;
    @SerializedName("hinhCD")
    @Expose
    private String hinhChuDe;

    public String getIdChuDe() {
        return idChuDe;
    }

    public void setIdChuDe(String idChuDe) {
        this.idChuDe = idChuDe;
    }

    public String getTenChuDe() {
        return tenChuDe;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }

    public String getHinhChuDe() {
        return hinhChuDe;
    }

    public void setHinhChuDe(String hinhChuDe) {
        this.hinhChuDe = hinhChuDe;
    }
}