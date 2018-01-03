package com.example.thomas.sapiadvert;

/**
 * Struct to represent an advertisement in the firebase database
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */

public class AdvertisementInDatabase {

    public String CreatedBy;
    public String Title;
    public String Details;
    public String MainPicture;
    public double Longitude;
    public double Latitude;

    /**
     * default constructor
     */
    public AdvertisementInDatabase(){

    }

    /**
     * function to convert the advertisement to String, debugging purposes
     * @return string containing all necessary data about the advertisement
     */
    @Override
    public String toString() {
        return "AdvertisementInDatabase{" +
                "CreatedBy='" + CreatedBy + '\'' +
                ", Title='" + Title + '\'' +
                ", Details='" + Details + '\'' +
                ", MainPicture='" + MainPicture + '\'' +
                ", Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                '}';
    }

    /**
     * Setter constructor
     * @param CreatedBy Id of the advertiser
     * @param Title Title of the advertisement
     * @param Details Details of the advertisement
     * @param MainPicture Uri to the main picture of the advertisement
     * @param Longitude longitude information about the advertisement
     * @param Latitude latitude information about the advertisement
     */
    AdvertisementInDatabase(String CreatedBy,
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
