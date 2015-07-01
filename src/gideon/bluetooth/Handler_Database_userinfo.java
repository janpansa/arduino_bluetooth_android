/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author gideon
 * 
 * --------------------------
 * Description
 * --------------------------
 * This class represents the layout of our user settings database.
 * This class also contains methods and queries which can be performed on
 * the settings database.
 * --------------------------
 * 
 */

public class Handler_Database_userinfo extends SQLiteOpenHelper
{
    //---------------------------
    //  Database Members
    //---------------------------
        private SQLiteDatabase database;
        
        // Database Version
        private static final int DATABASE_VERSION = 1;
        
        // Database Name
        private static final String DATABASE_NAME = "users";
    //---------------------------
        
    //---------------------------
    //  Tables   
    //---------------------------
        // Users table name
        private static final String TABLE_SETTINGS              = "users";
        
        // Users Table Columns names
        private static final String SETTINGS_ID                 = "id";
        private static final String SETTINGS_USERNAME           = "username";
        private static final String SETTINGS_PASSWORD           = "password";
        
   
    //---------------------------
        
    public Handler_Database_userinfo(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
         String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS
            + "("
                + SETTINGS_ID + " INTEGER PRIMARY KEY," 
                + SETTINGS_USERNAME + " TEXT," 
                + SETTINGS_PASSWORD + " TEXT"
             +")";
         
            db.execSQL(CREATE_SETTINGS_TABLE);
            String sql ="INSERT INTO settings (username, password) VALUES('user','password')" ;       
            db.execSQL(sql);
    }
    
            
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
     
        // Create tables again
        onCreate(db);
    }
   
     
    public Cursor getAllUserInfo()
    {
        return database.rawQuery("select * from " + TABLE_SETTINGS, null);
    }
    
    public void updateUserDetails(String username, String password, String pin)
    {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SETTINGS_USERNAME, username);
            contentValues.put(SETTINGS_PASSWORD, password);
            database.update(TABLE_SETTINGS, contentValues, SETTINGS_ID + " = " + "1", null);
    }
}