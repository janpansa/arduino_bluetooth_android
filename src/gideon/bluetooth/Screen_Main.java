package gideon.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Set;


public class Screen_Main extends Activity
{
    //ListView listview_device;
    //EditText edittext_mcnumber;
    //ArrayAdapter<String> mArrayAdapter;
    public static String mcnum;
    
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
    public static String mcnumscanner;
    
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

    private void selectItem(int position)
    {
        int EXIT = 4;
        if(position==EXIT)
        {
            onBackPressed();
        }
        else
        {
            // update the main content by replacing fragments
            Fragment fragment = new ContainerFragment();
            Bundle args = new Bundle();
            args.putInt(ContainerFragment.FRAGMENT_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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
        new AlertDialog.Builder(this)
        .setMessage(Html.fromHtml("<cite>Quit ?</cite>"))
        .setNegativeButton("No", new android.content.DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
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

    public static class ContainerFragment extends Fragment
    {
        public static final String FRAGMENT_NUMBER = "fragment_number";

        public ContainerFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
        {
            /*
                Fragment Volgorde
                ------------------
                0: Home
                1: Settings
                2: Change color
                3: Help
                4: Exit (TODO: moet voor hierdie gehandle word met 'n dialog vir exit...)
            */
            
            //Case statement wat i check en dan die relevante fragment load.
            
            switch(getArguments().getInt(FRAGMENT_NUMBER))
            {
                case 0:
                        {
                            String menuTitle = getResources().getStringArray(R.array.menuitems)[0];
                            View rootView = inflater.inflate(R.layout.screen_home, container, false);
                            //Doen veranderinge aan layout hier... bv Textviews ens...
                            getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[0]);
                            return rootView;
                        }
                case 1:
                        {
                            View rootView = inflater.inflate(R.layout.screen_settings, container, false);
                            //Doen veranderinge aan layout hier... bv Textviews ens...
                            getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[1]);
                            
                           
                            
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
                                                        //Hier add ons die scanner in die local database.

                                                        //Kyk eers of daar ander scanners is.
                                                        int amountOfScanner;
                                                        Handler_Database_devices dbHelpertemp0 = new Handler_Database_devices(v.getContext());
                                                        amountOfScanner = dbHelpertemp0.getDevicesAmount();
                                                        dbHelpertemp0.close();

                                                        String default_device = "YES";

                                                        //If there is more than one scanner, do not make this the default scanner.
                                                        if(amountOfScanner > 0)
                                                        {
                                                            default_device = "NO";
                                                        }

                                                        //Add the scanner to the database
                                                        Handler_Database_devices dbHelpertemp = new Handler_Database_devices(v.getContext());
                                                        dbHelpertemp.addDevice(device.getAddress(), device.getName(), "", default_device);
                                                        dbHelpertemp.close();

                                                        //Refresh the list of saved scanner's adapter.
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
                case 2:
                        {
                            View rootView = inflater.inflate(R.layout.screen_change_color, container, false);
                            //Doen veranderinge aan layout hier... bv Textviews ens...
                            getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[2]);
                            return rootView;
                        }
                case 3:
                        {
                            View rootView = inflater.inflate(R.layout.screen_help, container, false);
                            //Doen veranderinge aan layout hier... bv Textviews ens...
                            getActivity().setTitle(getResources().getStringArray(R.array.menuitems)[3]);
                            return rootView;
                        }
                default:   
                        {
                            String menuTitle = getResources().getStringArray(R.array.menuitems)[0];
                            View rootView = inflater.inflate(R.layout.screen_home, container, false);
                            //Doen veranderinge aan layout hier... bv Textviews ens...
                            getActivity().setTitle(menuTitle);
                            return rootView;
                        }
            }
        }
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
            MenuInflater inflater = getMenuInflater();
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
            databaseHelper = new Handler_Database_devices(this);
            databaseHelper.setDeviceDefault(selectedDevice);
            databaseHelper.close();
            displayLatestList(this);
            return true;
            
            //Delete the selected entry
            case R.id.menu_delete:
            databaseHelper = new Handler_Database_devices(this);
            databaseHelper.deleteDevice(selectedDevice);
            databaseHelper.close();
            displayLatestList(this);
            return true;

            //Edit the selected entry
            case R.id.menu_rename:
                dialogdevicesedit = new Dialog(this);
                dialogdevicesedit.setContentView(R.layout.dialog_device_edit);
                dialogdevicesedit.setTitle(Html.fromHtml("<font color='#6f6f6f'>Rename Scanner</font>"));
                //dialogScannerEdit.getWindow().setLayout(450, 600);

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
    
    


 