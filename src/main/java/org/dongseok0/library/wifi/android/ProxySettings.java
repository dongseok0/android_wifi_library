package org.dongseok0.library.wifi.android;

public enum ProxySettings {
    /* No proxy is to be used. Any existing proxy settings
         * should be cleared. */
    NONE,
    /* Use statically configured proxy. Configuration can be accessed
     * with httpProxy. */
    STATIC,
    /* no proxy details are assigned, this is used to indicate
     * that any existing proxy settings should be retained */
    UNASSIGNED,
    /* Use a Pac based proxy.
     */
    PAC
}
