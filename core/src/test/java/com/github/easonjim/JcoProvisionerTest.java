package com.github.easonjim;

import java.io.File;
import java.util.UUID;
import java.util.logging.LogManager;

import com.sap.conn.jco.rt.JCoRuntime;
import com.sap.conn.jco.rt.JCoRuntimeFactory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify jco loader operation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JcoProvisionerTest {

    static {
        /** Redirect JUL to SLF4J. */
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void provisionerTest() throws Exception {
        JcoProvisioner.provision();
        boolean nativeLoaded = JcoProvisioner.isNativeLoaded();
        assertTrue(nativeLoaded);
    }


    @Test
    public void Discover_Location_Via_Command_Options() {
        final String folder = UUID.randomUUID().toString();
        final String options = JcoProvisioner.SYSTEM_PROPERTY_TARGET + "=" + folder;
        assertEquals(JcoProvisioner.discoverLocation(options), folder);
    }

    @Test
    public void Discover_Location_Via_Default_Hardcoded_Value() {
        assertEquals(JcoProvisioner.discoverLocation(null),
                JcoProvisioner.DEFAULT_LOCATION_TARGET);
    }

    @Test
    public void Discover_Location_Via_Environment_Variable() {
        assertNull(System.getenv(JcoProvisioner.ENVIRONMENT_VARIABLE_TARGET));
        final String variable = UUID.randomUUID().toString();
        JDK.setEnv(JcoProvisioner.ENVIRONMENT_VARIABLE_TARGET, variable);
        assertEquals(JcoProvisioner.discoverLocation(null), variable);
        JDK.setEnv(JcoProvisioner.ENVIRONMENT_VARIABLE_TARGET, null);
        assertNull(System.getenv(JcoProvisioner.ENVIRONMENT_VARIABLE_TARGET));
    }

    @Test
    public void Discover_Location_Via_System_Property() {
        assertNull(System.getProperty(JcoProvisioner.SYSTEM_PROPERTY_TARGET));
        final String property = UUID.randomUUID().toString();
        System.setProperty(JcoProvisioner.SYSTEM_PROPERTY_TARGET, property);
        assertEquals(JcoProvisioner.discoverLocation(null), property);
        System.clearProperty(JcoProvisioner.SYSTEM_PROPERTY_TARGET);
        assertNull(System.getProperty(JcoProvisioner.SYSTEM_PROPERTY_TARGET));
    }

    @Test(expected = Throwable.class)
    public void T1_Not_Yet_Provisioned() throws Exception {
        JCoRuntime runtime = JCoRuntimeFactory.getRuntime();
        assertTrue(runtime != null);
    }

    @Test
    public void T2_Provision_Once() throws Exception {

        assertFalse(JcoProvisioner.isNativeLoaded());

        final File location = new File("target/native");
        JcoProvisioner.provision(location, null);

        assertTrue(JcoProvisioner.isNativeLoaded());
    }

    @Test
    public void T3_Provision_Again() throws Exception {

        assertTrue(JcoProvisioner.isNativeLoaded());

        JcoProvisioner.provision(new File("target/native1"), null);
        JcoProvisioner.provision(new File("target/native2"), null);
        JcoProvisioner.provision(new File("target/native3"), null);

        assertTrue(JcoProvisioner.isNativeLoaded());

    }
}
