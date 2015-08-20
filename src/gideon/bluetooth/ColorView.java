/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gideon.bluetooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class ColorView extends View
{
    Bitmap colorPalette; // Die image wat ons sommer in gimp gemaak het :-)
    float mScaleFactor = 1f;
    LinearLayout l; // Die linearlayout wat die selected kleur wys.
    CheckBox livepreview;

    public ColorView(Context c, LinearLayout l, CheckBox cb)
    {
        super(c);
        colorPalette = BitmapFactory.decodeResource(c.getResources(), R.drawable.colors); // Dink ons kan hierdie vinniger maak deur om dit in n drawable te verander
        this.l = l;
        this.livepreview = cb;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.drawBitmap(colorPalette, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float xvalue = Math.abs(event.getX());// Kry die X waarde van hierdie view, maw die colors image. Ek gebruik abs om seker te maak die waarde bly positief.
        float yvalue = Math.abs(event.getY());// Kry die Y waarde van hierdie view, maw die colors image. Ek gebruik abs om seker te maak die waarde bly positief.
        int point = colorPalette.getPixel((int)xvalue,(int) yvalue);// Hierdie gee vir ons n kleur van die spesifieke pixel.
        int red = Color.red(point); // Hierdie gee vir ons die rooi waarde 0-255 van die spesifieke pixel.
        int green = Color.green(point); // Hierdie gee vir ons die groen waarde 0-255 van die spesifieke pixel.
        int blue = Color.blue(point); // Hierdie gee vir ons die blou waarde 0-255 van die spesifieke pixel.
        
        l.setBackgroundColor(Color.rgb(red, green, blue));// Ons gebruik die waarde van die pixel en set dit hier
        
        //Nou hier wil ons die bluetooth values deurstuur, so , kyk eers of die livepreview checked is.
        
        if(livepreview.isChecked())
        {
           //ok, as live preview enabled is, kyk eers of die connection reg is
           
               //Message format to set color AT+SETCOLOR=255,255,255
               Session.message = "AT+SETCOLOR="+Integer.toString(red)+","+Integer.toString(green)+","+Integer.toString(blue);
               Log.d("Message",Session.message);
        }
        
        //Log.d("Colours","R:" +red +" G:" + green + " B:" + blue);
        
        //invalidate(); // refresh die ui... Ek dink nie dit is nodig nie, so ek comment uit vir nou.
        
        return true;
    }
}
