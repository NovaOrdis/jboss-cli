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
     * <p>
     * Important: each invocation will return a <b>new</b> instance, so if the calling layer wants to maintain the
     * instance state, it must cache it.
     *
     * @throws IllegalStateException if there is anything that prevents the factory method from producing an
     *                               instance.
     */
    static JBossControllerClient getInstance(JBossControllerAddress address) throws IllegalStateException {

        String className = System.getProperty(JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME);

        JBossControllerClient client;

        if (className == null) {

            client = new JBossControllerClientImpl();
            log.debug("jboss controller client default implementation" + client);
        }
        else {

            try {

                log.debug("jboss controller client implementation class name: " + className);

                Class c = Class.forName(className);
                client = (JBossControllerClient) c.newInstance();

            } catch (Exception e) {

                throw new IllegalStateException(e);
            }
        }

        if (address == null) {

            address = new JBossControllerAddress(
                    null, null, JBossControllerAddress.DEFAULT_HOST, JBossControllerAddress.DEFAULT_PORT);

            log.debug("using default address " + address);
        }

        client.setControllerAddress(address);

        log.debug("jboss controller address instance " + client);
        return client;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    String getHost();

    int getPort();

    String getUsername();

    char[] getPassword();

    void setControllerAddress(JBossControllerAddress a);

    JBossControllerAddress getControllerAddress();

    /**
     * Connects the client to the controller.
     *
     * The client instance must be configured before attempting to connect it, by using the setControllerAddress()
     * method. The setControllerAddress() is not invoked, the default are used:
     *
     * host: "localhost"
     * port: 9999
     * username: null (local connection attempted)
     * password: null
     *
     * @see JBossControllerClient#setControllerAddress(JBossControllerAddress)
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
     * Sends a command into the controller that returns the value of the specified attribute.
     *
     * If the path is valid but the attribute does not exist, or is undefined, them method will return null. If the
     * attribute exists, and it is defined, the returned object instance could be a String, Integer, etc. depending on
     * the type of the ModelNode returned by the controller. If the path does not exist, the method will throw an
     * exception, see below.
     *
     * @throws JBossCliException if the path is invalid (does not exist on controller)
     */
    Object getAttributeValue(String path, String attributeName) throws JBossCliException;

    /**
     * Install a custom command context factory. If not installed, CommandContextFactory.getInstance() is used.
     *
     * The method is not typed, so we can avoid declaring dependencies.
     *
     * @exception IllegalArgumentException if the argument is not a CommandContextFactory instance.
     */
    void setCommandContextFactory(Object commandContextFactory);
}