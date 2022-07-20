package com.example.medibuddy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {
    static NetworkInfo wifidata,Mobiledata;

    public static boolean isConnected (Context c)

    {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {

            wifidata= cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Mobiledata=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(wifidata!=null && wifidata.isAvailable() && wifidata.isConnected())
        {
            Toast.makeText(c, "Internet Available", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (Mobiledata!=null && Mobiledata.isAvailable() && Mobiledata.isConnected())
        {
            Toast.makeText(c, "mobile data is on", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            Toast.makeText(c, "please check your internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
