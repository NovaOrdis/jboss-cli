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

import org.jboss.as.cli.Util;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.client.OperationResponse;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;
import org.jboss.threads.AsyncFuture;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/4/16
 */
public class MockModelControllerClient implements ModelControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<String, Map<String, Object>> values;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockModelControllerClient() {

        this.values = new HashMap<>();
    }

    // ModelControllerClient implementation ----------------------------------------------------------------------------

    @Override
    public ModelNode execute(ModelNode operation) throws IOException {

        ModelNode operationNameNode = operation.get(Util.OPERATION);
        String operationNode = operationNameNode.asString();
        if (!"read-attribute".equals(operationNode)) {
            throw new RuntimeException("operation " + operationNode + " NOT IMPLEMENTED");
        }

        ModelNode address = operation.get(Util.ADDRESS);

        if (!ModelType.LIST.equals(address.getType())) {

            throw new IllegalArgumentException("expecting a LIST");
        }

        List<ModelNode> pathAsNodes = address.asList();

        String path = "/";

        for(ModelNode element: pathAsNodes) {

            Property p = element.asProperty();

            if (!"/".equals(path)) {

                path += "/";
            }

            path += p.getName() + "=" + p.getValue().asString();
        }

        if (!values.containsKey(path)) {

            //
            // no such path, the real client sends something like:
            //
            // JBAS014883: No resource definition is registered for address [("subsystem" => "no-such-path")]
            //

            return ModelNodeUtil.buildFailure(
                    "JBAS014883: No resource definition is registered for address " + pathAsNodes);
        }

        ModelNode attributeNameNode = operation.get(Util.NAME);
        String attributeName = attributeNameNode.asString();
        Object attributeValue = null;

        Map<String, Object> attributes = values.get(path);

        if (attributes != null) {

            attributeValue = attributes.get(attributeName);
        }

        if (attributeValue == null) {

            //
            // no such attribute, the controller returns a failure
            //
            return ModelNodeUtil.buildFailure("JBAS014792: Unknown attribute " + attributeName);
        }

        return ModelNodeUtil.buildSuccess(attributeValue);
    }

    @Override
    public ModelNode execute(Operation operation) throws IOException {
        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    @Override
    public ModelNode execute(ModelNode operation, OperationMessageHandler messageHandler) throws IOException {
        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    @Override
    public ModelNode execute(Operation operation, OperationMessageHandler messageHandler) throws IOException {
        throw new RuntimeException("execute() NOT YET IMPLEMENTED");
    }

    @Override
    public OperationResponse executeOperation(Operation operation, OperationMessageHandler messageHandler) throws IOException {
        throw new RuntimeException("executeOperation() NOT YET IMPLEMENTED");
    }

    @Override
    public AsyncFuture<ModelNode> executeAsync(ModelNode operation, OperationMessageHandler messageHandler) {
        throw new RuntimeException("executeAsync() NOT YET IMPLEMENTED");
    }

    @Override
    public AsyncFuture<ModelNode> executeAsync(Operation operation, OperationMessageHandler messageHandler) {
        throw new RuntimeException("executeAsync() NOT YET IMPLEMENTED");
    }

    @Override
    public AsyncFuture<OperationResponse> executeOperationAsync(Operation operation, OperationMessageHandler messageHandler) {
        throw new RuntimeException("executeOperationAsync() NOT YET IMPLEMENTED");
    }

    @Override
    public void close() throws IOException {
        throw new RuntimeException("close() NOT YET IMPLEMENTED");
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
