package biz.markgo.senior_project.tracksharelocations.Data;

public class MemberInformation {
    private static  String member_id;
    private static String account_id;
    private static String email;
    private static String FirstName;
    private static String LastName;
    private static String picture_name;
    private static String type_account;
    private static String follow_id;
    private static String Token_key;

    public static String getMember_id() {
        return member_id;
    }

    public static void setMember_id(String member_id) {
        MemberInformation.member_id = member_id;
    }

    public static String getAccount_id() {
        return account_id;
    }

    public static void setAccount_id(String account_id) {
        MemberInformation.account_id = account_id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        MemberInformation.email = email;
    }

    public static String getFirstName() {
        return FirstName;
    }

    public static void setFirstName(String firstName) {
        MemberInformation.FirstName = firstName;
    }

    public static String getLastName() {
        return LastName;
    }

    public static void setLastName(String lastName) {
        MemberInformation.LastName = lastName;
    }

    public static String getPicture_name() {
        return picture_name;
    }

    public static void setPicture_name(String picture_name) {
        MemberInformation.picture_name = picture_name;
    }

    public static String getType_account() {
        return type_account;
    }

    public static void setType_account(String type_account) {
        MemberInformation.type_account = type_account;
    }

    public static String getFollow_id() {
        return follow_id;
    }

    public static void setFollow_id(String follow_id) {
        MemberInformation.follow_id = follow_id;
    }

    public static String getToken_key() {
        return Token_key;
    }

    public static void setToken_key(String token_key) {
        MemberInformation.Token_key = token_key;
    }


}
