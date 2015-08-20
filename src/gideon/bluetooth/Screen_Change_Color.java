/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.os.Bundle;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.service.textservice.SpellCheckerService.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import static gideon.bluetooth.R.id.checkBox;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gideon
 */
public class Screen_Change_Color extends Fragment
{
    //Bluetooth Settings
    public static String mcnumscanner;
    private Handler handlerUI;
    Connect c;
    Handler_Database_devices dbHelper;

    Boolean thread1started = false;
        
        
    public Screen_Change_Color(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Bluetooth stuff hier, ek weet, baie deurmekaar op die oomblik jammer
        
        //Initialize Bluetooth devices
        dbHelper = new Handler_Database_devices(this.getActivity());
        String scannerAddress = dbHelper.getDefaultDevice();
        String scannerName = dbHelper.getDefaultDeviceName();
        dbHelper.close();
        
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what==555)
                {
                    //Connected, waiting for Read
                    super.handleMessage(msg);
                }
            }
        };
        
         //Kry eers die Bluetooth Adapter vir die device.
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
        } 
        //Maak seker die bluetooth is aan.
        if (!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();

            //Wait untill it is enabled before proceeding...
            while(!mBluetoothAdapter.isEnabled())
            {

            }
        }
                        
        try
        {
            c = new Connect(mBluetoothAdapter.getRemoteDevice(scannerAddress),handler);
            c.start();
            thread1started = true;
            Toast.makeText(this.getActivity(), "Connected to "+scannerAddress, Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
        }
       
        
        //Verander die Title van die Fragment/Activity
        getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[2]);
        
        //Inflate die fragment met sy layout.
        View rootView = inflater.inflate(R.layout.screen_change_color, container, false);
        
        //nadat hy inflate het, dan kan ons die linear layout se width en height kry.
        LinearLayout llcolor = (LinearLayout) rootView.findViewById(R.id.llcolor);
        
        LinearLayout colorselection = (LinearLayout) rootView.findViewById(R.id.colorselection);
        
        CheckBox livepreview = (CheckBox) rootView.findViewById(checkBox);
        
        //so kom ons maak eers die ColorView
        ColorView cv = new ColorView(this.getActivity(),colorselection,livepreview);
        
        //nou add ons dit in die view in en ons se dit moet die parent match. (Dit is sodat die bitmap ook reg scale)
        llcolor.addView(cv);
        
        return rootView;
    }
}