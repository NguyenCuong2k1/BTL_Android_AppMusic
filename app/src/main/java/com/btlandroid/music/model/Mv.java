package com.btlandroid.music.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Mv implements Serializable {

    @SerializedName("idmv")
    @Expose
    private String idmv;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("hinh")
    @Expose
    private String hinh;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("tencasi")
    @Expose
    private String tencasi;
    @SerializedName("idtheloai")
    @Expose
    private String idtheloai;

    public String getIdmv() {
        return idmv;
    }

    public void setIdmv(String idmv) {
        this.idmv = idmv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTencasi() {
        return tencasi;
    }

    public void setTencasi(String tencasi) {
        this.tencasi = tencasi;
    }

    public String getIdtheloai() {
        return idtheloai;
    }
}

