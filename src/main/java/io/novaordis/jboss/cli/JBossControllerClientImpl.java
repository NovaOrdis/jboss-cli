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
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.cli.parsing.ParserUtil;
import org.jboss.as.cli.parsing.operation.OperationFormat;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public class JBossControllerClientImpl implements JBossControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossControllerClientImpl.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private JBossControllerAddress controllerAddress;
    private boolean disableLocalAuthentication;
    private boolean initializeConsole;
    private int connectionTimeout;

    private CommandContext commandContext;

    private boolean connected;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JBossControllerClientImpl() {

        this.controllerAddress = new JBossControllerAddress();

        this.disableLocalAuthentication = false;
        this.initializeConsole = false;
        this.connectionTimeout = -1;
        this.connected = false;
    }

    // JBossControllerClient implementation ----------------------------------------------------------------------------

    /**
     * @see JBossControllerAddress#setHost(String)
     */
    @Override
    public void setHost(String host) {

        this.controllerAddress.setHost(host);
    }

    @Override
    public String getHost() {

        return controllerAddress.getHost();
    }

    /**
     * @see JBossControllerAddress#setPort(int)
     */
    @Override
    public void setPort(int i) {

        this.controllerAddress.setPort(i);
    }

    @Override
    public int getPort() {

        return controllerAddress.getPort();
    }

    @Override
    public void setUsername(String username) {

        this.controllerAddress.setUsername(username);
    }

    @Override
    public String getUsername() {

        return controllerAddress.getUsername();
    }

    @Override
    public void setPassword(char[] c) {

        this.controllerAddress.setPassword(c);
    }

    @Override
    public char[] getPassword() {

        return controllerAddress.getPassword();
    }

    @Override
    public void connect() throws JBossCliException {

        if (connected) {
            log.warn(this + " already connected");
            return;
        }

        try {

            CommandContextFactory factory = CommandContextFactory.getInstance();
            commandContext = factory.newCommandContext(
                    controllerAddress.getHost(),
                    controllerAddress.getPort(),
                    controllerAddress.getUsername(),
                    controllerAddress.getPassword(),
                    disableLocalAuthentication,
                    initializeConsole,
                    connectionTimeout);
            commandContext.connectController();
            connected = true;
        }
        catch (Exception e) {
            throw new JBossCliException(e);
        }
    }

    @Override
    public void disconnect() {

        commandContext.disconnectController();
        connected = false;
    }

    @Override
    public boolean isConnected() {

        return connected;
    }

    @Override
    public String getAttributeValue(String path, String attributeName) throws JBossCliException {

        if (!connected) {

            throw new JBossCliException(this + " not connected");
        }

        String command = path + ":read-attribute(name=" + attributeName + ")";

        boolean validate = true;
        //noinspection ConstantConditions
        DefaultCallbackHandler parsedCommand = new DefaultCallbackHandler(validate);

        try {
            ParserUtil.parse(command, parsedCommand);
        }
        catch(Exception e) {
            throw new JBossCliException(e);
        }

        if (parsedCommand.getFormat() != OperationFormat.INSTANCE ) {

            //
            // we got this from the CLI code, what is "INSTANCE"?
            //

            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        ModelNode request;

        try {

            request = parsedCommand.toOperationRequest(commandContext);
        }
        catch (Exception e) {
            throw new JBossCliException(e);
        }

        ModelControllerClient client = commandContext.getModelControllerClient();

        ModelNode result;

        try {

            result = client.execute(request);
        }
        catch (Exception e) {
            throw new JBossCliException(e);
        }

        if(Util.isSuccess(result)) {

            //System.out.println("success: " + result);

            ModelNode r = result.get(Util.RESULT);
            ModelType type = r.getType();

            if (ModelType.STRING.equals(type)) {

                return r.asString();
            }
            else {
                throw new RuntimeException("NOT YET IMPLEMENTED: handling type " + type);
            }

        } else {

            String failureDescription = Util.getFailureDescription(result);
            System.out.println("failure: " + failureDescription);
        }

        return "?";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}