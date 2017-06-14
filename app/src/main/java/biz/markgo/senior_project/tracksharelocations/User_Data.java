package biz.markgo.senior_project.tracksharelocations;

import android.content.Intent;

public class User_Data {
    private static String statusLogin;
    private static String FirstName;
    private static String LastName;
    private static String personPhotoUrl;
    private static String email;
    private static String account_id;

    public static void setStatusLogin(String statusLogin) {
        User_Data.statusLogin = statusLogin;
    }

    public static void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public static void setLastName(String lastName) {
        LastName = lastName;
    }

    public static void setPersonPhotoUrl(String personPhotoUrl) {
        User_Data.personPhotoUrl = personPhotoUrl;
    }

    public static void setEmail(String email) {
        User_Data.email = email;
    }

    public static void setAccount_id(String account_id) {
        User_Data.account_id = account_id;
    }

    public static String getStatusLogin() {
        return statusLogin;
    }

    public static String getFirstName() {
        return FirstName;
    }

    public static String getLastName() {
        return LastName;
    }

    public static String getPersonPhotoUrl() {
        return personPhotoUrl;
    }

    public static String getEmail() {
        return email;
    }

    public static String getAccount_id() {
        return account_id;
    }



}
