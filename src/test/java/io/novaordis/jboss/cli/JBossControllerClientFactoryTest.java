/*
 * Copyright (c) 2017 Nova Ordis LLC
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/14/17
 */
public abstract class JBossControllerClientFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void buildControllerClient_Default() throws Exception {

        JBossControllerClientFactory f = getJBossControllerClientFactoryToTest();

        JBossControllerAddress defaultAddress = new JBossControllerAddress();

        JBossControllerClient c = f.buildControllerClient(defaultAddress);

        assertEquals(JBossControllerAddress.DEFAULT_HOST, c.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, c.getPort());
        assertNull(c.getUsername());
        assertNull(c.getPassword());

        JBossControllerAddress a2 = c.getControllerAddress();
        assertTrue(defaultAddress.equals(a2));

        assertFalse(c.isConnected());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract JBossControllerClientFactory getJBossControllerClientFactoryToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
