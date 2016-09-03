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

import io.novaordis.jboss.cli.JBossCliException;
import io.novaordis.jboss.cli.JBossControllerClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(JBossControllerAddressTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void defaults() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        assertEquals(JBossControllerClient.DEFAULT_HOST, a.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
    }

    @Test
    public void setHost() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        a.setHost("something");
        assertEquals("something", a.getHost());
    }

    @Test
    public void attemptToSetNullHost() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        try {
            a.setHost(null);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
        }
    }

    @Test
    public void setPort() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        a.setPort(1111);
        assertEquals(1111, a.getPort());
    }

    @Test
    public void attemptToSetInvalidPortValue1() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        try {
            a.setPort(-1);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid port value -1", msg);
        }
    }

    @Test
    public void attemptToSetInvalidPortValue2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        try {
            a.setPort(0);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid port value 0", msg);
        }
    }

    @Test
    public void attemptToSetInvalidPortValue3() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        try {
            a.setPort(65536);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid port value 65536", msg);

        }
    }

    @Test
    public void setUsernameAndPassword() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();

        a.setUsername("somebody");
        a.setPassword(new char[]{'a', 'b', 'c'});

        assertEquals("somebody", a.getUsername());

        char[] p = a.getPassword();
        assertEquals(3, p.length);
        assertEquals('a', p[0]);
        assertEquals('b', p[1]);
        assertEquals('c', p[2]);
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("something");

        assertEquals("something", a.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, a.getPort());
        assertEquals("something:" + JBossControllerClient.DEFAULT_PORT, a.toString());
    }

    @Test
    public void constructor2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("something", 1);

        assertEquals("something", a.getHost());
        assertEquals(1, a.getPort());
        assertEquals("something:1", a.toString());
    }

    @Test
    public void constructor3() throws Exception {

        char[] password = new char[] { 'a', 'b', 'c' };

        JBossControllerAddress a = new JBossControllerAddress("someuser", password, "something", 1);

        assertEquals("something", a.getHost());
        assertEquals(1, a.getPort());
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

            new JBossControllerAddress("somebody", new char[] {'a'}, null, 10);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("null host"));
        }
    }

    @Test
    public void constructor_invalidPort() throws Exception {

        try {

            new JBossControllerAddress("somebody", new char[] {'a'}, "something", -1);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_IfUsernamePresentPasswordCantBeNull() throws Exception {

        try {

            new JBossControllerAddress("somebody", null, "something", 1);
            fail("should have thrown Exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("null password"));
        }
    }

    @Test
    public void constructor_IfUsernameNotPresentPasswordCanBeNotNull() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(null, new char[] { 'a' } , "something", 1);

        assertNull(a.getUsername());
    }

    // equals() --------------------------------------------------------------------------------------------------------

    @Test
    public void equals_Default() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress();
        JBossControllerAddress a2 = new JBossControllerAddress();

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void equals() throws Exception {

        //
        // we're equal even if the passwords are different
        //
        JBossControllerAddress a = new JBossControllerAddress("user1", new char[] { 'a' }, "host1", 10);
        JBossControllerAddress a2 = new JBossControllerAddress("user1", new char[] { 'b' }, "host1", 10);

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void notEquals() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress(null, null, "host1", 10);
        JBossControllerAddress a2 = new JBossControllerAddress("user1", new char[] { 'a' }, "host1", 10);

        assertFalse(a.equals(a2));
        assertFalse(a2.equals(a));
    }

    @Test
    public void notEquals2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("user1", new char[] { 'a' }, "host1", 10);
        JBossControllerAddress a2 = new JBossControllerAddress("user1", new char[] { 'a' }, "host2", 10);

        assertFalse(a.equals(a2));
        assertFalse(a2.equals(a));
    }

    // parseAddress() --------------------------------------------------------------------------------------------------

    @Test
    public void parseAddress() throws Exception {

        JBossControllerAddress a = JBossControllerAddress.parseAddress("something");

        assertEquals("something", a.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("something:" + JBossControllerClient.DEFAULT_PORT, a.toString());
    }

    @Test
    public void parseAddress_MissingPort() throws Exception {

        try {
            JBossControllerAddress.parseAddress("something:");
            fail("should have thrown exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("missing port information", msg);
        }
    }

    @Test
    public void parseAddress_InvalidPort() throws Exception {

        try {
            JBossControllerAddress.parseAddress("something:blah");
            fail("should have thrown exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid port value \"blah\"" , msg);
        }
    }

    @Test
    public void parseAddress_HostAndPort() throws Exception {

        JBossControllerAddress a = JBossControllerAddress.parseAddress("something:5");

        assertEquals("something", a.getHost());
        assertEquals(5, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("something:5", a.toString());
    }

    @Test
    public void parseAddress_Username_NoPassword() throws Exception {

        try {

            JBossControllerAddress.parseAddress("some-user@some-host:1000");
            fail("should have failed exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("no password specified"));
        }
    }

    @Test
    public void parseAddress_Username_EmptyPassword() throws Exception {

        try {

            JBossControllerAddress.parseAddress("some-user:@some-host:1000");
            fail("should have failed exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("empty password"));
        }
    }

    @Test
    public void parseAddress_UsernameAndPassword() throws Exception {

        JBossControllerAddress a = JBossControllerAddress.parseAddress("some-user:abc@some-host:1000");

        assertEquals("some-host", a.getHost());
        assertEquals(1000, a.getPort());
        assertEquals("some-user", a.getUsername());
        char[] password = a.getPassword();
        assertEquals(3, password.length);
        assertEquals('a', password[0]);
        assertEquals('b', password[1]);
        assertEquals('c', password[2]);
        assertEquals("some-user:***@some-host:1000", a.toString());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
