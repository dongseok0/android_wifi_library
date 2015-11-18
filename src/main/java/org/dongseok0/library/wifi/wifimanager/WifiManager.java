package org.dongseok0.library.wifi.wifimanager;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dongseok0 on 9/11/2015.
 */
public class WifiManager implements IWifiManager {
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_UNKNOWN = 4;

    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";

    public static final String EXTRA_WIFI_STATE = "wifi_state";
    public static final String EXTRA_NEW_STATE = "newState";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";

    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;

    final android.net.wifi.WifiManager mWifiManager;
    private Method setWifiApEnabled;
    private Method getWifiApState;

    public WifiManager(Context ctx) {
        mWifiManager = (android.net.wifi.WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        try {
            getWifiApState = mWifiManager.getClass().getDeclaredMethod("getWifiApState");
            setWifiApEnabled = mWifiManager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect(int networkId) {

    }

    @Override
    public void connect(WifiConfiguration configuration) {

    }

    @Override
    public void forget(int netId) {

    }

    @Override
    public void save(WifiConfiguration configuration) {

    }

    @Override
    public int getWifiApState() {
        try {
            return (int) getWifiApState.invoke(mWifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return WIFI_AP_STATE_FAILED;
    }

    @Override
    public void setWifiApEnabled(WifiConfiguration config, boolean enabled) {
        try {
            setWifiApEnabled.invoke(mWifiManager, null, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // proxies
    public List<WifiConfiguration> getConfiguredNetworks() {
        return mWifiManager.getConfiguredNetworks();
    }

    public boolean disableNetwork(int netId) {
        return mWifiManager.disableNetwork(netId);
    }

    public int updateNetwork(WifiConfiguration config) {
        return mWifiManager.updateNetwork(config);
    }

    public boolean saveConfiguration() {
        return mWifiManager.saveConfiguration();
    }

    public WifiInfo getConnectionInfo() {
        return mWifiManager.getConnectionInfo();
    }

    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    public boolean setWifiEnabled(boolean enabled) {
        return mWifiManager.setWifiEnabled(enabled);
    }

    public int getWifiState() {
        return mWifiManager.getWifiState();
    }

    public List<ScanResult> getScanResults() {
        return mWifiManager.getScanResults();
    }

    public boolean startScan() {
        return mWifiManager.startScan();
    }
}
