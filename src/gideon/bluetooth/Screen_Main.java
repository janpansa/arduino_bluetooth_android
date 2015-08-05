package gideon.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;


public class Screen_Main extends Activity
{
    //ListView listview_device;
    //EditText edittext_mcnumber;
    //ArrayAdapter<String> mArrayAdapter;
    public static String mcnum;
    
    public Boolean isExit = false;
    
    //Navigation drawer
    //------------------------------
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFragmentTitles;
    
    //Bluetooth Variables
    public static ArrayAdapter<String> mArrayAdapter;
    public static String selectedDevice,selectedDeviceName;
    public static ListView listDevices;
    public static ListView listview_device;
    //Bluetooth_Light_Controller controllers;
    public static Handler_Database_devices databaseHelper;
    public static Dialog dialogdevices,dialogdevicesedit;
    public static BaseAdapter listAdapterDevices;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Remove the icon before the content is show ... sigh :-)
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        
        mTitle = mDrawerTitle = getTitle();
        mFragmentTitles = getResources().getStringArray(R.array.menuitems);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mFragmentTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //Net daai icon weg vat...
        
        

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        /*
        listview_device = (ListView) findViewById(R.id.listview_device);
        mArrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listview_device.setAdapter(mArrayAdapter);
        edittext_mcnumber = (EditText) findViewById(R.id.edittext_mcnumber);
        edittext_mcnumber.setKeyListener(null);
        
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        
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
            pairedDevices = queryBluetoothAdapter(mBluetoothAdapter);  
        }
        else
        {
            pairedDevices = queryBluetoothAdapter(mBluetoothAdapter);       
        }
        
        final Handler handler = new Handler()
        {
        @Override
        public void handleMessage(Message msg) {
          if(msg.what==555)
          {
            //Connected, waiting for Read
            edittext_mcnumber.setText(mcnum);
            TextView txt1 = new TextView(Screen_Main.this);
            txt1.setLayoutParams(params);
            txt1.setPadding(20, 1, 10, 1);
            Calendar c1 = Calendar.getInstance();      
            String myfrmt = c1.get(Calendar.YEAR) +"/"+ c1.get(Calendar.MONTH)+1 +"/"+ c1.get(Calendar.DAY_OF_MONTH) +" "+ c1.get(Calendar.HOUR_OF_DAY) +":"+ c1.get(Calendar.MINUTE);
            txt1.setText(myfrmt+" - "+mcnum);
            linearLayout.addView(txt1);
          }
          if(msg.what==556)
          {
           //Connected, waiting for Read
           Toast.makeText(Screen_Main.this, "Connected. Start Scanning", Toast.LENGTH_LONG).show();
          }
          super.handleMessage(msg);
        }
      };
        
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
                            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress() +"\n" + device.getUuids());
                            if(count == position)
                            {
                                android.util.Log.w("     BLUETOOTH     ", "Attempting to connect to device");
                                Toast.makeText(Screen_Main.this, "Connecting. Please Wait . . .", Toast.LENGTH_LONG).show();
                                Connect c = new Connect(device,handler);
                                c.start();
                            }
                            count++;
                        } 
                    }
            }
        });
        */
    }
