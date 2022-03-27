package com.btlandroid.music.model;

//public class Playlist {
//}

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist implements Serializable {

    @SerializedName("idPlaylist")
    @Expose
    private String idPlaylist;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("hinh")
    @Expose
    private String hinh;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}