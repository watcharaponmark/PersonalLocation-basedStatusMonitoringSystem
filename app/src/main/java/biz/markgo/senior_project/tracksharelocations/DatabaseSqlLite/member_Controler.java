package biz.markgo.senior_project.tracksharelocations.DatabaseSqlLite;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import java.util.ArrayList;
    import java.util.HashMap;

    public class Member_Controler {
        private DBHelper dbHelper;

        public Member_Controler(Context context) {
            dbHelper = new DBHelper(context);
        }


        public int insert(Member member) {

            //Open connection to write data
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Member.KEY_member_id, member.member_id);
            values.put(Member.KEY_account_id,member.account_id);
            values.put(Member.KEY_email, member.email);
            values.put(Member.KEY_FirstName, member.FirstName);
            values.put(Member.KEY_LastName,member.LastName);
            values.put(Member.KEY_picture_name, member.picture_name);
            values.put(Member.KEY_type_account, member.type_account);
            values.put(Member.KEY_follow_id,member.follow_id);
            values.put(Member.KEY_Token_key, member.Token_key);

            // Inserting Row
            long member_Id = db.insert(Member.TABLE, null, values);
            db.close(); // Closing database connection
            return (int) member_Id;
        }

        public void delete(String member_Id) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // It's a good practice to use parameter ?, instead of concatenate string
            db.delete(Member.TABLE, Member.KEY_member_id + "= ?", new String[] { String.valueOf(member_Id) });
            db.close(); // Closing database connection
        }

        public void update(Member member) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Member.KEY_member_id, member.member_id);
            values.put(Member.KEY_account_id,member.account_id);
            values.put(Member.KEY_email, member.email);
            values.put(Member.KEY_FirstName, member.FirstName);
            values.put(Member.KEY_LastName,member.LastName);
            values.put(Member.KEY_picture_name, member.picture_name);
            values.put(Member.KEY_type_account, member.type_account);
            values.put(Member.KEY_follow_id,member.follow_id);
            values.put(Member.KEY_Token_key, member.Token_key);

            // It's a good practice to use parameter ?, instead of concatenate string
            db.update(Member.TABLE, values, Member.KEY_member_id + "= ?", new String[] { String.valueOf(member.member_id) });
            db.close(); // Closing database connection
        }

        public ArrayList<HashMap<String, String>>  getmemberList() {
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    Member.KEY_member_id + "," +
                    Member.KEY_account_id + "," +
                    Member.KEY_email + "," +
                    Member.KEY_FirstName + "," +
                    Member.KEY_LastName + "," +
                    Member.KEY_picture_name + "," +
                    Member.KEY_type_account + "," +
                    Member.KEY_follow_id + "," +
                    Member.KEY_Token_key +
                    " FROM " + Member.TABLE;


            ArrayList<HashMap<String, String>> memberList = new ArrayList<HashMap<String, String>>();

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> member = new HashMap<String, String>();
                    member.put("member_id", cursor.getString(cursor.getColumnIndex(Member.KEY_member_id)));
                    member.put("account_id", cursor.getString(cursor.getColumnIndex( Member.KEY_account_id)));
                    member.put("email", cursor.getString(cursor.getColumnIndex(Member.KEY_email)));
                    member.put("FirstName", cursor.getString(cursor.getColumnIndex( Member.KEY_FirstName)));
                    member.put("LastName", cursor.getString(cursor.getColumnIndex(Member.KEY_LastName)));
                    member.put("picture_name", cursor.getString(cursor.getColumnIndex( Member.KEY_picture_name)));
                    member.put("type_account", cursor.getString(cursor.getColumnIndex(Member.KEY_type_account)));
                    member.put("follow_id", cursor.getString(cursor.getColumnIndex( Member.KEY_follow_id)));
                    member.put("Token_key", cursor.getString(cursor.getColumnIndex(Member.KEY_Token_key)));

                    memberList.add(member);


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return memberList;

        }

        public Member getMemberById(String member_id){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    Member.KEY_member_id + "," +
                    Member.KEY_account_id + "," +
                    Member.KEY_email + "," +
                    Member.KEY_FirstName + "," +
                    Member.KEY_LastName + "," +
                    Member.KEY_picture_name + "," +
                    Member.KEY_type_account + "," +
                    Member.KEY_follow_id + "," +
                    Member.KEY_Token_key +
                    " FROM " + Member.TABLE
                    + " WHERE " +
                    Member.KEY_member_id + "=?";// It's a good practice to use parameter ?, instead of concatenate string

            int iCount =0;
            Member member = new Member();

            Cursor cursor = db.rawQuery(selectQuery, new String[]{member_id});

            if (cursor.moveToFirst()) {
                do {
                    //member.member_id = cursor.getInt(cursor.getColumnIndex(Member.KEY_member_id));
                    member.member_id = cursor.getString(cursor.getColumnIndex(Member.KEY_member_id));
                    member.account_id =cursor.getString(cursor.getColumnIndex(Member.KEY_account_id));
                    member.email  = cursor.getString(cursor.getColumnIndex(Member.KEY_email));
                    member.FirstName = cursor.getString(cursor.getColumnIndex(Member.KEY_FirstName));
                    member.LastName = cursor.getString(cursor.getColumnIndex(Member.KEY_LastName));
                    member.picture_name =cursor.getString(cursor.getColumnIndex(Member.KEY_picture_name));
                    member.type_account  =cursor.getString(cursor.getColumnIndex(Member.KEY_type_account));
                    member.follow_id =cursor.getString(cursor.getColumnIndex(Member.KEY_follow_id));
                    member.Token_key =cursor.getString(cursor.getColumnIndex(Member.KEY_Token_key));


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return member;
        }

    }
