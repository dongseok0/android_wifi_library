package org.dongseok0.library.wifi.wificonfiguration;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;

import org.dongseok0.library.wifi.android.IpAssignment;
import org.dongseok0.library.wifi.android.ProxySettings;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dongseok0 on 18/11/2015.
 */
public abstract class WifiConfigurationHelper {
    public static final int DISABLED_UNKNOWN_REASON                         = 0;
    public static final int DISABLED_DNS_FAILURE                            = 1;
    public static final int DISABLED_DHCP_FAILURE                           = 2;
    public static final int DISABLED_AUTH_FAILURE                           = 3;
    public static final int DISABLED_ASSOCIATION_REJECT                     = 4;
    public static final int DISABLED_BY_WIFI_MANAGER                        = 5;

    private Field disableReason;
    private Method setClientCertificateAlias;
    private Method getClientCertificateAlias;
    private Method getCaCertificateAlias;
    private Method setCaCertificateAlias;

    WifiConfigurationHelper() {
        try {
            setCaCertificateAlias = WifiEnterpriseConfig.class.getDeclaredMethod("setCaCertificateAlias", String.class);
            getCaCertificateAlias = WifiEnterpriseConfig.class.getDeclaredMethod("getCaCertificateAlias");
            setClientCertificateAlias = WifiEnterpriseConfig.class.getDeclaredMethod("setClientCertificateAlias", String.class);
            getClientCertificateAlias = WifiEnterpriseConfig.class.getDeclaredMethod("getClientCertificateAlias");
            disableReason = WifiConfiguration.class.getField("disableReason");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public abstract void setIpProxy(WifiConfiguration config, JSONObject jsonConfig);

    public abstract String[] getProxyFields(WifiConfiguration config);

    public abstract boolean setProxyFields(WifiConfiguration config, String host, String port, String exclusionList);

    public abstract IpAssignment getIpAssignment(WifiConfiguration config);

    public abstract ProxySettings getProxySettings(WifiConfiguration config);

    public void setCaCertificateAlias(WifiConfiguration config, String caCert) {
        try {
            setCaCertificateAlias.invoke(config.enterpriseConfig, caCert);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getCaCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        try {
            return (String) getCaCertificateAlias.invoke(enterpriseConfig);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClientCertificateAlias(WifiConfiguration config, String clientCert) {
        try {
            setClientCertificateAlias.invoke(config.enterpriseConfig, clientCert);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getClientCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        try {
            return (String) getClientCertificateAlias.invoke(enterpriseConfig);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDisableReason(WifiConfiguration config) {
        try {
            return disableReason.getInt(config);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return WifiConfigurationHelper.DISABLED_UNKNOWN_REASON;
    }
}
