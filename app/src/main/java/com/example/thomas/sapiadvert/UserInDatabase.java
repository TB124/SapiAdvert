package com.example.thomas.sapiadvert;

public class UserInDatabase {
    public String EmailAddress;
    public String FirstName;
    public String LastName;
    public String PhoneNumber;
    public String ProfilePicture;
    public UserInDatabase(){};
    public UserInDatabase(String EmailAddress, String FirstName, String LastName, String PhoneNumber, String ProfilePicture){
        this.EmailAddress = EmailAddress;
        this.FirstName = FirstName;
        this.LastName= LastName;
        this.PhoneNumber = PhoneNumber;
        this.ProfilePicture = ProfilePicture;
    };
}
