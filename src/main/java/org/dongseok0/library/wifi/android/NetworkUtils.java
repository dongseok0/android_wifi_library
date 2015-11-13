package org.dongseok0.library.wifi.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * Created by dongseok0 on 6/11/2015.
 */
public class NetworkUtils {
    private static Method numericToInetAddress;
    private static Method getNetworkPart;

    static {
        try {
            Class networkUtils = Class.forName("android.net.NetworkUtils");
            numericToInetAddress = networkUtils.getDeclaredMethod("numericToInetAddress", String.class);
            getNetworkPart = networkUtils.getDeclaredMethod("getNetworkPart", InetAddress.class, int.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static InetAddress numericToInetAddress(String addr) {
        try {
            return (InetAddress) numericToInetAddress.invoke(null, addr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InetAddress getNetworkPart(InetAddress inetAddress, int prefixLength) {
        try {
            return (InetAddress) getNetworkPart.invoke(null, inetAddress, prefixLength);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
