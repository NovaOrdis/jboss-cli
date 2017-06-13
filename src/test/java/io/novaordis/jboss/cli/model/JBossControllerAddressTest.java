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

package io.novaordis.jboss.cli.model;

import io.novaordis.utilities.address.AddressException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public class JBossControllerAddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void defaults() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        assertEquals(JBossControllerAddress.PROTOCOL, a.getProtocol());
        assertEquals(JBossControllerAddress.DEFAULT_HOST, a.getHost());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
    }

    @Test
    public void host() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://something");
        assertEquals("something", a.getHost());
    }

    @Test
    public void host_Default() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(
                "jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":" + JBossControllerAddress.DEFAULT_PORT);

        assertEquals(JBossControllerAddress.DEFAULT_HOST, a.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
    }

    @Test
    public void host_Null() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://:" + JBossControllerAddress.DEFAULT_PORT);
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid"));
            assertTrue(msg.contains("host"));
            assertTrue(msg.contains("specification"));
        }
    }

    @Test
    public void port() throws Exception {

        JBossControllerAddress a =
                new JBossControllerAddress("jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":1111");

        assertEquals(1111, a.getPort().intValue());
    }

    @Test
    public void port_DefaultPort() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(
                "jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":" + JBossControllerAddress.DEFAULT_PORT);

        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
    }

    @Test
    public void port_DefaultPort2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://somehost");
        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
    }

    @Test
    public void attemptToSetInvalidPortValue1() throws Exception {

        try {
            new JBossControllerAddress("jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":-1");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }

    @Test
    public void attemptToSetInvalidPortValue2() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":0");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }

    @Test
    public void attemptToSetInvalidPortValue3() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://" + JBossControllerAddress.DEFAULT_HOST + ":65536");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }

    @Test
    public void setUsernameAndPassword() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(
                "jbosscli://somebody:abc@" + JBossControllerAddress.DEFAULT_HOST + ":" +
                        JBossControllerAddress.DEFAULT_PORT);

        assertEquals("somebody", a.getUsername());

        char[] p = a.getPassword();
        assertEquals(3, p.length);
        assertEquals('a', p[0]);
        assertEquals('b', p[1]);
        assertEquals('c', p[2]);
    }

    @Test
    public void constructor() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(
                "jbosscli://something:"  + JBossControllerAddress.DEFAULT_PORT);

        assertEquals("something", a.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
        assertEquals("something:" + JBossControllerAddress.DEFAULT_PORT, a.toString());
    }

    @Test
    public void constructor2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://something:1");

        assertEquals("something", a.getHost());
        assertEquals(1, a.getPort().intValue());
        assertEquals("something:1", a.toString());
    }

    @Test
    public void constructor3() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://someuser:abc@something:1");

        assertEquals("something", a.getHost());
        assertEquals(1, a.getPort().intValue());
        assertEquals("someuser", a.getUsername());
        char[] password2 = a.getPassword();
        assertEquals(3, password2.length);
        assertEquals('a', password2[0]);
        assertEquals('b', password2[1]);
        assertEquals('c', password2[2]);
        assertEquals("someuser:***@something:1", a.toString());
    }

    @Test
    public void constructor_nullHost() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://someuser:a@:1");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid"));
            assertTrue(msg.contains("host"));
            assertTrue(msg.contains("specification"));
        }
    }

    @Test
    public void constructor_invalidPort() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://somebody:a@something:-1");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("out of bounds"));
        }
    }

    @Test
    public void constructor_IfUsernamePresentPasswordCantBeNull() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://somebody@something:1");
            fail("should have thrown Exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing password"));
        }
    }

    @Test
    public void constructor_DefaultHost() throws Exception {

        JBossControllerAddress c = new JBossControllerAddress("jbosscli://");
        assertEquals(JBossControllerAddress.DEFAULT_HOST, c.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, c.getPort().intValue());
    }

    @Test
    public void constructor_DefaultHost2() throws Exception {

        JBossControllerAddress c = new JBossControllerAddress("");
        assertEquals(JBossControllerAddress.DEFAULT_HOST, c.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, c.getPort().intValue());
    }


    // equals() --------------------------------------------------------------------------------------------------------

    @Test
    public void equals_Default() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("admin", "password1", "mock-host", 12345);
        JBossControllerAddress a2 = new JBossControllerAddress("admin", "password2", "mock-host", 12345);

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void equals() throws Exception {

        //
        // we're equal even if the passwords are different
        //
        JBossControllerAddress a = new JBossControllerAddress("jbosscli://user1:a@host1:10");
        JBossControllerAddress a2 = new JBossControllerAddress("jbosscli://user1:b@host1:10");

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void notEquals() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://host1:10");
        JBossControllerAddress a2 = new JBossControllerAddress("jbosscli://user1:a@host1:10");

        assertFalse(a.equals(a2));
        assertFalse(a2.equals(a));
    }

    @Test
    public void notEquals2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://user1:a@host1:10");
        JBossControllerAddress a2 = new JBossControllerAddress("jbosscli://user1:a@host2:10");

        assertFalse(a.equals(a2));
        assertFalse(a2.equals(a));
    }

    @Test
    public void parseAddress_MissingPort() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://something:");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing port"));
        }
    }

    @Test
    public void parseAddress_PortNotAnInteger() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://something:blah");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid"));
            assertTrue(msg.contains("port"));
        }
    }

    @Test
    public void parseAddress_InvalidPort() throws Exception {

        try {

            new JBossControllerAddress("jbosscli://something:70000");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("70000"));
            assertTrue(msg.contains("out of bounds"));
        }
    }

    @Test
    public void parseAddress_HostAndPort() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("something:5");

        assertEquals("something", a.getHost());
        assertEquals(5, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("something:5", a.toString());
    }

    @Test
    public void parseAddress_Username_NoPassword() throws Exception {

        try {

            new JBossControllerAddress("some-user@some-host:1000");
            fail("should have failed exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing password"));
        }
    }

    @Test
    public void parseAddress_Username_EmptyPassword() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("some-user:@some-host:1000");

        char[] p = a.getPassword();
        assertEquals(0, p.length);
    }

    @Test
    public void parseAddress_UsernameAndPassword() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("some-user:abc@some-host:1000");

        assertEquals("some-host", a.getHost());
        assertEquals(1000, a.getPort().intValue());
        assertEquals("some-user", a.getUsername());
        char[] password = a.getPassword();
        assertEquals(3, password.length);
        assertEquals('a', password[0]);
        assertEquals('b', password[1]);
        assertEquals('c', password[2]);
        assertEquals("some-user:***@some-host:1000", a.toString());
    }

    @Test
    public void parseAddress_ProtocolPrefix() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("jbosscli://1.2.3.4");

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", a.getLiteral());
    }

    @Test
    public void parseAddress_InvalidProtocolPrefix() throws Exception {

        String s = "jmx://1.2.3.4";

        try {

            new JBossControllerAddress(s);
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid JBoss CLI protocol"));
        }
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral() throws Exception {

        String s = "admin:adminpasswd@1.2.3.4:9999";
        JBossControllerAddress a = new JBossControllerAddress(s);
        assertEquals("admin@1.2.3.4:9999", a.getLiteral());
    }

    @Test
    public void getLiteral2() throws Exception {

        String s = "1.2.3.4";
        JBossControllerAddress a = new JBossControllerAddress(s);
        assertEquals(s, a.getLiteral());
    }

    @Test
    public void getLiteral3() throws Exception {

        String s = "1.2.3.4:5555";
        JBossControllerAddress a = new JBossControllerAddress(s);
        assertEquals(s, a.getLiteral());
    }

    // copy() ----------------------------------------------------------------------------------------------------------

    @Test
    public void copy_Mutate() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        a.setPort(56789);

        JBossControllerAddress copy = a.copy();

        assertEquals(56789, copy.getPort().intValue());

        copy.setPort(56790);

        assertEquals(56789, a.getPort().intValue());
        assertEquals(56790, copy.getPort().intValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
