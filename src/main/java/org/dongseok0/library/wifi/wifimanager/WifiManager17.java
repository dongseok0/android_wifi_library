package org.dongseok0.library.wifi.wifimanager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by dongseok0 on 9/11/2015.
 */
public class WifiManager17 extends WifiManager {
    private final String TAG = this.getClass().getName();
    private Method  forget;
    private Method  connect_netID;
    private Method  connect_wifiConfig;
    private Method  save;

    public WifiManager17(Context ctx) {
        super(ctx);
        try {
            Class<?> wifiManagerCls = mWifiManager.getClass();
            Class actionListener = Class.forName("android.net.wifi.WifiManager$ActionListener", false, null);
            connect_netID = wifiManagerCls.getDeclaredMethod("connect", int.class, actionListener);
            connect_wifiConfig = wifiManagerCls.getDeclaredMethod("connect", WifiConfiguration.class, actionListener);
            forget = wifiManagerCls.getDeclaredMethod("forget", int.class, actionListener);
            save = wifiManagerCls.getDeclaredMethod("save", WifiConfiguration.class, actionListener);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect(int networkId) {
        try {
            connect_netID.invoke(mWifiManager, networkId, null);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use the STANDARD API as a fallback solution
        mWifiManager.enableNetwork(networkId, true);
    }

    @Override
    public void connect(WifiConfiguration configuration) {
        try {
            connect_wifiConfig.invoke(mWifiManager, configuration, null);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use the STANDARD API as a fallback solution
        mWifiManager.enableNetwork(configuration.networkId, true);
    }

    @Override
    public void forget(int netId) {
        try {
            forget.invoke(mWifiManager, netId, null);
            return;
        } catch (Exception e) {
            Log.e(TAG, "Exception trying to forget");
        }

        mWifiManager.disableNetwork(netId);
    }

    @Override
    public void save(WifiConfiguration configuration) {
        try {
            save.invoke(mWifiManager, configuration, null);
            return;
        } catch (Exception e) {
            Log.e(TAG, "Exception trying to save");
        }

        if (configuration.networkId == -1) {
            mWifiManager.addNetwork(configuration);
        } else {
            mWifiManager.updateNetwork(configuration);
        }
    }
}
