package com.example.thomas.sapiadvert;

import android.os.Parcel;
import android.os.Parcelable;


/**
 *The Advertisement class is used for represent an advertisement in the MainActivity
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */

public class Advertisement implements Parcelable {

    private String title;
    private String details;
    private String createdBy;
    private String profilePictureUri;
    private String mainPictureUri;
    private double longitude;
    private double latitude;

    /**
     * default constructor
     */
    public Advertisement(){}


    /**
     * Constructor, creating a class from a parcel(retrieving advertisement from intent)
     * @param in parcel
     */
    protected Advertisement(Parcel in) {
        title = in.readString();
        details = in.readString();
        createdBy = in.readString();
        profilePictureUri = in.readString();
        mainPictureUri = in.readString();
        longitude=Double.parseDouble(in.readString());
        latitude=Double.parseDouble(in.readString());
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
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
     * setter for changing the advertiser ID
     * @param createdBy new advertiser ID
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * setter for chaning the URi to the profile picture
     * @param profilePictureUri new URi to the
     */
    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    /**
     * setter for chaning the Uri to the main picture of the advertisement
     * @param mainPictureUri new Uri to the main picture of the advertisement
     */
    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }


    /**
     * Setter constructor
     * @param title title
     * @param details details
     * @param createdBy ID of the advertiser
     * @param profilePictureUri Uri to the profile picture of the advertiser
     * @param mainPictureUri Uri to the main picture of the advertisement
     * @param longitude longitude information
     * @param latitude latiude information
     */
    public Advertisement(String title,
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

    /**
     * getter for the title of advertisement
     * @return the title of the advertisement
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for the details of the advertisement
     * @return the details of the advertisement
     */
    public String getDetails() {
        return details;
    }

    /**
     * getter for  ID of the advertiser
     * @return ID of the advertiser
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * getter for Uri of the profile picture of the advertiser
     * @return Uri to the profile picture of the advertiser
     */
    public String getProfilePictureUri() {
        return profilePictureUri;
    }


    /**
     * getter for the Uri of the main picture of the advertisement
     * @return Uri to the main picture of the advertisement
     */
    public String getMainPictureUri() {
        return mainPictureUri;
    }

    /**
     * getter for the longitude information
     * @return longitude information about the advertisement
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * getter for the latitude information
     * @return latitude information about the advertisement
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * dummy
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * function to write the advertisement into parcel
     * @param parcel destination
     * @param i flag
     */
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

}
