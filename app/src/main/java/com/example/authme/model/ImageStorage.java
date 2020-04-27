package com.example.authme.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageStorage implements Parcelable {
    String name;
    String uri;

    public ImageStorage() {
    }

    public ImageStorage(String uri) {
        this.uri = uri;
    }


    protected ImageStorage(Parcel in) {
        name = in.readString();
        uri = in.readString();
    }

    public static final Creator<ImageStorage> CREATOR = new Creator<ImageStorage>() {
        @Override
        public ImageStorage createFromParcel(Parcel in) {
            return new ImageStorage(in);
        }

        @Override
        public ImageStorage[] newArray(int size) {
            return new ImageStorage[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(uri);
    }
}
