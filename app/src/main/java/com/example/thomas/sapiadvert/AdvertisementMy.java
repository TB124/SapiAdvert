package com.example.thomas.sapiadvert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to represent an advertisement created by the user
 */
public class AdvertisementMy implements Parcelable {
    private String title;
    private String details;
    private String mainPictureUri;
    private double longitude;
    private double latitude;

    /**
     * funtion to create an advertisement from a parcel (creating advertisement from intent)
     * @param in parcel
     */
    protected AdvertisementMy(Parcel in) {
        title = in.readString();
        details = in.readString();
        mainPictureUri = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<AdvertisementMy> CREATOR = new Creator<AdvertisementMy>() {
        @Override
        public AdvertisementMy createFromParcel(Parcel in) {
            return new AdvertisementMy(in);
        }

        @Override
        public AdvertisementMy[] newArray(int size) {
            return new AdvertisementMy[size];
        }
    };

    /**
     * setter for changing tittle
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * setter for changin details
     * @param details new details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * setter for chaning the Uri to the main picture of the advertisement
     * @param mainPictureUri new Uri to the main picture of the advertisement
     */
    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }

    /**
     * setter for longitude information
     * @param longitude new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * setter for latitude information
     * @param latitude new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Constructor
     * @param title title of the advertisement
     * @param details detials of the advertisement
     * @param mainPictureUri uri to the main picture of the advertisement
     * @param longitude longitude information about the advertisement
     * @param latitude latitude information about the advertisement
     */
    public AdvertisementMy(String title, String details, String mainPictureUri, double longitude, double latitude) {
        this.title = title;
        this.details = details;
        this.mainPictureUri = mainPictureUri;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * getter for the latitude information
     * @return latitude information about the advertisement
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * getter for the longitude information
     * @return longitude information about the advertisement
     */
    public double getLongitude() {
        return longitude;
    }

    public String getMainPictureUri() {
        return mainPictureUri;
    }

    /**
     * getter for the details of the advertisement
     * @return the details of the advertisement
     */
    public String getDetails() {
        return details;
    }

    /**
     * getter for the title of advertisement
     * @return the title of the advertisement
     */
    public String getTitle() {
        return title;
    }

    /**
     * converting the advertisement to String for debugging purposess
     * @return return the String representing the advertisement
     */
    @Override
    public String toString() {
        return "AdvertisementMy{" +
                "title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", mainPictureUri='" + mainPictureUri + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    /**
     * dummy
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * writing the advertisement into a parcel ( sending in intent)
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(mainPictureUri);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
