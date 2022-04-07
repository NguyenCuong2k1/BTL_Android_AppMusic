package com.btlandroid.music.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TheLoaiTrongNgay {

    @SerializedName("TheLoai")
    @Expose
    private List<TheLoai> listTheLoai = null;
    @SerializedName("ChuDe")
    @Expose
    private List<ChuDe> listChuDe = null;

    public List<TheLoai> getListTheLoai() {
        return listTheLoai;
    }

    public void setListTheLoai(List<TheLoai> listTheLoai) {
        this.listTheLoai = listTheLoai;
    }

    public List<ChuDe> getListChuDe() {
        return listChuDe;
    }

    public void setListChuDe(List<ChuDe> listChuDe) {
        this.listChuDe = listChuDe;
    }
}