/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.jboss.cli;

import io.novaordis.jboss.cli.model.JBossControllerAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public abstract class JBossControllerClientTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossControllerClientTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void configuration() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        assertEquals(JBossControllerClient.DEFAULT_HOST, c.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, c.getPort());
        assertNull(c.getUsername());
        assertNull(c.getPassword());

        JBossControllerAddress a = new JBossControllerAddress(
                "somebody", new char[] { 'a', 'b', 'c', 'd'}, "something", "something", 1234, "1234");
        c.setControllerAddress(a);

        assertEquals("something", c.getHost());
        assertEquals(1234, c.getPort());
        assertEquals("somebody", c.getUsername());
        char[] p = c.getPassword();
        assertEquals(4, p.length);
        assertEquals('a', p[0]);
        assertEquals('b', p[1]);
        assertEquals('c', p[2]);
        assertEquals('d', p[3]);
    }

    @Test
    public void getAttributeBeforeConnecting() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        assertFalse(c.isConnected());

        try {
            c.getAttributeValue("/", "release-version");
            fail("should have thrown exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("not connected"));
        }
    }

    // getInstance() ---------------------------------------------------------------------------------------------------

    @Test
    public void getInstance_DefaultBehavior() throws Exception {

        JBossControllerClient c = JBossControllerClient.getInstance();

        assertNotNull(c);
        assertTrue(c instanceof JBossControllerClientImpl);
    }

    @Test
    public void getInstance_ConfiguredImplementationClass() throws Exception {

        System.setProperty(
                JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME,
                MockJBossControllerClient.class.getName());

        try {

            JBossControllerClient c = JBossControllerClient.getInstance();

            assertNotNull(c);
            assertTrue(c instanceof MockJBossControllerClient);
        }
        finally {

            System.clearProperty(JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME);
        }
    }

    @Test
    public void getInstance_CustomClassNotFound() throws Exception {

        System.setProperty(
                JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME,
                "there.is.no.such.Class");

        try {

            JBossControllerClient.getInstance();
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            ClassNotFoundException cnfe = (ClassNotFoundException)e.getCause();
            assertNotNull(cnfe);

            log.info(cnfe.getMessage());
        }
        finally {

            System.clearProperty(JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME);
        }
    }

    // setControllerAddress() ------------------------------------------------------------------------------------------

    @Test
    public void setControllerAddress_Null() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        try {
            c.setControllerAddress(null);
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("null controller address", msg);
        }
    }

    @Test
    public void setControllerAddress() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        JBossControllerAddress a = new JBossControllerAddress(
                "some-user", new char[] { 'a'}, "some-host", "some-host", 2, "2");

        c.setControllerAddress(a);

        assertEquals("some-user", c.getUsername());
        char[] p = c.getPassword();
        assertEquals(1, p.length);
        assertEquals('a', p[0]);
        assertEquals("some-host", c.getHost());
        assertEquals(2, c.getPort());

        JBossControllerAddress a2 = c.getControllerAddress();

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JBossControllerClient getJBossControllerClientToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
