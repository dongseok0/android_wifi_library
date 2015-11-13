package org.dongseok0.library.wifi.utils;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.wifi.*;
import android.net.wifi.WifiConfiguration;

import org.dongseok0.library.wifi.android.IpAssignment;
import org.dongseok0.library.wifi.android.NetworkUtils;
import org.dongseok0.library.wifi.android.ProxySettings;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * Created by dongseok0 on 6/11/2015.
 */
public class WifiConfigurationHelper {

    public void setIpProxy(WifiConfiguration config, JSONObject jsonConfig) {
        try {
            LinkProperties linkProperties = (LinkProperties) config.getClass().getField("linkProperties").get(config);

            Field ipAssignment = config.getClass().getDeclaredField("ipAssignment");
            Object[] ipAssignmentConsts = ipAssignment.getType().getEnumConstants();
            if (jsonConfig.has("ip") && jsonConfig.has("prefix_length")) {
                InetAddress ipAddress = NetworkUtils.numericToInetAddress(jsonConfig.getString("ip"));
                int prefixLength = jsonConfig.getInt("prefix_length");

                Constructor linkAddressCtr = LinkAddress.class.getDeclaredConstructor(InetAddress.class, int.class);
                LinkAddress linkAddress = (LinkAddress) linkAddressCtr.newInstance(ipAddress, prefixLength);

                Method addLinkAddress = linkProperties.getClass().getDeclaredMethod("addLinkAddress", LinkAddress.class);
                addLinkAddress.invoke(linkProperties, linkAddress);

                ipAssignment.set(config, ipAssignmentConsts[0]);        // static
            } else {
                ipAssignment.set(config, ipAssignmentConsts[1]);        // dhcp
            }


            if (jsonConfig.has("gateway")) {
                Constructor routeInfoCtr = RouteInfo.class.getDeclaredConstructor(InetAddress.class);
                InetAddress gateway = NetworkUtils.numericToInetAddress(jsonConfig.getString("gateway"));
                RouteInfo routeInfo = (RouteInfo) routeInfoCtr.newInstance(gateway);

                Method addRoute = linkProperties.getClass().getDeclaredMethod("addRoute", RouteInfo.class);
                addRoute.invoke(linkProperties, routeInfo);
            }

            Method addDns = linkProperties.getClass().getDeclaredMethod("addDns", InetAddress.class);
            if (jsonConfig.has("dns1")) {
                InetAddress dns1 = NetworkUtils.numericToInetAddress(jsonConfig.getString("dns1"));
                addDns.invoke(linkProperties, dns1);
            }

            if (jsonConfig.has("dns2")) {
                InetAddress dns2 = NetworkUtils.numericToInetAddress(jsonConfig.getString("dns2"));
                addDns.invoke(linkProperties, dns2);
            }

            Field proxySettings = config.getClass().getDeclaredField("proxySettings");
            Object[] proxySettingsConsts = proxySettings.getType().getEnumConstants();
            if (jsonConfig.has("proxy_host")) {
                String host = jsonConfig.getString("proxy_host");
                String port = jsonConfig.getString("proxy_port");
                String exclusionList = jsonConfig.getString("exclusion_list");

                setProxyFields(config, host, port, exclusionList);

                proxySettings.set(config, proxySettingsConsts[1]);      // static
            } else {
                proxySettings.set(config, proxySettingsConsts[0]);      // none
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getProxyFields(WifiConfiguration config) {
        try {
            Object linkProperties = config.getClass().getField("linkProperties").get(config);
            Object proxyProperties = linkProperties.getClass().getDeclaredMethod("getHttpProxy").invoke(linkProperties);
            if (proxyProperties != null) {
                String host = (String) proxyProperties.getClass().getDeclaredMethod("getHost").invoke(proxyProperties);
                String port = Integer.toString((int) proxyProperties.getClass().getDeclaredMethod("getPort").invoke(proxyProperties));
                String exclusionList = (String) proxyProperties.getClass().getDeclaredMethod("getExclusionList").invoke(proxyProperties);
                return new String[]{host, port, exclusionList};
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setProxyFields(WifiConfiguration config, String host, String port, String exclusionList) {
        try {
            Object linkProperties = config.getClass().getField("linkProperties").get(config);
            Class proxyPropertiesCls = Class.forName("android.net.ProxyProperties");
            Constructor proxyPropertiesCtr = proxyPropertiesCls.getDeclaredConstructor(String.class, int.class, String.class);

            Method setHttpProxy = linkProperties.getClass().getDeclaredMethod("setHttpProxy", proxyPropertiesCls);
            setHttpProxy.invoke(linkProperties, proxyPropertiesCtr.newInstance(host, Integer.parseInt(port), exclusionList));
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public IpAssignment getIpAssignment(WifiConfiguration config) {
        try {
            return IpAssignment.values()[((Enum) config.getClass().getDeclaredField("ipAssignment").get(config)).ordinal()];
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return IpAssignment.DHCP;
    }

    public ProxySettings getProxySettings(WifiConfiguration config) {
        try {
            return ProxySettings.values()[((Enum) config.getClass().getDeclaredField("proxySettings").get(config)).ordinal()];
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ProxySettings.NONE;
    }

    public void setCaCertificateAlias(WifiConfiguration config, String caCert) {
        try {
            Method setCaCertificateAlias = config.enterpriseConfig.getClass().getDeclaredMethod("setCaCertificateAlias", String.class);
            setCaCertificateAlias.invoke(config.enterpriseConfig, caCert);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getCaCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        try {
            Method getCaCertificateAlias = enterpriseConfig.getClass().getDeclaredMethod("getCaCertificateAlias");
            return (String) getCaCertificateAlias.invoke(enterpriseConfig);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClientCertificateAlias(WifiConfiguration config, String clientCert) {
        try {
            Method setClientCertificateAlias = config.enterpriseConfig.getClass().getDeclaredMethod("setClientCertificateAlias", String.class);
            setClientCertificateAlias.invoke(config.enterpriseConfig, clientCert);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getClientCertificateAlias(WifiEnterpriseConfig enterpriseConfig) {
        try {
            Method getClientCertificateAlias = enterpriseConfig.getClass().getDeclaredMethod("getClientCertificateAlias");
            return (String) getClientCertificateAlias.invoke(enterpriseConfig);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDisableReason(WifiConfiguration config) {
        try {
            return config.getClass().getField("disableReason").getInt(config);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return WifiConfigurationUtil.DISABLED_UNKNOWN_REASON;
    }
}
