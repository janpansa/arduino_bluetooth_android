package gideon.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Set;

public class Screen_Main extends Activity
{
    ListView listview_device;
    EditText edittext_mcnumber;
    ArrayAdapter<String> mArrayAdapter;
    public static String mcnum;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
    }

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
    }
}
    
    


 