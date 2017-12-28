package com.example.thomas.sapiadvert;

/**
 * Created by Szabi on 2017. 12. 24..
 */

public class  AdvertismentInDatabase {
    public String CreatedBy;
    public String Title;
    public String Details;
    public String MainPicture;
    public double Longitude;
    public double Latitude;
    AdvertismentInDatabase(){

    }
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
