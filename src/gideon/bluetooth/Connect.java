/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author gideon
 */
public class Connect extends Thread
{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    //final int SOCKET_CONNECTED = 9998;
    //final int MESSAGE_READ = 9999; 
    
    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
 
    Handler mHandler;
    
    public Connect(BluetoothDevice device, Handler h1)
    {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mHandler = h1;
 
        android.util.Log.w("     BLUETOOTH     ", "Creating Socket...");
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            UUID uuid = MY_UUID;
            try
            {
                uuid = device.getUuids()[0].getUuid();
            }
            catch (NullPointerException ne)
            {
                android.util.Log.w("     BLUETOOTH     ", "There was a Nullpointer exception... Imma Just Saying yo..");
                uuid = MY_UUID;
            }
            android.util.Log.w("     BLUETOOTH     ", "UUID from device: "+uuid);
            tmp = device.createRfcommSocketToServiceRecord(uuid);
            android.util.Log.w("     BLUETOOTH     ", "SUCCESS, created socket from device...");
        } catch (IOException e) { android.util.Log.w("     BLUETOOTH     ", "Could not connect with Device..."); }
        mmSocket = tmp;
    }
 
    public void run()
    {
        // Cancel discovery because it will slow down the connection
        //mBluetoothAdapter.cancelDiscovery();
 
        try
        {
            android.util.Log.w("     BLUETOOTH     ", "Attempting Connection with Socket...");
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            android.util.Log.w("     BLUETOOTH     ", "Connected");
             ConnectedThread conn = new ConnectedThread(mmSocket,mHandler);
            Message msg = mHandler.obtainMessage();
                        msg.what = 556;
                            mHandler.sendMessage(msg);
            conn.start();

        } catch (IOException connectException)
        {
            android.util.Log.w("     BLUETOOTH     ", "Unable to connect, closing socket: "+connectException);
            // Unable to connect; close the socket and get out
            try
            {
                mmSocket.close();
            } catch (IOException closeException) { }
        }
    }
    
    private void manageConnectedSocket()
    {
        ConnectedThread conn = new ConnectedThread(mmSocket,mHandler);
        //mHandler.obtainMessage(SOCKET_CONNECTED, conn).sendToTarget();
        android.util.Log.w("     BLUETOOTH     ", "Connected");
        conn.start();
    }

 
    // Will cancel an in-progress connection, and close the socket 
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
