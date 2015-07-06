/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 *
 * @author Gideon
 * 
 * --------------------------
 * Description
 * --------------------------
 * This class serves as an adapter for the submissions list.
 * It is used as the adapter for the main submissions list that can be viewed from the main screen.
 * --------------------------
 * 
 */

public class Adapter_Devices_List extends BaseAdapter
{
    //--------------------------
    //  Variables
    //--------------------------
        Context context;
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> address = new ArrayList<String>();
        ArrayList<String> name1 = new ArrayList<String>();
        ArrayList<String> name2 = new ArrayList<String>();
        ArrayList<String> isdefault = new ArrayList<String>();
    //--------------------------

    //--------------------------
    //  Methods
    //--------------------------
        //--------------------------
        //  Adapter_Submissions_List(Context context, Submission p)
        //--------------------------
        //  Constructor that sets the context and assigns the values of a scanner collection
        //--------------------------

        public Adapter_Devices_List(Context context, Bluetooth_Light_Controller device)
        {
            this.context = context;
            this.ids = device.arraylist_device_id;
            this.address = device.arraylist_device_address;
            this.name1 = device.arraylist_device_name_official;
            this.name2 = device.arraylist_device_name_unofficial;
            this.isdefault = device.arraylist_device_isdefault;
        }
        //--------------------------
        
        //--------------------------
        // getView(int position, View convertView, ViewGroup parent)
        //--------------------------
        // create a new Layout/Entry for each
        // item referenced by the Adapter
        //--------------------------
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView  = inflater.inflate(R.layout.row_list_scanner_entry, parent , false);
            }
                TextView nametv           = (TextView)      convertView.findViewById(R.id.name);
                TextView addresstv        = (TextView)      convertView.findViewById(R.id.address);
                TextView isdefaulttv     = (TextView)      convertView.findViewById(R.id.isdefault);
                
                TextView id = (TextView) convertView.findViewById(R.id.id);
                id.setText(ids.get(position));
                id.setVisibility(View.GONE);
                
                if("".equals(name2.get(position))||" ".equals(name2.get(position))||name2.get(position)==null||name2.get(position).length()==0)
                {
                    nametv.setText(name1.get(position));
                }
                else
                {
                    nametv.setText(name2.get(position));
                }
                
                addresstv.setText(address.get(position));
               
                
                if("NO".equals(isdefault.get(position)))
                {
                    isdefaulttv.setText("");
                    isdefaulttv.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                else
                {
                    isdefaulttv.setText("DEFAULT");
                    isdefaulttv.setBackgroundColor(Color.parseColor("#d1f899"));
                }

                return convertView;
         }
        //--------------------------
        
        //--------------------------
        // Not Implemented
        //--------------------------
        public Object getItem(int arg0)
        {
            return null;
        }

        public long getItemId(int position)
        {
           return 0;
        }

        public int getCount()
        {
           if(address == null)
           {
                return 0;
           }
           
           return address.size();
        }
       //--------------------------
    //--------------------------
}
