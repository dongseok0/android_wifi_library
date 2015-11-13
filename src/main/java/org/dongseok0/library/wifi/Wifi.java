package org.dongseok0.library.wifi;

import android.content.Context;
import android.os.Build;

import org.dongseok0.library.wifi.reflection.WifiManager;
import org.dongseok0.library.wifi.reflection.WifiManager17;

/**
 * Created by dongseok0 on 9/11/2015.
 */
public class Wifi {
    static private WifiManager mInstance;

    public static WifiManager getWifiManager(Context ctx) {
        if (mInstance != null) {
            return mInstance;
        }

        switch (Build.VERSION.SDK_INT)
        {
            case 14:
            case 15:
                //internalSaveDone = save_4_0(wifiManager, configuration);
                mInstance = new WifiManager(ctx);
                break;

            case 16:
                //internalSaveDone = save_4_1(wifiManager, configuration);
                mInstance = new WifiManager(ctx);
                break;

            case 17:
            case 18:
            case 19:
            case 20:
            default:
                mInstance = new WifiManager17(ctx);
        }

        return mInstance;
    }
}
