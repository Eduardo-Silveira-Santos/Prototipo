package com.eduardosantos.prototipo;

import android.os.Parcel;
import android.os.Parcelable;

public class Worker implements Parcelable {
    private final String name;
    private final String profession;
    private final double rating;
    private final String phoneNumber;
    private final String city;

    public Worker(String name, String profession, double rating, String phoneNumber, String city) {
        this.name = name;
        this.profession = profession;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }

    protected Worker(Parcel in) {
        name = in.readString();
        profession = in.readString();
        rating = in.readDouble();
        phoneNumber = in.readString();
        city = in.readString();
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
        dest.writeString(phoneNumber);
        dest.writeString(city);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }
}
