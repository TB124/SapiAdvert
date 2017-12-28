package com.example.thomas.sapiadvert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Szabi on 2017. 12. 24..
 */

public class Advertisment implements Parcelable {

    public Advertisment (){

    }
    protected Advertisment(Parcel in) {
        title = in.readString();
        details = in.readString();
        createdBy = in.readString();
        profilePictureUri = in.readString();
        mainPictureUri = in.readString();
        longitude=Double.parseDouble(in.readString());
        latitude=Double.parseDouble(in.readString());
    }

    public static final Creator<Advertisment> CREATOR = new Creator<Advertisment>() {
        @Override
        public Advertisment createFromParcel(Parcel in) {
            return new Advertisment(in);
        }

        @Override
        public Advertisment[] newArray(int size) {
            return new Advertisment[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }

    private String title;
    private String details;
    private String createdBy;
    private String profilePictureUri;
    private String mainPictureUri;
    private double longitude;
    private double latitude;
    public Advertisment(String title,
                        String details,
                        String createdBy,
                        String profilePictureUri,
                        String mainPictureUri,
                        double longitude,
                        double latitude) {
        this.title = title;
        this.details = details;
        this.createdBy = createdBy;
        this.profilePictureUri = profilePictureUri;
        this.mainPictureUri = mainPictureUri;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public String getMainPictureUri() {
        return mainPictureUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(details);
        parcel.writeString(createdBy);
        parcel.writeString(profilePictureUri);
        parcel.writeString(mainPictureUri);
        parcel.writeString(Double.toString(longitude));
        parcel.writeString(Double.toString(latitude));
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
