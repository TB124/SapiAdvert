package com.example.thomas.sapiadvert;
/**
 * This class will contain all the data of a user.
 */
public class UserInDatabase {
    public String EmailAddress;
    public String FirstName;
    public String LastName;
    public String PhoneNumber;
    public String ProfilePicture;

    /**
     * Empty Constructor of the class
     */
    UserInDatabase(){};

    /**
     * Setter constructor.
     * @param EmailAddress email address
     * @param FirstName first name
     * @param LastName last name
     * @param PhoneNumber phone number
     * @param ProfilePicture profile picture URI
     */
    UserInDatabase(String EmailAddress, String FirstName, String LastName, String PhoneNumber, String ProfilePicture){
        this.EmailAddress = EmailAddress;
        this.FirstName = FirstName;
        this.LastName= LastName;
        this.PhoneNumber = PhoneNumber;
        this.ProfilePicture = ProfilePicture;
    };
}
