package com.btlandroid.music.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuangCao implements Serializable {

    @SerializedName("idQC")
    @Expose
    private String idQuangCao;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("nd")
    @Expose
    private String noiDung;
    @SerializedName("idBH")
    @Expose
    private String idBaiHat;
    @SerializedName("name")
    @Expose
    private String nameBaiHat;
    @SerializedName("imageBH")
    @Expose
    private String imageBaiHat;

    public QuangCao() {
    }

    public QuangCao(String idQuangCao, String image, String noiDung, String idBaiHat, String nameBaiHat, String imageBaiHat) {
        this.idQuangCao = idQuangCao;
        this.image = image;
        this.noiDung = noiDung;
        this.idBaiHat = idBaiHat;
        this.nameBaiHat = nameBaiHat;
        this.imageBaiHat = imageBaiHat;
    }

    public String getIdQuangCao() {
        return idQuangCao;
    }

    public void setIdQuangCao(String idQuangCao) {
        this.idQuangCao = idQuangCao;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getNameBaiHat() {
        return nameBaiHat;
    }

    public void setNameBaiHat(String nameBaiHat) {
        this.nameBaiHat = nameBaiHat;
    }

    public String getImageBaiHat() {
        return imageBaiHat;
    }

    public void setImageBaiHat(String imageBaiHat) {
        this.imageBaiHat = imageBaiHat;
    }
}