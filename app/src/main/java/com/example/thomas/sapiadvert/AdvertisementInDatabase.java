package com.example.thomas.sapiadvert;

/**
 * Struct to represent an advertisement in the firebase database
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */

public class AdvertisementInDatabase {
    /**
     * ID of the advertiser
     */
    public String CreatedBy;

    /**
     * title of the advertisement
     */
    public String Title;

    /**
     * details of the advertisement
     */
    public String Details;

    /**
     * Uri to the main picture of the advertisement
     */
    public String MainPicture;

    /**
     * longitude information of the advertisement
     */
    public double Longitude;

    /**
     * latitude information of the advertisement
     */
    public double Latitude;

    /**
     * default consturctor
     */
    public AdvertisementInDatabase(){

    }

    /**
     * function to convert the advertisement to String, debugging purposes
     * @return string containing all necesary data about the advertisement
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
     *
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
