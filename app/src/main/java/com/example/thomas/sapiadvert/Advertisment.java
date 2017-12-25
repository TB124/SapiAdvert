package com.example.thomas.sapiadvert;

/**
 * Created by Szabi on 2017. 12. 24..
 */

public class Advertisment {
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public void setMainPictureUri(String mainPictureUri) {
        this.mainPictureUri = mainPictureUri;
    }

    private String title;
    private String details;
    private String createdBy;
    private String profilePictureUri;
    private String mainPictureUri;
    public Advertisment(String title, String details, String createdBy, String profilePictureUri, String mainPictureUri) {
        this.title = title;
        this.details = details;
        this.createdBy = createdBy;
        this.profilePictureUri = profilePictureUri;
        this.mainPictureUri = mainPictureUri;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public String getMainPictureUri() {
        return mainPictureUri;
    }
}
