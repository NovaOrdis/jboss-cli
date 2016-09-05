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
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/4/16
 */
public class ModelNodeUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String FAILED = "failed";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Converts a CLI operation response to a typed value, or throws an Exception as necessary.
     *
     * @exception IllegalArgumentException on null result,
     *
     * @exception JBossCliException on various error conditions. The message is human readable and can be used in
     *  interface messages
     *
     * @exception JBossCliOperationFailureException in case the response represents an operation failure.
     */
    public static Object operationResponseToValue(ModelNode response) throws JBossCliException {

        if (response == null) {

            throw new IllegalArgumentException("null response");
        }

        if (!response.hasDefined(Util.OUTCOME)) {

            throw new JBossCliException("node has no '" + Util.OUTCOME + "' key; is it an operation response? " + response);
        }

        ModelNode outcome = response.get(Util.OUTCOME);
        String outcomeAsString = outcome.asString();

        if (Util.SUCCESS.equals(outcomeAsString)) {

            //
            // success
            //

            ModelNode r = response.get(Util.RESULT);
            ModelType type = r.getType();

            if (ModelType.UNDEFINED.equals(type)) {

                return null;
            }
            else if (ModelType.STRING.equals(type)) {

                return r.asString();
            }
            else if (ModelType.BOOLEAN.equals(type)) {

                return r.asBoolean();
            }
            else if (ModelType.INT.equals(type)) {

                return r.asInt();
            }
            else if (ModelType.LONG.equals(type)) {

                return r.asLong();
            }
            else if (ModelType.DOUBLE.equals(type)) {

                return r.asDouble();
            }
            else {

                throw new JBossCliException("unsupported response type " + type);
            }

        }
        else if (FAILED.equals(outcomeAsString)) {

            //
            // failure
            //

            if (!response.hasDefined(Util.FAILURE_DESCRIPTION)) {

                throw new JBossCliOperationFailureException("no details");
            }

            String failureDescription = Util.getFailureDescription(response);

            if (failureDescription == null) {

                throw new JBossCliOperationFailureException("no details");
            }

            String fdtlc = failureDescription.toLowerCase();
            if (fdtlc.contains("unknown attribute")) {

                //
                // we don't fail, we return null
                //
                return null;
            }

            //
            // if the path does not exist, we get something similar to
            // JBAS014883: No resource definition is registered for address [("subsystem" => "no-such-path")]
            //

            throw new JBossCliOperationFailureException(failureDescription);

        }
        else {

            throw new JBossCliException("unknown jboss CLI operation outcome: " + outcomeAsString);
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
