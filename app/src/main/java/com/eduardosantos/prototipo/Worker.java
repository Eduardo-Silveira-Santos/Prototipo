package com.eduardosantos.prototipo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Worker implements Parcelable {
    private final String name;
    private final String email;
    private final String profession;
    private final double rating;
    private final String phoneNumber;
    private final String city;
    private Bitmap profileImage;

    // Construtor completo
    public Worker(String name, String email, String profession, double rating, String phoneNumber, String city) {
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }

    // Construtor para o Parcelable
    protected Worker(Parcel in) {
        name = in.readString();
        email = in.readString();
        profession = in.readString();
        rating = in.readDouble();
        phoneNumber = in.readString();
        city = in.readString();
        profileImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    // Implementação do Parcelable.Creator
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

    // Métodos Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(profession);
        dest.writeDouble(rating);
        dest.writeString(phoneNumber);
        dest.writeString(city);
        dest.writeParcelable(profileImage, flags);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfession() {
        return profession;
    }

    public double getRating() {
        return rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    // toString para uma representação textual
    @Override
    public String toString() {
        return name + " - " + profession;
    }
}
