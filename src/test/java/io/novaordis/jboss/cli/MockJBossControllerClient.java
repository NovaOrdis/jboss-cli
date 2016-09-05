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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/3/16
 */
public class MockJBossControllerClient implements JBossControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JBossControllerAddress controllerAddress;

    // Constructors ----------------------------------------------------------------------------------------------------

    // JBossControllerClient implementation ----------------------------------------------------------------------------

    @Override
    public String getHost() {
        throw new RuntimeException("getHost() NOT YET IMPLEMENTED");
    }

    @Override
    public int getPort() {
        throw new RuntimeException("getPort() NOT YET IMPLEMENTED");
    }

    @Override
    public String getUsername() {
        throw new RuntimeException("getUsername() NOT YET IMPLEMENTED");
    }

    @Override
    public char[] getPassword() {
        throw new RuntimeException("getPassword() NOT YET IMPLEMENTED");
    }

    @Override
    public void setControllerAddress(JBossControllerAddress a) {

        this.controllerAddress = a;
    }

    @Override
    public JBossControllerAddress getControllerAddress() {

        return controllerAddress;
    }

    @Override
    public void connect() throws JBossCliException {
        throw new RuntimeException("connect() NOT YET IMPLEMENTED");
    }

    @Override
    public void disconnect() {
        throw new RuntimeException("disconnect() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isConnected() {
        throw new RuntimeException("isConnected() NOT YET IMPLEMENTED");
    }

    @Override
    public Object getAttributeValue(String path, String attributeName) throws JBossCliException {
        throw new RuntimeException("getAttributeValue() NOT YET IMPLEMENTED");
    }

    @Override
    public void setCommandContextFactory(CommandContextFactory commandContextFactory) {
        throw new RuntimeException("setCommandContextFactory() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
