package com.example.thomas.sapiadvert;

/**
 * Struct to represent an advertisment in the firebase database
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */

public class  AdvertismentInDatabase {
    /**
     * ID of the advertiser
     */
    public String CreatedBy;

    /**
     * title of the advertisment
     */
    public String Title;

    /**
     * details of the advertisment
     */
    public String Details;

    /**
     * Uri to the main picture of the advertisment
     */
    public String MainPicture;

    /**
     * longitude information of the advertisment
     */
    public double Longitude;

    /**
     * latitude information of the advertisment
     */
    public double Latitude;

    /**
     * default consturctor
     */
    public AdvertismentInDatabase(){

    }

    /**
     * function to convert the advertisment to String, debugging purposes
     * @return string containing all necesary data about the advertisment
     */
    @Override
    public String toString() {
        return "AdvertismentInDatabase{" +
                "CreatedBy='" + CreatedBy + '\'' +
                ", Title='" + Title + '\'' +
                ", Details='" + Details + '\'' +
                ", MainPicture='" + MainPicture + '\'' +
                ", Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                '}';
    }

    /**
     *
     * @param CreatedBy Id of the advertiser
     * @param Title Title of the advertisment
     * @param Details Details of the advertisment
     * @param MainPicture Uri to the main picture of the advertisment
     * @param Longitude longitude information about the advertisment
     * @param Latitude latitude information about the advertisment
     */
    AdvertismentInDatabase(String CreatedBy,
                           String Title,
                           String Details,
                           String MainPicture,
                           double Longitude,
                           double Latitude){
        this.CreatedBy=CreatedBy;
        this.Title=Title;
        this.Details=Details;
        this.MainPicture=MainPicture;
        this.Longitude=Longitude;
        this.Latitude=Latitude;
    }

}
