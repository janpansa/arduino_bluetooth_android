/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.TextView;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gideon
 * 
 * --------------------------
 * Description
 * --------------------------
 * This class is used to provide functionality for the splash screen.
 * In this class, the timeout of the splash screen is controlled.
 * --------------------------
 * 
 */
public class Screen_Splash extends Activity
{
    // Set the display time, in milliseconds (or extract it out as a configurable parameter)
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    TextView versiontext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.screen_splash);

            //Maak seker die screen bly aan.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            PackageManager packageManager = this.getPackageManager();
            String packageName = this.getPackageName();

            versiontext = (TextView) findViewById(R.id.versiontext);
        
    }

    @Override
    protected void onResume()
    {
            super.onResume();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

            //Maak seker die screen bly aan.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // Obtain the sharedPreference, default to true if not available
            boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);

            if (isSplashEnabled)
            {
                     new Handler().postDelayed(new Runnable()
                    {
                            @Override
                            public void run()
                            {
                                //Finish the splash activity so it can't be returned to.
                                Screen_Splash.this.finish();
                                // Create an Intent that will start the main activity.
                                Intent mainIntent = new Intent(Screen_Splash.this, Screen_Main.class);
                                Screen_Splash.this.startActivity(mainIntent);
                            }
                    }, SPLASH_DISPLAY_LENGTH);
            }
            else
            {
                    // if the splash is not enabled, then finish the activity immediately and go to main.
                    finish();
                    Intent mainIntent = new Intent(Screen_Splash.this, Screen_Main.class);
                    Screen_Splash.this.startActivity(mainIntent);
            }
    }
        
    @Override
    public void onBackPressed()
    {
    }
}
