package com.example.varun.vesica.models;

/**
 * Created by varun on 21/10/16.
 */
public class Contact {

    private String userName;
    private String displayName;
    private Boolean _isValidated;
    private Boolean userFound;

    public Boolean getUserFound() {
        return userFound;
    }

    public void setUserFound(Boolean userFound) {
        this.userFound = userFound;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void set_isValidated(Boolean _isValidated) {
        this._isValidated = _isValidated;
    }


    public Boolean get_isValidated() {
        return _isValidated;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }


}
