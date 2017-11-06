package biz.markgo.senior_project.tracksharelocations.DatabaseSqlLite;

/**
 * Created by macintosh on 7/17/2017 AD.
 */

public class Member {
    // Labels table name
    public static final String TABLE = "member";
    // Labels Table Columns names
    public static final String KEY_member_id = "member_id";
    public static final String KEY_account_id = "account_id";
    public static final String KEY_email = "email";
    public static final String KEY_FirstName = "FirstName";
    public static final String KEY_LastName = "LastName";
    public static final String KEY_picture_name = "picture_name";
    public static final String KEY_type_account = "type_account";
    public static final String KEY_follow_id = "follow_id";
    public static final String KEY_Token_key = "Token_key";

    // property help us to keep data
    public String member_id;
    public String account_id;
    public String email;
    public String FirstName;
    public String LastName;
    public String picture_name;
    public String type_account;
    public String follow_id;
    public String Token_key;
}
