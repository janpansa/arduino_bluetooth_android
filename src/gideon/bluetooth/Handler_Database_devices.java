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
import java.util.ArrayList;

/**
 *
 * @author gideon
 * 
 * --------------------------
 * Description
 * --------------------------
 * This class represents the layout of our devices database.
 * This class also contains methods and queries which can be performed on
 * the devices database.
 * --------------------------
 * 
 */

public class Handler_Database_devices extends SQLiteOpenHelper
{
    //---------------------------
    //  Database Members
    //---------------------------
        private SQLiteDatabase database;
        
        // Database Version
        private static final int DATABASE_VERSION = 1;
        
        // Database Name
        private static final String DATABASE_NAME = "devices";
    //---------------------------
        
    //---------------------------
    //  Tables   
    //---------------------------
        
        private static final String TABLE_BLUETOOTH_DEVICES     = "bluetoothdevices";
        
        private static final String DEVICE_ID                   = "id";
        private static final String DEVICE_ADDRESS              = "address";
        private static final String DEVICE_NAME1                = "name1";
        private static final String DEVICE_NAME2                = "name2";
        private static final String DEVICE_ISDEFAULT            = "isdefault";

    //---------------------------
        
    public Handler_Database_devices(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_BLUETOOTH_DEVICES_TABLE = "CREATE TABLE " + TABLE_BLUETOOTH_DEVICES
         + "("
             + DEVICE_ID + " INTEGER PRIMARY KEY," 
             + DEVICE_ADDRESS + " TEXT NOT NULL DEFAULT '',"
             + DEVICE_NAME1 + " TEXT NOT NULL DEFAULT '',"
             + DEVICE_NAME2 + " TEXT NOT NULL DEFAULT '',"
             + DEVICE_ISDEFAULT + " TEXT NOT NULL DEFAULT ''"
         +")";
        
         db.execSQL(CREATE_BLUETOOTH_DEVICES_TABLE);
    }
     
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH_DEVICES);
     
        // Create tables again
        onCreate(db);
    }
   
    public Cursor getAllDevicesInfo()
    {
        return database.rawQuery("select * from " + TABLE_BLUETOOTH_DEVICES, null);
    }
    
    public Bluetooth_Light_Controller getDevices()
    {
        Cursor cursor = database.rawQuery("select * from " + TABLE_BLUETOOTH_DEVICES, null);
        
        //Device Members
        ArrayList<String> arraylist_device_id = new ArrayList<String>();
        ArrayList<String> arraylist_device_address = new ArrayList<String>();
        ArrayList<String> arraylist_device_name_official = new ArrayList<String>();
        ArrayList<String> arraylist_device_name_unofficial = new ArrayList<String>();
        ArrayList<String> arraylist_device_isdefault = new ArrayList<String>();
 
        if(cursor.moveToFirst())
        {
            do
            {
                int _DEVICE_ID = cursor.getColumnIndex(DEVICE_ID);
                int _DEVICE_ADDRESS = cursor.getColumnIndex(DEVICE_ADDRESS);
                int _DEVICE_NAME1 = cursor.getColumnIndex(DEVICE_NAME1);
                int _DEVICE_NAME2 = cursor.getColumnIndex(DEVICE_NAME2);
                int _DEVICE_ISDEFAULT = cursor.getColumnIndex(DEVICE_ISDEFAULT);
               
                String  DEVICE_IDIndexentryid = cursor.getString(_DEVICE_ID);
                String  DEVICE_ADDRESSIndexentryid = cursor.getString(_DEVICE_ADDRESS);
                String  DEVICE_NAME1Indexentryid = cursor.getString(_DEVICE_NAME1);
                String  DEVICE_NAME2Indexentryid = cursor.getString(_DEVICE_NAME2);
                String  DEVICE_ISDEFAULTIndexentryid = cursor.getString(_DEVICE_ISDEFAULT);
                
                arraylist_device_id.add(DEVICE_IDIndexentryid);  
                arraylist_device_address.add(DEVICE_ADDRESSIndexentryid);                        
                arraylist_device_name_official.add(DEVICE_NAME1Indexentryid);                           
                arraylist_device_name_unofficial.add(DEVICE_NAME2Indexentryid); 
                arraylist_device_isdefault.add(DEVICE_ISDEFAULTIndexentryid);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        
        Bluetooth_Light_Controller device = new Bluetooth_Light_Controller(
            arraylist_device_id,
            arraylist_device_address,
            arraylist_device_name_official,
            arraylist_device_name_unofficial,
            arraylist_device_isdefault
        );
        
        return device;
    }
    
    public int getDevicesAmount()
    {
        Cursor cursor = database.rawQuery("select id from " + TABLE_BLUETOOTH_DEVICES, null);
        
       int count = 0;
        if(cursor.moveToFirst())
        {
            do
            {
               count++;
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return count;
    }
    
    public void addDevice(
        String device_address,
        String device_name_official,
        String device_name_unofficial,
        String device_isdefault
        )
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_ADDRESS,device_address);
        contentValues.put(DEVICE_NAME1,device_name_official);
        contentValues.put(DEVICE_NAME2,device_name_unofficial);
        contentValues.put(DEVICE_ISDEFAULT,device_isdefault);
        database.insert(TABLE_BLUETOOTH_DEVICES, null, contentValues);
    }
    
    public void deleteDevice(String id)
    {
        database.delete(TABLE_BLUETOOTH_DEVICES, DEVICE_ID + " = " + id, null);
    }
    
    public void updateDeviceName(String id,String newName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_NAME2, newName);
        database.update(TABLE_BLUETOOTH_DEVICES, contentValues, DEVICE_ID + " = " + id, null);
    }
    
    public void setDeviceDefault(String id)
    {
        ContentValues contentValuesOLD = new ContentValues();
        contentValuesOLD.put(DEVICE_ISDEFAULT,"NO");
        database.update(TABLE_BLUETOOTH_DEVICES, contentValuesOLD, DEVICE_ISDEFAULT + " = \"YES\"", null);
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_ISDEFAULT ,"YES");
        database.update(TABLE_BLUETOOTH_DEVICES, contentValues, DEVICE_ID + " = " + id, null);
    }
    
    public String getDefaultDevice()
    {
        //Device Address
        String address = "";
        
        Cursor cursor = database.rawQuery("select * from " + TABLE_BLUETOOTH_DEVICES + " WHERE " + DEVICE_ISDEFAULT + " = \"YES\"", null);
 
        if(cursor.moveToFirst())
        {
            do
            {
                address = cursor.getString(cursor.getColumnIndex(DEVICE_ADDRESS));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        
        return address;
    }
    
    public String getDefaultDeviceName()
    {
        //Device Address
        String name = "";
        
        Cursor cursor = database.rawQuery("select * from " + TABLE_BLUETOOTH_DEVICES + " WHERE " + DEVICE_ISDEFAULT + " = \"YES\"", null);
 
        if(cursor.moveToFirst())
        {
            do
            {
                if("".equals(cursor.getString(cursor.getColumnIndex(DEVICE_NAME2)))||cursor.getString(cursor.getColumnIndex(DEVICE_NAME2)).length()==0)
                {
                    name = cursor.getString(cursor.getColumnIndex(DEVICE_NAME1));
                }
                else
                {
                    name = cursor.getString(cursor.getColumnIndex(DEVICE_NAME2));
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        
        return name;
    }
}