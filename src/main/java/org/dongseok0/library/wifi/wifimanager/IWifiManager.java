package org.dongseok0.library.wifi.wifimanager;

import android.net.wifi.WifiConfiguration;

/**
 * Created by dongseok0 on 9/11/2015.
 */
public interface IWifiManager {
    void connect(int networkId);
    void connect(WifiConfiguration configuration);
    void forget(int netId);
    void save(WifiConfiguration configuration);
    int getWifiApState();
    void setWifiApEnabled(WifiConfiguration config, boolean enabled);
}
