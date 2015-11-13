package org.dongseok0.library.wifi.android;

/**
 * Created by dongseok0 on 5/11/2015.
 */
public class Credentials {
    public static final String INSTALL_ACTION = "android.credentials.INSTALL";

    public static final String INSTALL_AS_USER_ACTION = "android.credentials.INSTALL_AS_USER";

    public static final String UNLOCK_ACTION = "com.android.credentials.UNLOCK";

    /** Key prefix for CA certificates. */
    public static final String CA_CERTIFICATE = "CACERT_";

    /** Key prefix for user certificates. */
    public static final String USER_CERTIFICATE = "USRCERT_";

    /** Key prefix for user private keys. */
    public static final String USER_PRIVATE_KEY = "USRPKEY_";

    /** Key prefix for VPN. */
    public static final String VPN = "VPN_";

    /** Key prefix for WIFI. */
    public static final String WIFI = "WIFI_";

    /** Key containing suffix of lockdown VPN profile. */
    public static final String LOCKDOWN_VPN = "LOCKDOWN_VPN";

    /** Data type for public keys. */
    public static final String EXTRA_PUBLIC_KEY = "KEY";

    /** Data type for private keys. */
    public static final String EXTRA_PRIVATE_KEY = "PKEY";

    // historically used by Android
    public static final String EXTENSION_CRT = ".crt";
    public static final String EXTENSION_P12 = ".p12";
    // commonly used on Windows
    public static final String EXTENSION_CER = ".cer";
    public static final String EXTENSION_PFX = ".pfx";
}
