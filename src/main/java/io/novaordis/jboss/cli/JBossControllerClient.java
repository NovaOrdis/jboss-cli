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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateful JBoss controller client, requires connection via the connect() method, and it can be disconnected, thus
 * releasing resources. Local connection is possible, if the controller is located on the same host, for local
 * connection username and password should be null. The user should be part of the management realm.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public interface JBossControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    Logger log = LoggerFactory.getLogger(JBossControllerClient.class);

    String JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME = "io.novaordis.jboss.controller.client.impl";

    String DEFAULT_HOST = "localhost";
    int DEFAULT_PORT = 9999;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * If "io.novaordis.jboss.controller.client.impl" is set, will use the value as the implementation class name,
     * attempt to instantiate and return the instance. Otherwise, will return the default implementation.
     *
     * @exception IllegalStateException if there is anything that prevents the factory method from producing an
     * instance.
     */
    static JBossControllerClient getInstance() throws IllegalStateException {

        String className = System.getProperty(JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME);

        if (className == null) {

            return new JBossControllerClientImpl();
        }

        try {

            log.debug("jboss controller client implementation class name: " + className);

            Class c = Class.forName(className);

            return (JBossControllerClient)c.newInstance();
        }
        catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    void setHost(String host);
    String getHost();

    void setPort(int port);
    int getPort();

    void setUsername(String username);
    String getUsername();

    void setPassword(char[] password);
    char[] getPassword();

    /**
     * Connects the client to the controller.
     *
     * The client instance must be configured before attempting to connect it, by using the set...() methods.
     *
     * If no set...() method is invoked, the default are used:
     *
     * host: "localhost"
     * port: 9999
     * username: null (local connection attempted)
     * password: null
     *
     * @see JBossControllerClient#setHost(String)
     * @see JBossControllerClient#setPort(int)
     * @see JBossControllerClient#setUsername(String)
     * @see JBossControllerClient#setPassword(char[])
     *
     * @throws JBossCliException
     */
    void connect() throws JBossCliException;

    /**
     * Disconnects the client, and releases resources.
     */
    void disconnect();

    boolean isConnected();

    /**
     * Sends a command into the controller that returns the value of the specified attribute, as String.
     *
     * null can be returned if the attribute is not defined.
     *
     * @throws JBossCliException if the path is invalid (does not exist on controller)
     */
    String getAttributeValue(String path, String attributeName) throws JBossCliException;
}