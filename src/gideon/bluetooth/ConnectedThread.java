package gideon.bluetooth;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 *
 * @author gideon
 */
public class ConnectedThread extends Thread
{
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    DataInputStream dinput;
    Handler mHandler;
    //final int SOCKET_CONNECTED = 9998;
    //final int MESSAGE_READ = 9999; 
    
    String Message = "";
    
    public ConnectedThread(BluetoothSocket socket, Handler h1)
    {
        android.util.Log.w("     BLUETOOTH     ", "Created a thread for the connected socket");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
       
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        android.util.Log.w("     BLUETOOTH     ", "Got the input and output streams.");
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        mHandler = h1;
    }
 
    public void run() {
            
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
 
            // Keep listening to the InputStream until an exception occurs
            android.util.Log.w("     BLUETOOTH     ", "Reading from the Input Stream");
        while (true)
        {
                // Read from the InputStream
                //BufferedReader r = new BufferedReader(new InputStreamReader(mmInStream));
            
            /*
                try
                {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    android.util.Log.w("     BLUETOOTH     ", "Done with the READ method.");
                    
           
                    String readMessage = new String(buffer, 0, bytes);
                    android.util.Log.w("     BLUETOOTH     ", "Arduino says:"+readMessage.trim());
                   Screen_Main.mcnum = readMessage.trim();
                   
                   if(readMessage.trim().contains(".")&&readMessage.trim().contains("KG")&&!readMessage.trim().startsWith("."))
                   {
                       if(readMessage.trim().length()==7 || readMessage.trim().length()==8)
                       {
                        android.util.Log.w("     BLUETOOTH     ", "Bluetooth Scale Says: "+readMessage.trim());
                         Message msg = mHandler.obtainMessage();
                            msg.what = 555;
                                mHandler.sendMessage(msg);
                                android.util.Log.w("     BLUETOOTH     ", "Socket Closed - Read Completed");
                                this.cancel();
                                Message msg2 = mHandler.obtainMessage();
                                msg2.what = 557;
                                mHandler.sendMessage(msg2);
                                break;
                       }
                   }
                   else
                   {
                   }
                }
                catch (IOException ex)
                {
                    android.util.Log.w("     BLUETOOTH     ", "Socket Closed - This is why -> "+ex);
                    break;
                }
                catch (NullPointerException n)
                {
                    //android.util.Log.w("     BLUETOOTH     ", "Bluetooth Scale Says: NOTHING");
                }
                */
            
            //kom ons skryf
                if(!"".equals(Session.message))
                {
                    if(!Message.equals(Session.message))
                    {

                         Message = Session.message;
                         Log.d("Message write",Message);
                         
                         this.write(Message);
                    }
                }
                
            }
        }
    

 
    /* Call this from the main activity to send data to the remote device */
    public void write(String s) {
        try {
            mmOutStream.write(s.getBytes(Charset.forName("UTF-8")));
            //mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
           android.util.Log.w("     BLUETOOTH     ", "cancel() - Socket Closed");
            mmSocket.close();
        } catch (IOException e) { }
    }
}