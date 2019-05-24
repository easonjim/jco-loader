package com.github.easonjim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.sap.conn.jco.rt.JCoRuntime;
import com.sap.conn.jco.rt.JCoRuntimeFactory;

/**
 * Jco native library extractor and loader.
 * <p>
 * To provision jco native library at the given folder location:
 * <p>
 * <pre>
 * JcoProvisioner.provision(new File(location));
 * </pre>
 *
 * @author jim
 */
public class JcoProvisioner {

    /**
     * Provisioner logger.
     */
    private static final Logger logger = Logger.getLogger(JcoProvisioner.class.getName());

    /**
     * Location of native libraries.
     * <p>
     * <pre>
     * when stored in loader.jar:     /${LIB_DIR}/libsapjco3.so
     * after default load/extract:    ${user.dir}/${LIB_DIR}/libsapjco3.so
     * </pre>
     */
    public static final String LIB_DIR = "native";

    /**
     * Environment variable which provides jco extract location. for target.
     */
    public static final String ENVIRONMENT_VARIABLE_TARGET = "SAP_JCO_TARGET_FOLDER";

    /**
     * System property which provides jco extract location. for target.
     */
    public static final String SYSTEM_PROPERTY_TARGET = "sap.jco.target.folder";

    /**
     * Hard coded value which provides jco extract location. for target.
     */
    public static final String DEFAULT_LOCATION_TARGET = System.getProperty("user.dir") + File.separator + LIB_DIR;

    /**
     * Jco native path in system property location.
     */
    public static final String JCO_NATIVE_SYSTEM_PROPERTY = "java.library.path";

    /**
     * Jco native library names
     */
    public static final Map<String, String> JCO_NATIVE_LIBRARY_NAMES = new HashMap<String, String>(3) {
        {
            put("windows", "sapjco3.dll");
        }

        {
            put("linux", "libsapjco3.so");
        }

        {
            put("mac", "libsapjco3.jnilib");
        }
    };

    /**
     * Discover jco library extract location. <br>
     * Priority 1) user provided command line agent options. <br>
     * Priority 2) user provided o/s environment variable. <br>
     * Priority 3) user provided java system property. <br>
     * Priority 4) hard coded location in ${user.dir}/native. <br>
     *
     * @param options Command line agent options.
     * @return Discovered jco library extract location.
     */
    public static String discoverLocation(final String options) {

        /** Priority 1) user provided agent command line options. */
        if (options != null) {
            final String[] optionArray = options.split(",");
            for (final String option : optionArray) {
                if (option.startsWith(SYSTEM_PROPERTY_TARGET) && option.contains("=")) {
                    logger.info("Using location provided by options. for target.");
                    return option.substring(option.indexOf("=") + 1);
                }
            }
        }

        /** Priority 2) user provided o/s environment variable. */
        final String variable = System.getenv(ENVIRONMENT_VARIABLE_TARGET);
        if (variable != null) {
            logger.info("Using location provided by environment variable. for target. path:" + variable);
            return variable;
        }

        /** Priority 3) user provided java system property. */
        final String property = System.getProperty(SYSTEM_PROPERTY_TARGET);
        if (property != null) {
            logger.info("Using location provided by system property. for target. path:" + property);
            return property;
        }

        /** Priority 4) hard coded location. */
        logger.info("Using location provided by hard coded value. for target. path:" + DEFAULT_LOCATION_TARGET);
        return DEFAULT_LOCATION_TARGET;

    }

    /**
     * Verify if jco native library is loaded and operational.
     *
     * @return true, if is native library loaded.
     */
    public static synchronized boolean isNativeLoaded() {
        try {
            return isJcoAlreadyLoaded();
        } catch (final Throwable e) {
            try {
                return isJcoAlreadyLoaded();
            } catch (final Throwable ex) {
                logger.info("Jco Runtime load error:" + ex.getMessage());
                return false;
            }
        }
    }

    /**
     * Extract and load native jco library in the default folder.
     *
     * @throws Exception The provisioning failure exception.
     */
    public static void provision() throws Exception {
        provision(new File(discoverLocation(null)));
    }

    /**
     * Extract and load native jco library in the provided folder.
     *
     * @param targetFolder     Library extraction folder.
     * @throws Exception The provisioning failure exception.
     */
    public static synchronized void provision(final File targetFolder)
            throws Exception {

        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        /** Library name for given architecture. */
        final String libraryName = JCO_NATIVE_LIBRARY_NAMES.get(OsUtils.getOsShotName());

        /** Library location embedded in the jar class path. */
        final String sourcePath = "/" + LIB_DIR + "/" + libraryName;

        /** Absolute path to the extracted library the on file system. */
        final File targetPath = new File(targetFolder, libraryName).getAbsoluteFile();

        /** Extract library form the jar to the local file system. */
        final InputStream sourceStream = JcoProvisioner.class.getResourceAsStream(sourcePath);
        final OutputStream targetStream = new FileOutputStream(targetPath);
        transfer(sourceStream, targetStream);
        sourceStream.close();
        targetStream.close();

        /** get library via absolute path. Only get folder path, not get file full path. */
        final String libraryPath = targetPath.getAbsolutePath().replace(libraryName, "");

        /** Tell jco loader that the library is already loaded. */
        String jcoSystemNativePath = System.getProperty(JCO_NATIVE_SYSTEM_PROPERTY);
        if (OsUtils.isWindows()) {
            jcoSystemNativePath += ";" + libraryPath;
        } else {
            jcoSystemNativePath += ":" + libraryPath;
        }
        System.setProperty(JCO_NATIVE_SYSTEM_PROPERTY, jcoSystemNativePath);

        logger.info("Jco system native path: " + System.getProperty(JCO_NATIVE_SYSTEM_PROPERTY));

        if (isNativeLoaded()) {
            logger.info("Jco library is already provisioned.");
        }
    }

    /**
     * The Constant EOF.
     */
    static final int EOF = -1;

    /**
     * The Constant SIZE.
     */
    static final int SIZE = 64 * 1024;

    /**
     * Perform stream copy.
     *
     * @param input  The input stream.
     * @param output The output stream.
     * @throws Exception The stream copy failure exception.
     */
    public static void transfer(final InputStream input,
                                final OutputStream output) throws Exception {
        final byte[] data = new byte[SIZE];
        while (true) {
            final int count = input.read(data, 0, SIZE);
            if (count == EOF) {
                break;
            }
            output.write(data, 0, count);
        }
    }

    /**
     * Check silently if jco was loaded in order to avoid the UnsatisfiedLinkError: no sapjco3 in java.library.path.
     *
     * @return true, if Jco native library is present in the ClassLoader otherwise false.
     * @throws UnsatisfiedLinkError
     */
    private static boolean isJcoAlreadyLoaded() throws Exception {
        JCoRuntime runtime = JCoRuntimeFactory.getRuntime();
        return runtime != null;
    }
}
