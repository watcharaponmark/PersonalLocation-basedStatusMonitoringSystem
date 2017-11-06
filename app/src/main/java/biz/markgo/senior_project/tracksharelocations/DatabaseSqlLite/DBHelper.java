package biz.markgo.senior_project.tracksharelocations.DatabaseSqlLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "pdb1.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_MEMBER = "CREATE TABLE " + Member.TABLE  + "("
                + Member.KEY_member_id  + "TEXT, "
                + Member.KEY_account_id + " TEXT, "
                + Member.KEY_email + " TEXT, "
                + Member.KEY_FirstName + " TEXT, "
                + Member.KEY_LastName + " TEXT, "
                + Member.KEY_picture_name + " TEXT, "
                + Member.KEY_type_account + " TEXT, "
                + Member.KEY_follow_id + " TEXT, "
                + Member.KEY_Token_key + " TEXT )";
        db.execSQL(CREATE_TABLE_MEMBER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Member.TABLE);

        // Create tables again
        onCreate(db);

    }

}