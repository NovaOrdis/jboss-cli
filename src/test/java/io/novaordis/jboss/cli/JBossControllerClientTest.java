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
import org.jboss.as.cli.CommandContextFactory;
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

        JBossControllerAddress address = new JBossControllerAddress(
                "test-user", new char[] { 't', 'e', 's', 't'}, "test-host", "test-host", 7777, "7777");

        JBossControllerClient c = JBossControllerClient.getInstance(address);

        assertNotNull(c);
        assertTrue(c instanceof JBossControllerClientImpl);

        JBossControllerAddress address2 = c.getControllerAddress();
        assertEquals("test-user", address2.getUsername());
        char[] password = address2.getPassword();
        assertEquals(4, password.length);
        assertEquals('t', password[0]);
        assertEquals('e', password[1]);
        assertEquals('s', password[2]);
        assertEquals('t', password[3]);
        assertEquals("test-host", address2.getHost());
        assertEquals("test-host", address2.getHostLiteral());
        assertEquals(7777, address2.getPort());
        assertEquals("7777", address2.getPortLiteral());
    }

    @Test
    public void getInstance_ConfiguredImplementationClass() throws Exception {

        System.setProperty(
                JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME,
                MockJBossControllerClient.class.getName());

        try {

            JBossControllerAddress address = new JBossControllerAddress(
                    "test-user-2", new char[] { 't', 'e', 's', 't'}, "test-host-2", "test-host-2", 8888, "8888");


            JBossControllerClient c = JBossControllerClient.getInstance(address);

            assertNotNull(c);
            assertTrue(c instanceof MockJBossControllerClient);

            JBossControllerAddress address2 = c.getControllerAddress();
            assertEquals("test-user-2", address2.getUsername());
            char[] password = address2.getPassword();
            assertEquals(4, password.length);
            assertEquals('t', password[0]);
            assertEquals('e', password[1]);
            assertEquals('s', password[2]);
            assertEquals('t', password[3]);
            assertEquals("test-host-2", address2.getHost());
            assertEquals("test-host-2", address2.getHostLiteral());
            assertEquals(8888, address2.getPort());
            assertEquals("8888", address2.getPortLiteral());
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

            JBossControllerAddress address = new JBossControllerAddress(
                    "test-user-2", new char[] { 't', 'e', 's', 't'}, "test-host-2", "test-host-2", 8888, "8888");

            JBossControllerClient.getInstance(address);
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

    // getAttributeValue() ---------------------------------------------------------------------------------------------

    @Test
    public void getAttributeValue_String() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        MockCommandContext mcc = new MockCommandContext();
        MockCommandContextFactory mccf = new MockCommandContextFactory(mcc);
        c.setCommandContextFactory(mccf);

        //
        // install path and attribute
        //

        mcc.install("/a=b", "c", "d");

        c.connect();

        Object o = c.getAttributeValue("/a=b", "c");

        assertEquals("d", o);

        c.disconnect();
    }

    @Test
    public void getAttributeValue_Int() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        MockCommandContext mcc = new MockCommandContext();
        MockCommandContextFactory mccf = new MockCommandContextFactory(mcc);
        c.setCommandContextFactory(mccf);

        //
        // install path and attribute
        //

        mcc.install("/a=b", "c", 1);

        c.connect();

        Object o = c.getAttributeValue("/a=b", "c");

        assertEquals(1, o);

        c.disconnect();
    }

    @Test
    public void getAttributeValue_ValidPath_NoSuchAttribute() throws Exception {

        //
        // must return null
        //

        JBossControllerClient c = getJBossControllerClientToTest();

        MockCommandContext mcc = new MockCommandContext();
        MockCommandContextFactory mccf = new MockCommandContextFactory(mcc);
        c.setCommandContextFactory(mccf);

        //
        // install path and a different attribute
        //

        mcc.install("/a=b", "x", "y");

        c.connect();

        Object o = c.getAttributeValue("/a=b", "c");

        assertNull(o);

        c.disconnect();
    }

    @Test
    public void getAttributeValue_NoSuchPath() throws Exception {

        //
        // must throw exception
        //

        JBossControllerClient c = getJBossControllerClientToTest();

        MockCommandContext mcc = new MockCommandContext();
        MockCommandContextFactory mccf = new MockCommandContextFactory(mcc);
        c.setCommandContextFactory(mccf);

        //
        // install a different path and a different attribute
        //

        mcc.install("/a=d", "x", "y");

        c.connect();

        try {
            c.getAttributeValue("/a=b", "c");
            fail("should throw exception");
        }
        catch(JBossCliException e) {
            String s = e.getMessage();
            log.info(s);
            assertEquals("JBAS014883: No resource definition is registered for address [(\"a\" => \"b\")]", s);
        }

        c.disconnect();
    }

    // setCommandContextFactory() --------------------------------------------------------------------------------------

    @Test
    public void setCommandContextFactory_NotACommandContextFactoryInstance() throws Exception {

        JBossControllerClient c = getJBossControllerClientToTest();

        try {
            c.setCommandContextFactory(new Object());
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("is not a " + CommandContextFactory.class.getName() + " instance"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JBossControllerClient getJBossControllerClientToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
