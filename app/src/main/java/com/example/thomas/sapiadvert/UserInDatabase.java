package com.example.thomas.sapiadvert;

import android.provider.ContactsContract;

/**
 * Created by Szabi on 2017. 12. 24..
 */

public class UserInDatabase {
    public String EmailAddress;
    public String FirstName;
    public String LastName;
    public String PhoneNumber;
    public String ProfilePicture;
    UserInDatabase(){};
    UserInDatabase(String EmailAddress, String FirstName, String LastName, String PhoneNumber, String ProfilePicture){
        this.EmailAddress = EmailAddress;
        this.FirstName = FirstName;
        this.LastName= LastName;
        this.PhoneNumber = PhoneNumber;
        this.ProfilePicture = ProfilePicture;
    };
}
