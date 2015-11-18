package org.dongseok0.library.wifi.wificonfiguration;

import android.net.LinkAddress;
import android.net.RouteInfo;
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
public class WifiConfigurationHelper16 extends WifiConfigurationHelper {
    private Method getPort;
    private Method getExclusionList;
    private Method getHost;
    private Method setHttpProxy;
    private Method getHttpProxy;
    private Constructor proxyPropertiesCtr;
    private Field fieldLinkProperties;
    private Field proxySettings;
    private Object[] proxySettingsConsts;
    private Field ipAssignment;
    private Object[] ipAssignmentConsts;

    public WifiConfigurationHelper16() {
        super();
        try {
            fieldLinkProperties = WifiConfiguration.class.getDeclaredField("linkProperties");

            ipAssignment = WifiConfiguration.class.getDeclaredField("ipAssignment");
            ipAssignmentConsts = ipAssignment.getType().getEnumConstants();

            proxySettings = WifiConfiguration.class.getDeclaredField("proxySettings");
            proxySettingsConsts = proxySettings.getType().getEnumConstants();

            Class proxyPropertiesCls = Class.forName("android.net.ProxyProperties");
            proxyPropertiesCtr = proxyPropertiesCls.getDeclaredConstructor(String.class, int.class, String.class);

            getHttpProxy = fieldLinkProperties.getType().getDeclaredMethod("getHttpProxy");
            setHttpProxy = fieldLinkProperties.getType().getDeclaredMethod("setHttpProxy", proxyPropertiesCls);

            getHost = proxyPropertiesCls.getDeclaredMethod("getHost");
            getPort = proxyPropertiesCls.getDeclaredMethod("getPort");
            getExclusionList = proxyPropertiesCls.getDeclaredMethod("getExclusionList");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setIpProxy(WifiConfiguration config, JSONObject jsonConfig) {
        try {
            Object linkProperties = fieldLinkProperties.get(config);

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
            Object linkProperties = fieldLinkProperties.get(config);
            Object proxyProperties = getHttpProxy.invoke(linkProperties);
            if (proxyProperties != null) {
                String host = (String) getHost.invoke(proxyProperties);
                String port = Integer.toString((int) getPort.invoke(proxyProperties));
                String exclusionList = (String) getExclusionList.invoke(proxyProperties);
                return new String[]{host, port, exclusionList};
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setProxyFields(WifiConfiguration config, String host, String port, String exclusionList) {
        try {
            Object linkProperties = fieldLinkProperties.get(config);
            setHttpProxy.invoke(linkProperties, proxyPropertiesCtr.newInstance(host, Integer.parseInt(port), exclusionList));
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public IpAssignment getIpAssignment(WifiConfiguration config) {
        try {
            return IpAssignment.values()[((Enum) ipAssignment.get(config)).ordinal()];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return IpAssignment.DHCP;
    }

    public ProxySettings getProxySettings(WifiConfiguration config) {
        try {
            return ProxySettings.values()[((Enum) proxySettings.get(config)).ordinal()];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ProxySettings.NONE;
    }
}
