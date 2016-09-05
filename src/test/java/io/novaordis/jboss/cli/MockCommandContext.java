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

import org.jboss.as.cli.CliConfig;
import org.jboss.as.cli.CliEventListener;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandHistory;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.CommandLineRedirection;
import org.jboss.as.cli.batch.BatchManager;
import org.jboss.as.cli.batch.BatchedCommand;
import org.jboss.as.cli.operation.CommandLineParser;
import org.jboss.as.cli.operation.NodePathFormatter;
import org.jboss.as.cli.operation.OperationCandidatesProvider;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/4/16
 */
public class MockCommandContext implements CommandContext {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockCommandContext.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<String, Map<String, Object>> values;

    private boolean connected;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockCommandContext() {

        this.values = new HashMap<>();
    }

    // CommandContext implementation -----------------------------------------------------------------------------------

    @Override
    public CliConfig getConfig() {
        throw new RuntimeException("getConfig() NOT YET IMPLEMENTED");
    }

    @Override
    public String getArgumentsString() {
        throw new RuntimeException("getArgumentsString() NOT YET IMPLEMENTED");
    }

    @Override
    public ParsedCommandLine getParsedCommandLine() {
        throw new RuntimeException("getParsedCommandLine() NOT YET IMPLEMENTED");
    }

    @Override
    public void printLine(String message) {
        throw new RuntimeException("printLine() NOT YET IMPLEMENTED");
    }

    @Override
    public void printColumns(Collection<String> col) {
        throw new RuntimeException("printColumns() NOT YET IMPLEMENTED");
    }

    @Override
    public void clearScreen() {
        throw new RuntimeException("clearScreen() NOT YET IMPLEMENTED");
    }

    @Override
    public void terminateSession() {
        throw new RuntimeException("terminateSession() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isTerminated() {
        throw new RuntimeException("isTerminated() NOT YET IMPLEMENTED");
    }

    @Override
    public void set(String key, Object value) {
        throw new RuntimeException("set() NOT YET IMPLEMENTED");
    }

    @Override
    public Object get(String key) {
        throw new RuntimeException("get() NOT YET IMPLEMENTED");
    }

    @Override
    public Object remove(String key) {
        throw new RuntimeException("remove() NOT YET IMPLEMENTED");
    }

    @Override
    public ModelControllerClient getModelControllerClient() {
        throw new RuntimeException("getModelControllerClient() NOT YET IMPLEMENTED");
    }

    @Override
    public void connectController(String host, int port) throws CommandLineException {
        throw new RuntimeException("connectController() NOT YET IMPLEMENTED");
    }

    @Override
    public void bindClient(ModelControllerClient newClient) {
        throw new RuntimeException("bindClient() NOT YET IMPLEMENTED");
    }

    @Override
    public void connectController() throws CommandLineException {

        connected = true;

        log.info("controller connected");
    }

    @Override
    public void disconnectController() {
        throw new RuntimeException("disconnectController() NOT YET IMPLEMENTED");
    }

    @Override
    public String getDefaultControllerHost() {
        throw new RuntimeException("getDefaultControllerHost() NOT YET IMPLEMENTED");
    }

    @Override
    public int getDefaultControllerPort() {
        throw new RuntimeException("getDefaultControllerPort() NOT YET IMPLEMENTED");
    }

    @Override
    public String getControllerHost() {
        throw new RuntimeException("getControllerHost() NOT YET IMPLEMENTED");
    }

    @Override
    public int getControllerPort() {
        throw new RuntimeException("getControllerPort() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandLineParser getCommandLineParser() {
        throw new RuntimeException("getCommandLineParser() NOT YET IMPLEMENTED");
    }

    @Override
    public OperationRequestAddress getCurrentNodePath() {
        throw new RuntimeException("getCurrentNodePath() NOT YET IMPLEMENTED");
    }

    @Override
    public NodePathFormatter getNodePathFormatter() {
        throw new RuntimeException("getNodePathFormatter() NOT YET IMPLEMENTED");
    }

    @Override
    public OperationCandidatesProvider getOperationCandidatesProvider() {
        throw new RuntimeException("getOperationCandidatesProvider() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandHistory getHistory() {
        throw new RuntimeException("getHistory() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isBatchMode() {
        throw new RuntimeException("isBatchMode() NOT YET IMPLEMENTED");
    }

    @Override
    public BatchManager getBatchManager() {
        throw new RuntimeException("getBatchManager() NOT YET IMPLEMENTED");
    }

    @Override
    public BatchedCommand toBatchedCommand(String line) throws CommandFormatException {
        throw new RuntimeException("toBatchedCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public ModelNode buildRequest(String line) throws CommandFormatException {
        throw new RuntimeException("buildRequest() NOT YET IMPLEMENTED");
    }

    @Override
    public CommandLineCompleter getDefaultCommandCompleter() {
        throw new RuntimeException("getDefaultCommandCompleter() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isDomainMode() {
        throw new RuntimeException("isDomainMode() NOT YET IMPLEMENTED");
    }

    @Override
    public void addEventListener(CliEventListener listener) {
        throw new RuntimeException("addEventListener() NOT YET IMPLEMENTED");
    }

    @Override
    public int getExitCode() {
        throw new RuntimeException("getExitCode() NOT YET IMPLEMENTED");
    }

    @Override
    public void handle(String line) throws CommandLineException {
        throw new RuntimeException("handle() NOT YET IMPLEMENTED");
    }

    @Override
    public void handleSafe(String line) {
        throw new RuntimeException("handleSafe() NOT YET IMPLEMENTED");
    }

    @Override
    public void interact() {
        throw new RuntimeException("interact() NOT YET IMPLEMENTED");
    }

    @Override
    public File getCurrentDir() {
        throw new RuntimeException("getCurrentDir() NOT YET IMPLEMENTED");
    }

    @Override
    public void setCurrentDir(File dir) {
        throw new RuntimeException("setCurrentDir() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isResolveParameterValues() {
        throw new RuntimeException("isResolveParameterValues() NOT YET IMPLEMENTED");
    }

    @Override
    public void setResolveParameterValues(boolean resolve) {
        throw new RuntimeException("setResolveParameterValues() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isSilent() {
        throw new RuntimeException("isSilent() NOT YET IMPLEMENTED");
    }

    @Override
    public void setSilent(boolean silent) {
        throw new RuntimeException("setSilent() NOT YET IMPLEMENTED");
    }

    @Override
    public int getTerminalWidth() {
        throw new RuntimeException("getTerminalWidth() NOT YET IMPLEMENTED");
    }

    @Override
    public int getTerminalHeight() {
        throw new RuntimeException("getTerminalHeight() NOT YET IMPLEMENTED");
    }

    @Override
    public void registerRedirection(CommandLineRedirection redirection) throws CommandLineException {
        throw new RuntimeException("registerRedirection() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Install custom attribute values.
     */
    public void install(String path, String attributeName, Object attributeValue) {

        Map<String, Object> attributes = values.get(path);

        if (attributes == null) {
            attributes = new HashMap<>();
            values.put(path, attributes);
        }

        attributes.put(attributeName, attributeValue);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
