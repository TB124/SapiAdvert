package com.example.thomas.sapiadvert;

import android.os.Parcel;
import android.os.Parcelable;


/**
* The Advertisment class is used for represent an advertisment in the MainActivity
 * @author      Bondor Tamas
 * @author      Kovacs Szabolcs
 */

public class Advertisment implements Parcelable {


    /**
     *  title of the advertisment
     */
    private String title;

    /**
    * Details of the advertisment
     */
    private String details;

    /**
     * The ID of advertiser
     */
    private String createdBy;

    /**
     * Url to the profile picture of the advertiser
     */
    private String profilePictureUri;

    /**
     * Url to the main pictore of advertisment
     */
    private String mainPictureUri;

    /**
     * longitud information about the avertisment
     */
    private double longitude;

    /**
     * latitude information about the advertisment
     */
    private double latitude;

    /**
     * detault constructor
     */
    public Advertisment (){

    }


    /**
     * Constructor, creating a class from a parcel(retreiving advertisment from intent)
     * @param in parcel
     */
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
     * setter for chaning the Uri to the main picture of the advertisment
     * @param mainPictureUri new Uri to the main picture of the advertisment
     */
    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }


    /**
     * Contructor for creating an advertisment
     * @param title title
     * @param details details
     * @param createdBy ID of the advertiser
     * @param profilePictureUri Uri to the profile picture of the advertiser
     * @param mainPictureUri Uri to the main picture of the advertisment
     * @param longitude longitude information
     * @param latitude latiude information
     */
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

    /**
     * getter for the title of advertisment
     * @return the title of the advertisment
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for the details of the advertisment
     * @return the details of the advertisment
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
     * getter for the Uri of the main picture of the advertisment
     * @return Uri to the main picture of the advertisment
     */
    public String getMainPictureUri() {
        return mainPictureUri;
    }

    /**
     * getter for the longitude information
     * @return longitude information about the advertisment
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * getter for the latitude information
     * @return latitude information about the advertisment
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * dummy
     * @return dummy
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * function to write the advertisment into parcel
     * @param parcel
     * @param i
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
