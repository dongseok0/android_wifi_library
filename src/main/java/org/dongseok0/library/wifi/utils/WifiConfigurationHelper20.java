package org.dongseok0.library.wifi.utils;

import android.net.LinkAddress;
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
public class WifiConfigurationHelper20 extends WifiConfigurationHelper {
    private Constructor     staticIpConfigurationCtr;
    private Object[]        proxySettingsConsts;
    private Field           proxySettings;
    private Object[]        ipAssignmentConsts;
    private Field           ipAssignment;
    private Field           ipConfiguration;

    public WifiConfigurationHelper20() {
        super();

        try {
            ipConfiguration = WifiConfiguration.class.getDeclaredField("mIpConfiguration");
            ipConfiguration.setAccessible(true);

            ipAssignment = ipConfiguration.getType().getDeclaredField("ipAssignment");
            ipAssignmentConsts = ipAssignment.getType().getEnumConstants();

            proxySettings = ipConfiguration.getType().getDeclaredField("proxySettings");
            proxySettingsConsts = proxySettings.getType().getEnumConstants();

            Field staticIpConfigurationField = ipConfiguration.getType().getDeclaredField("staticIpConfiguration");
            staticIpConfigurationCtr = staticIpConfigurationField.getType().getDeclaredConstructor();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setIpProxy(WifiConfiguration config, JSONObject jsonConfig) {
        try {
            Object mIpConfiguration = ipConfiguration.get(config);
            Object staticIpConfiguration = staticIpConfigurationCtr.newInstance();

            if (jsonConfig.has("ip") && jsonConfig.has("prefix_length")) {
                InetAddress ipAddress = NetworkUtils.numericToInetAddress(jsonConfig.getString("ip"));
                int prefixLength = jsonConfig.getInt("prefix_length");

                Constructor linkAddressCtr = LinkAddress.class.getDeclaredConstructor(InetAddress.class, int.class);
                LinkAddress linkAddress = (LinkAddress) linkAddressCtr.newInstance(ipAddress, prefixLength);

                Field ipAddressField = staticIpConfiguration.getClass().getDeclaredField("ipAddress");
                ipAddressField.set(staticIpConfiguration, linkAddress);

                ipAssignment.set(mIpConfiguration, ipAssignmentConsts[0]);        // static
            } else {
                ipAssignment.set(mIpConfiguration, ipAssignmentConsts[1]);        // dhcp
            }


            if (jsonConfig.has("gateway")) {
                Field gatewayField = staticIpConfiguration.getClass().getDeclaredField("gateway");
                InetAddress gateway = NetworkUtils.numericToInetAddress(jsonConfig.getString("gateway"));
                gatewayField.set(staticIpConfiguration, gateway);
            }

            Object dnsServers = staticIpConfiguration.getClass().getField("dnsServers").get(staticIpConfiguration);
            Method add = dnsServers.getClass().getDeclaredMethod("add", Object.class);
            if (jsonConfig.has("dns1")) {
                InetAddress dns1 = NetworkUtils.numericToInetAddress(jsonConfig.getString("dns1"));
                add.invoke(dnsServers, dns1);
            }

            if (jsonConfig.has("dns2")) {
                InetAddress dns2 = NetworkUtils.numericToInetAddress(jsonConfig.getString("dns2"));
                add.invoke(dnsServers, dns2);
            }

            if (jsonConfig.has("proxy_host")) {
                String host = jsonConfig.getString("proxy_host");
                String port = jsonConfig.getString("proxy_port");
                String exclusionList = jsonConfig.getString("exclusion_list");

                setProxyFields(config, host, port, exclusionList);

                proxySettings.set(mIpConfiguration, proxySettingsConsts[1]);      // static
            } else {
                proxySettings.set(mIpConfiguration, proxySettingsConsts[0]);      // none
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getProxyFields(WifiConfiguration config) {
        try {
            Object mIpConfiguration = ipConfiguration.get(config);

            Object httpProxy = mIpConfiguration.getClass().getDeclaredField("httpProxy").get(mIpConfiguration);

            if (httpProxy != null) {
                String host = (String) httpProxy.getClass().getDeclaredMethod("getHost").invoke(httpProxy);
                String port = Integer.toString((int) httpProxy.getClass().getDeclaredMethod("getPort").invoke(httpProxy));
                String exclusionList = (String) httpProxy.getClass().getDeclaredMethod("getExclusionListAsString").invoke(httpProxy);
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
            Object mIpConfiguration = ipConfiguration.get(config);

            Field httpProxy = mIpConfiguration.getClass().getDeclaredField("httpProxy");

            Constructor proxyInfoCtr = httpProxy.getType().getDeclaredConstructor(String.class, int.class, String.class);
            Object proxyInfo = proxyInfoCtr.newInstance(host, Integer.parseInt(port), exclusionList);

            httpProxy.set(mIpConfiguration, proxyInfo);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
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
            Object mIpConfiguration = ipConfiguration.get(config);
            return IpAssignment.values()[((Enum) ipAssignment.get(mIpConfiguration)).ordinal()];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return IpAssignment.DHCP;
    }

    public ProxySettings getProxySettings(WifiConfiguration config) {
        try {
            Object mIpConfiguration = ipConfiguration.get(config);
            return ProxySettings.values()[((Enum) proxySettings.get(mIpConfiguration)).ordinal()];
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ProxySettings.NONE;
    }
}
