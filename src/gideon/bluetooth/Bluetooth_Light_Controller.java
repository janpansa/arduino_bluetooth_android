/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;
import java.util.ArrayList;

/**
 *
 * @author gideon
 */
public class Bluetooth_Light_Controller
{
    //Device Members
    ArrayList<String> arraylist_device_id = new ArrayList<String>();
    ArrayList<String> arraylist_device_address = new ArrayList<String>();
    ArrayList<String> arraylist_device_name_official = new ArrayList<String>();
    ArrayList<String> arraylist_device_name_unofficial = new ArrayList<String>();
    ArrayList<String> arraylist_device_isdefault = new ArrayList<String>();


    Bluetooth_Light_Controller(
    ArrayList<String> device_id,
    ArrayList<String> device_address,
    ArrayList<String> device_name_official,
    ArrayList<String> device_name_unofficial,
    ArrayList<String> arraylist_device_isdefault
    )
    {
        this.arraylist_device_id = device_id;
        this.arraylist_device_address = device_address;
        this.arraylist_device_name_official = device_name_official;
        this.arraylist_device_name_unofficial = device_name_unofficial;
        this.arraylist_device_isdefault = arraylist_device_isdefault;
    }
}