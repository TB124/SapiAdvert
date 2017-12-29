package com.example.thomas.sapiadvert;

import android.os.Parcel;
import android.os.Parcelable;

public class AdvertismentMy implements Parcelable {
    private String title;
    private String details;
    private String mainPictureUri;
    private double longitude;
    private double latitude;

    protected AdvertismentMy(Parcel in) {
        title = in.readString();
        details = in.readString();
        mainPictureUri = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<AdvertismentMy> CREATOR = new Creator<AdvertismentMy>() {
        @Override
        public AdvertismentMy createFromParcel(Parcel in) {
            return new AdvertismentMy(in);
        }

        @Override
        public AdvertismentMy[] newArray(int size) {
            return new AdvertismentMy[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public AdvertismentMy(String title, String details, String mainPictureUri, double longitude, double latitude) {
        this.title = title;
        this.details = details;
        this.mainPictureUri = mainPictureUri;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMainPictureUri() {
        return mainPictureUri;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "AdvertismentMy{" +
                "title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", mainPictureUri='" + mainPictureUri + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(mainPictureUri);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
