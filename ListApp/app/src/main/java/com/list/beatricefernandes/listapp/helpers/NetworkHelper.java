package com.list.beatricefernandes.listapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Created by beatrice.fernandes on 01/04/18.
 */

public class NetworkHelper {


        public static boolean isInternetConnectionON(Context context) {

            boolean isInternetConnectionON = true;

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if((networkInfo == null) || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                isInternetConnectionON = false;
            }

            return isInternetConnectionON;
        }

        public static String getConnectivityType(Context context) {

            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_BLUETOOTH:
                    return "Bluetooth";
                case ConnectivityManager.TYPE_DUMMY:
                    return "Dummy";
                case ConnectivityManager.TYPE_ETHERNET:
                    return "Ethernet";
                case ConnectivityManager.TYPE_MOBILE:
                    return "Mobile";
                case ConnectivityManager.TYPE_MOBILE_DUN:
                    return "Mobile Dun";
                case ConnectivityManager.TYPE_VPN:
                    return "VPN";
                case ConnectivityManager.TYPE_WIFI:
                    return "WIFI";
                case ConnectivityManager.TYPE_WIMAX:
                    return "WIMAX";
            }

            return null;

        }

    }


