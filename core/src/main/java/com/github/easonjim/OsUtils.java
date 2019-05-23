package com.github.easonjim;

/**
 * OS Utils
 * <p>
 * Use for check OS version.
 * </p>
 *
 * @author jim
 */
public class OsUtils {
    private static String OS = null;

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static String getOsShotName() {
        if (isWindows()) {
            return "windows";
        } else if (isLinux()) {
            return "linux";
        } else if (isMac()) {
            return "mac";
        }
        return "linux";
    }

    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith("windows");
    }

    public static boolean isLinux() {
        return getOsName().toLowerCase().startsWith("linux");
    }

    public static boolean isMac() {
        return getOsName().toLowerCase().startsWith("mac");
    }
}

