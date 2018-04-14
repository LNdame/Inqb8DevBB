package cite.ansteph.beerly.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;

/**
 * Created by loicstephan on 2018/03/24.
 */

public class InternetConnectionStatus {


    public InternetConnectionStatus() {
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    //This method actually checks if device is connected to internet(There is a possibility it's connected to a network but not to internet).

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


     public static boolean isFullConnectionOn(Context context)
     {
         boolean isNetwork =isNetworkConnected(context);

         if(isNetwork){

                 return true;

         }
          return false;
     }



}
