/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import static gideon.bluetooth.Screen_Main.databaseHelper;
import static gideon.bluetooth.Screen_Main.dialogdevices;
import static gideon.bluetooth.Screen_Main.dialogdevicesedit;
import static gideon.bluetooth.Screen_Main.listAdapterDevices;
import static gideon.bluetooth.Screen_Main.listDevices;
import static gideon.bluetooth.Screen_Main.listview_device;
import static gideon.bluetooth.Screen_Main.mArrayAdapter;
import static gideon.bluetooth.Screen_Main.selectedDevice;
import static gideon.bluetooth.Screen_Main.selectedDeviceName;
import java.util.Set;

/**
 *
 * @author gideon
 */
public class Screen_Settings extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Verander die Title van die Fragment/Activity
        getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[1]);
        
        //Inflate die fragment met sy layout.
        View rootView = inflater.inflate(R.layout.screen_settings, container, false);
        
        //Doen veranderinge aan layout hier... bv Textviews ens...
        
        /*
            Bluetooth Devices Settings
        */

        Connect c;
        
        final Button add_device_button = (Button) rootView.findViewById(R.id.add_device_button);

        listDevices = (ListView) rootView.findViewById(R.id.listview_devices);


        displayLatestList(rootView.getContext());
        registerForContextMenu(listDevices);

        //Action Listener for long click on item in the list
        listDevices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {
                TextView temp2 = (TextView) v.findViewById(R.id.id);
                selectedDevice = temp2.getText().toString();

                TextView temp = (TextView) v.findViewById(R.id.name);
                selectedDeviceName = temp.getText().toString();

                listDevices.showContextMenu();


                return true;
            }
        });

        listDevices.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            // Disallow the touch request for parent scroll on touch of child view
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
            }
        });

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
            }
        });

        add_device_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                dialogdevices = new Dialog(v.getContext());
                dialogdevices.setContentView(R.layout.dialog_device);
                dialogdevices.setTitle(Html.fromHtml("<font color='#6f6f6f'>Choose Device</font>"));

                Button buttoncancel = (Button) dialogdevices.findViewById(R.id.buttoncancel);


                listview_device = (ListView) dialogdevices.findViewById(R.id.listview_device);
                mArrayAdapter= new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1);
                listview_device.setAdapter(mArrayAdapter);

                //Kry eers die Bluetooth Adapter van die device.
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null)
                {
                    // Device does not support Bluetooth
                }
                final Set<BluetoothDevice> pairedDevices;  
                //Maak seker die bluetooth is aan.
                if (!mBluetoothAdapter.isEnabled())
                {
                    mBluetoothAdapter.enable();

                    //Wait untill it is enabled before proceeding...
                    while(!mBluetoothAdapter.isEnabled())
                    {

                    }
                    pairedDevices = queryBluetoothAdapter(mBluetoothAdapter,mArrayAdapter);  
                }
                else
                {
                    pairedDevices = queryBluetoothAdapter(mBluetoothAdapter,mArrayAdapter);       
                }

                //Action Listener for short click on item in the list
                listview_device.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {
                        if (pairedDevices.size() > 0)
                        {
                            // Loop through paired devices
                            int count = 0;
                            for (BluetoothDevice device : pairedDevices)
                            {
                                // Add the name and address to an array adapter to show in a ListView
                                if(count == position)
                                {
                                    //Hier add ons die device in die local database.

                                    //Kyk eers of daar ander devices is.
                                    int amountOfDevices;
                                    Handler_Database_devices dbHelpertemp0 = new Handler_Database_devices(v.getContext());
                                    amountOfDevices = dbHelpertemp0.getDevicesAmount();
                                    dbHelpertemp0.close();

                                    String default_device = "YES";

                                    //If there is more than one scanner, do not make this the default scanner.
                                    if(amountOfDevices > 0)
                                    {
                                        default_device = "NO";
                                    }

                                    //Add the device to the database
                                    Handler_Database_devices dbHelpertemp = new Handler_Database_devices(v.getContext());
                                    dbHelpertemp.addDevice(device.getAddress(), device.getName(), "", default_device);
                                    dbHelpertemp.close();

                                    //Refresh the list of saved device's adapter.
                                    displayLatestList(v.getContext());
                                    registerForContextMenu(listDevices);

                                    //Close the dialog.
                                    dialogdevices.dismiss();
                                }
                                count++;
                            } 
                        }
                    }
                });

                // if button is clicked, close the custom dialog
                buttoncancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogdevices.dismiss();
                    }
                });
                dialogdevices.show();
            }
        });
        
        return rootView;
    }
    
    private static Set<BluetoothDevice> queryBluetoothAdapter(BluetoothAdapter mBluetoothAdapter,ArrayAdapter<String> adapter)
    {
        //Query paired devices
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0)
        {
            adapter.clear();
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices)
            {
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(device.getName());
            }
        }
        return pairedDevices;
    }
    
    private static void displayLatestList(Context c)
    {
        databaseHelper = new Handler_Database_devices(c);
        Bluetooth_Light_Controller devices = databaseHelper.getDevices();
        databaseHelper.close();
        
        listAdapterDevices = new Adapter_Devices_List(c,devices);
        listDevices.setAdapter(listAdapterDevices);
        
        //Setup list to support context menu
        
        //registerForContextMenu(listDevices);
        
        listDevices.setLongClickable(true);
        listAdapterDevices.notifyDataSetChanged();
    }
    
    //Helper method to activate the context menu
    public void showContextMenuDevices()
    { 
        listDevices.showContextMenu();
    }
    
    //On Create method for context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.listview_devices)
        {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.layout.menu_list_device, menu);
        }
    }   
    
    //Content for the context menu
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            
            //Set Default
            case R.id.menu_default:
            databaseHelper = new Handler_Database_devices(getActivity());
            databaseHelper.setDeviceDefault(selectedDevice);
            databaseHelper.close();
            displayLatestList(getActivity());
            return true;
            
            //Delete the selected entry
            case R.id.menu_delete:
            databaseHelper = new Handler_Database_devices(getActivity());
            databaseHelper.deleteDevice(selectedDevice);
            databaseHelper.close();
            displayLatestList(getActivity());
            return true;

            //Edit the selected entry
            case R.id.menu_rename:
                dialogdevicesedit = new Dialog(getActivity());
                dialogdevicesedit.setContentView(R.layout.dialog_device_edit);
                dialogdevicesedit.setTitle(Html.fromHtml("<font color='#6f6f6f'>Rename Device</font>"));
                
                final EditText name = (EditText ) dialogdevicesedit.findViewById(R.id.edittextname);
                Button buttonsave = (Button) dialogdevicesedit.findViewById(R.id.buttonsave);
                Button buttoncancel = (Button) dialogdevicesedit.findViewById(R.id.buttoncancel);
                
                name.setText(selectedDeviceName);

                buttonsave.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String newName = name.getText().toString();
                        databaseHelper = new Handler_Database_devices(v.getContext());
                        databaseHelper.updateDeviceName(selectedDevice,newName);
                        databaseHelper.close();
                        displayLatestList(v.getContext());
                        dialogdevicesedit.dismiss();
                    }
                });
                
                // if button is clicked, close the custom dialog
                buttoncancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogdevicesedit.dismiss();
                    }
                });
                dialogdevicesedit.show();
                
                //Default
                default:
                return super.onContextItemSelected(item);
        }
    }
}