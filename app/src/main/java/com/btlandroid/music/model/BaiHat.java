package com.btlandroid.music.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaiHat implements Parcelable {

    @SerializedName("idBaiHat")
    @Expose
    private String idBaiHat;
    @SerializedName("tenBaiHat")
    @Expose
    private String tenBaiHat;
    @SerializedName("hinhBaiHat")
    @Expose
    private String imageBaiHat;
    @SerializedName("caSi")
    @Expose
    private String singer;
    @SerializedName("linkBaiHat")
    @Expose
    private String linkBaiHat;
    @SerializedName("luotThich")
    @Expose
    private String luotThich;

    protected BaiHat(Parcel in) {
        idBaiHat = in.readString();
        tenBaiHat = in.readString();
        imageBaiHat = in.readString();
        singer = in.readString();
        linkBaiHat = in.readString();
        luotThich = in.readString();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>() {
        @Override
        public BaiHat createFromParcel(Parcel in) {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size) {
            return new BaiHat[size];
        }
    };

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getImageBaiHat() {
        return imageBaiHat;
    }

    public void setImageBaiHat(String imageBaiHat) {
        this.imageBaiHat = imageBaiHat;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public String getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(String luotThich) {
        this.luotThich = luotThich;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(imageBaiHat);
        dest.writeString(singer);
        dest.writeString(linkBaiHat);
        dest.writeString(luotThich);
    }
}