/*
    private Set<BluetoothDevice> queryBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        //Query paired devices
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0)
        {
            mArrayAdapter.clear();
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices)
            {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName());
            }
        }
        return pairedDevices;
    }*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        
        // Handle action buttons
        // Hier kan ons 'n context menu of icon add heel regs as ons ooit wil...
        switch(item.getItemId())
        {
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    // Ek sit die fragments in n array vir die fragment manager hier onder.
    
     /*
        Fragment Volgorde
        ------------------
        0: Home
        1: Settings
        2: Change color
        3: Help
        4: Exit (TODO: moet voor hierdie gehandle word met 'n dialog vir exit...)
    */
    
    final String[] screens =
    {
        "gideon.bluetooth.Screen_Home",
        "gideon.bluetooth.Screen_Settings",
        "gideon.bluetooth.Screen_Change_Color",
        "gideon.bluetooth.Screen_Help"
    };

    private void selectItem(int position)
    {
        int EXIT = 4;
        if(position==EXIT)
        {
            isExit = true;
            onBackPressed();
        }
        else
        {
            //Ek maak 'n tag vir die fragments om te gebruik vir die back button check later
            String TAG;
            if(position == 0)
            {
                TAG = "Home";
            }
            else if(position == 1)
            {
                TAG = "Settings";
            }
            else if(position == 2)
            {
                TAG = "Color";
            }
            else if(position == 3)
            {
                TAG = "Help";
            }
            else
            {
                TAG = "Home";
            }
            
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            
            //Ek dink nie Home moet op die back stack weees nie.
            if("Home".equals(TAG))
            {
               fragmentManager.beginTransaction().replace(R.id.content_frame, Fragment.instantiate(Screen_Main.this, screens[position]),TAG).commit(); 
            }
            else
            {
                fragmentManager.beginTransaction().replace(R.id.content_frame, Fragment.instantiate(Screen_Main.this, screens[position]),TAG).addToBackStack("previousFragment").commit();
            }
            
            
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mFragmentTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    //--------------------------
    //  All the overrides.
    //
    //  I add these if needed later,
    //  e.g. we want to disconnect
    //  the bluetooth on Pause()
    //  etc...
    //
    //--------------------------
    
    //--------------------------
    //  onBackPressed
    //--------------------------
    @Override
    public void onBackPressed()
    {
        //First check if the home fragment is active, else don't handle the back press.
        if(getFragmentManager().findFragmentByTag("Home").isVisible()||isExit)
        {
            Boolean isBluetoothOn = false;
            BluetoothAdapter tempAdapter = BluetoothAdapter.getDefaultAdapter();
            if (tempAdapter == null)
            {
                // Device does not support Bluetooth
                
            }
            else
            {
                //Maak seker die bluetooth is aan.
                if (tempAdapter.isEnabled())
                {
                    isBluetoothOn = true;
                }
            }
            
            if(isBluetoothOn)
            {
                View dialog_checkbox = View.inflate(this, R.layout.dialog_checkbox, null);
                final CheckBox checkbluetooth = (CheckBox) dialog_checkbox.findViewById(R.id.checkbluetooth);
                checkbluetooth.setChecked(true);//In most cases, bluetooth won't be ON before our app was launched, so in most cases we want the default behaviour to switch it off when we exit.
                checkbluetooth.setText("Turn Bluetooth off ?");
                
                new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml("<cite>Quit ?</cite>"))
                .setView(dialog_checkbox)
                .setNegativeButton("No", new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                       isExit = false;
                       arg0.dismiss();
                    }
                })
                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        //Lets first make sure the user did not change his mind.
                        if(checkbluetooth.isChecked())
                        {
                            //Switch the bluetooth off when done.
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
                            if (mBluetoothAdapter.isEnabled())
                            {
                                mBluetoothAdapter.disable();
                            }
                        }
                        finish();
                    }
                }).create().show();
            }
            else
            {
                new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml("<cite>Quit ?</cite>"))
                .setNegativeButton("No", new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                       isExit = false;
                       arg0.dismiss();
                    }
                })
                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        finish();
                    }
                }).create().show();
            }
        }
        else
        {
            //Go back to the first screen...
            getFragmentManager().beginTransaction().replace(R.id.content_frame, Fragment.instantiate(Screen_Main.this, screens[0]),"Home").commit();
        }
    }
    
    //--------------------------
    //  onPause
    //--------------------------
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    //--------------------------
    //  onStop
    //--------------------------
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    //--------------------------
    //  onResume
    //--------------------------
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    //--------------------------
    //  onStart
    //--------------------------
    @Override
    public void onStart()
    {
        super.onStart();
    }
}
    
    


 