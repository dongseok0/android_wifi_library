package org.dongseok0.library.wifi.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;

import org.dongseok0.library.wifi.android.IpAssignment;
import org.dongseok0.library.wifi.android.ProxySettings;
import org.json.JSONObject;

/**
 * Created by dongseok0 on 6/11/2015.
 */
public class WifiConfigurationUtil {
    public static final int DISABLED_UNKNOWN_REASON                         = 0;
    public static final int DISABLED_DNS_FAILURE                            = 1;
    public static final int DISABLED_DHCP_FAILURE                           = 2;
    public static final int DISABLED_AUTH_FAILURE                           = 3;
    public static final int DISABLED_ASSOCIATION_REJECT                     = 4;
    public static final int DISABLED_BY_WIFI_MANAGER                        = 5;

    private static WifiConfigurationHelper helper;
    static {
        if (Build.VERSION.SDK_INT >= 20) {
            helper = new WifiConfigurationHelper20();
        } else {
            helper = new WifiConfigurationHelper();
        }
    }

    public static void setIpProxy(WifiConfiguration config, JSONObject jsonConfig) {
        helper.setIpProxy(config, jsonConfig);
    }

    public static String[] getProxyFields(WifiConfiguration config) {
        return helper.getProxyFields(config);
    }

    public static boolean setProxyFields(WifiConfiguration config, String host, String port, String exclusionList) {
        return helper.setProxyFields(config, host, port, exclusionList);
    }

    public static IpAssignment getIpAssignment(WifiConfiguration config) {
        return helper.getIpAssignment(config);
    }

    public static ProxySettings getProxySettings(WifiConfiguration config) {
        return helper.getProxySettings(config);
    }

    public static void setCaCertificateAlias(WifiConfiguration config, String caCert) {
        helper.setCaCertificateAlias(config, caCert);
    }

    public static String getCaCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        return helper.getCaCertificateAlias(enterpriseConfig);
    }

    public static void setClientCertificateAlias(WifiConfiguration config, String clientCert) {
        helper.setClientCertificateAlias(config, clientCert);
    }

    public static String getClientCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        return helper.getClientCertificateAlias(enterpriseConfig);
    }

    public static int getDisableReason(WifiConfiguration config) {
        return helper.getDisableReason(config);
    }
}
