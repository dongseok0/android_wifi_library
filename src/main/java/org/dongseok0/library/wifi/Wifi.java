package org.dongseok0.library.wifi;

import android.content.Context;
import android.os.Build;

import org.dongseok0.library.wifi.wifimanager.WifiManager;
import org.dongseok0.library.wifi.wifimanager.WifiManager17;
import org.dongseok0.library.wifi.wificonfiguration.WifiConfigurationHelper;
import org.dongseok0.library.wifi.wificonfiguration.WifiConfigurationHelper16;
import org.dongseok0.library.wifi.wificonfiguration.WifiConfigurationHelper21;

/**
 * Created by dongseok0 on 9/11/2015.
 */
public class Wifi {
    static private WifiManager wifiManager;
    static private WifiConfigurationHelper wifiConfigurationHelper;

    public static WifiManager getWifiManager(Context ctx) {
        if (wifiManager != null) {
            return wifiManager;
        }

        switch (Build.VERSION.SDK_INT)
        {
            case 14:
            case 15:
                //internalSaveDone = save_4_0(wifiManager, configuration);
                wifiManager = new WifiManager(ctx);
                break;

            case 16:
                //internalSaveDone = save_4_1(wifiManager, configuration);
                wifiManager = new WifiManager(ctx);
                break;

            case 17:
            case 18:
            case 19:
            case 20:
            default:
                wifiManager = new WifiManager17(ctx);
        }

        return wifiManager;
    }

    public static WifiConfigurationHelper getWifiConfigurationHelper() {
        if (wifiConfigurationHelper != null) {
            return wifiConfigurationHelper;
        }

        switch (Build.VERSION.SDK_INT)
        {
            case 14:
            case 15:
                // not support
                break;

            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                wifiConfigurationHelper = new WifiConfigurationHelper16();
                break;

            case 21:
            case 22:
            case 23:
            default:
                wifiConfigurationHelper = new WifiConfigurationHelper21();
        }

        return wifiConfigurationHelper;
    }
}
