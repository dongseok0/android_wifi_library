package org.dongseok0.library.wifi.android;

/**
 * Created by dongseok0 on 5/11/2015.
 */
public enum IpAssignment {
    /* Use statically configured IP settings. Configuration can be accessed
    * with linkProperties */
    STATIC,
    /* Use dynamically configured IP settigns */
    DHCP,
    /* no IP details are assigned, this is used to indicate
     * that any existing IP settings should be retained */
    UNASSIGNED
}
