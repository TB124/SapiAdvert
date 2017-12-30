package com.example.thomas.sapiadvert;

public class  AdvertismentInDatabase {
    public String CreatedBy;
    public String Title;
    public String Details;
    public String MainPicture;
    public double Longitude;
    public double Latitude;
    AdvertismentInDatabase(){

    }

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
