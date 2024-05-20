package com.eduardosantos.prototipo;

import android.os.Parcel;
import android.os.Parcelable;

public class Worker implements Parcelable {
    private String name;
    private String profession;
    private double rating;

    public Worker(String name, String profession, double rating) {
        this.name = name;
        this.profession = profession;
        this.rating = rating;
    }

    protected Worker(Parcel in) {
        name = in.readString();
        profession = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Worker> CREATOR = new Creator<Worker>() {
        @Override
        public Worker createFromParcel(Parcel in) {
            return new Worker(in);
        }

        @Override
        public Worker[] newArray(int size) {
            return new Worker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(profession);
        dest.writeDouble(rating);
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return name + " - " + profession;
    }
}
