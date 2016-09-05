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

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/4/16
 */
public class MockCommandContextFactory extends CommandContextFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockCommandContextFactory.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MockCommandContext mockCommandContext;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockCommandContextFactory(MockCommandContext mockCommandContext) {

        this.mockCommandContext = mockCommandContext;

    }

    // CommandContextFactory -------------------------------------------------------------------------------------------

    @Override
    public CommandContext newCommandContext() throws CliInitializationException {
        throw new RuntimeException("newCommandContext() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandContext newCommandContext(String username, char[] password) throws CliInitializationException {
        throw new RuntimeException("newCommandContext() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandContext newCommandContext(String controllerHost, int controllerPort, String username, char[] password) throws CliInitializationException {
        throw new RuntimeException("newCommandContext() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandContext newCommandContext(String controllerHost, int controllerPort, String username, char[] password, boolean initConsole, int connectionTimeout) throws CliInitializationException {
        throw new RuntimeException("newCommandContext() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandContext newCommandContext(String controllerHost, int controllerPort, String username, char[] password,
                                            boolean disableLocalAuth, boolean initConsole, int connectionTimeout)
            throws CliInitializationException {


        log.info("mock configuration controller host: " + controllerHost);
        log.info("mock configuration controller port: " + controllerPort);
        log.info("mock configuration controller username: " + username);

        return mockCommandContext;
    }

    @Override
    public CommandContext newCommandContext(String controllerHost, int controllerPort, String username, char[] password, InputStream consoleInput, OutputStream consoleOutput) throws CliInitializationException {
        throw new RuntimeException("newCommandContext() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
