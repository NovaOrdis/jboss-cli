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

        c.setHost("something");
        assertEquals("something", c.getHost());

        c.setPort(1234);
        assertEquals(1234, c.getPort());

        c.setUsername("somebody");
        assertEquals("somebody", c.getUsername());

        c.setPassword(new char[] { 'a', 'b', 'c', 'd'});
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JBossControllerClient getJBossControllerClientToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
