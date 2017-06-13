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
import io.novaordis.utilities.address.AddressImpl;

/**
 * Encapsulates a hostname, port, username and password.
 *
 * Correctly implements equals() and hashCode();
 *
 * @see io.novaordis.utilities.address.Address
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public class JBossControllerAddress extends AddressImpl {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL = "jbosscli";

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 9999;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Defaults, implies jbosscli://localhost:9999
     */
    public JBossControllerAddress() {

        super(PROTOCOL, null, null, DEFAULT_HOST, DEFAULT_PORT);
    }

    public JBossControllerAddress(String username, String password, String host, Integer port) {

        super(PROTOCOL, username, password, host, port);
    }

    /**
     * @param address - accepts the following format [jbosscli://][[username:password]@]host[:port].
     */
    public JBossControllerAddress(String address) throws AddressException {

        super(address);

        String protocol = getProtocol();

        if (protocol == null) {

            setProtocol(PROTOCOL);
        }
        else if (!PROTOCOL.equals(protocol)) {

            throw new AddressException("invalid JBoss CLI protocol \"" + protocol + "\"");
        }

        if (getHost() == null) {

            setHost(DEFAULT_HOST);
        }

        if (getPort() == null) {

            setPort(DEFAULT_PORT);
        }
    }

    // AddressImpl overrides -------------------------------------------------------------------------------------------

    @Override
    public JBossControllerAddress copy() {

        return (JBossControllerAddress)super.copy();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JBossControllerAddress createBlankInstance() {

        return new JBossControllerAddress(null, null, null, null);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
