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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/4/16
 */
public class ModelNodeUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ModelNodeUtilTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // operationResponseToValue() --------------------------------------------------------------------------------------

    @Test
    public void operationResponseToValue_Null() throws Exception {

        try {

            ModelNodeUtil.operationResponseToValue(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null response", msg);
        }
    }

    @Test
    public void operationResponseToValue_responseHasNoOutcome() throws Exception {

        ModelNode response = new ModelNode();

        try {
            ModelNodeUtil.operationResponseToValue(response);
            fail("should throw exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("node has no '" + Util.OUTCOME + "' key"));
        }
    }

    @Test
    public void operationResponseToValue_unknownOutcome() throws Exception {

        ModelNode response = new ModelNode();

        response.set(Util.OUTCOME, "blah");

        try {

            ModelNodeUtil.operationResponseToValue(response);
            fail("should throw exception");
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("unknown jboss CLI operation outcome: blah"));
        }
    }

    @Test
    public void operationResponseToValue_Failure_NoDetails() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(ModelNodeUtil.FAILED);

        try {

            ModelNodeUtil.operationResponseToValue(response);
            fail("should throw exception");
        }
        catch(JBossCliOperationFailureException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("no details", msg);
        }
    }

    @Test
    public void operationResponseToValue_Failure_DetailsAvailable() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(ModelNodeUtil.FAILED);
        response.get(Util.FAILURE_DESCRIPTION).set("mock failure description");

        try {

            ModelNodeUtil.operationResponseToValue(response);
            fail("should throw exception");
        }
        catch(JBossCliOperationFailureException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("mock failure description", msg);
        }
    }

    @Test
    public void operationResponseToValue_Success_Undefined() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);

        //
        // this creates and "UNDEFINED" node
        //
        response.get(Util.RESULT);

        Object result = ModelNodeUtil.operationResponseToValue(response);
        assertNull(result);
    }

    @Test
    public void operationResponseToValue_Success_String() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set("test string");

        String s = (String)ModelNodeUtil.operationResponseToValue(response);
        assertEquals("test string", s);
    }

    @Test
    public void operationResponseToValue_Success_Boolean() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set(false);

        Boolean b = (Boolean)ModelNodeUtil.operationResponseToValue(response);
        assertNotNull(b);
        assertFalse(b);
    }

    @Test
    public void operationResponseToValue_Success_Int() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set(10);

        Integer i = (Integer)ModelNodeUtil.operationResponseToValue(response);
        assertNotNull(i);
        assertEquals(10, i.intValue());
    }

    @Test
    public void operationResponseToValue_Success_Long() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set(10L);

        Long l = (Long)ModelNodeUtil.operationResponseToValue(response);
        assertNotNull(l);
        assertEquals(10L, l.longValue());
    }

    @Test
    public void operationResponseToValue_Success_Double() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set(10.1);

        Double d = (Double)ModelNodeUtil.operationResponseToValue(response);
        assertNotNull(d);
        assertEquals(10.1, d.doubleValue(), 0.0000001);
    }

    @Test
    public void operationResponseToValue_Success_UnsupportedType() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(Util.SUCCESS);
        response.get(Util.RESULT).set(new BigDecimal(1));

        try {
            ModelNodeUtil.operationResponseToValue(response);
        }
        catch(JBossCliException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("unsupported response type BIG_DECIMAL", msg);
        }
    }

    @Test
    public void operationResponseToValue_UnknownAttributeReturnsNull() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(ModelNodeUtil.FAILED);
        response.get(Util.FAILURE_DESCRIPTION).set("JBAS014792: Unknown attribute no-such-attr");

        Object result = ModelNodeUtil.operationResponseToValue(response);
        assertNull(result);
    }

    @Test
    public void operationResponseToValue_UnknownAttributeReturnsNull2() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(ModelNodeUtil.FAILED);
        response.get(Util.FAILURE_DESCRIPTION).set("unknown attribute");

        Object result = ModelNodeUtil.operationResponseToValue(response);
        assertNull(result);
    }

    @Test
    public void operationResponseToValue_NoSuchPath() throws Exception {

        ModelNode response = new ModelNode();

        response.get(Util.OUTCOME).set(ModelNodeUtil.FAILED);
        response.get(Util.FAILURE_DESCRIPTION).set("unknown attribute");

        Object result = ModelNodeUtil.operationResponseToValue(response);
        assertNull(result);
    }

    // buildFailure() --------------------------------------------------------------------------------------------------

    @Test
    public void buildFailure_NullDescription() throws Exception {

        try {
            ModelNodeUtil.buildFailure(null);
        }
        catch(IllegalArgumentException e) {
            assertEquals("null failure description", e.getMessage());
        }
    }

    @Test
    public void buildFailure() throws Exception {

        ModelNode failure = ModelNodeUtil.buildFailure("some failure description");

        assertFalse(Util.isSuccess(failure));

        String failureDescription = Util.getFailureDescription(failure);

        assertEquals("some failure description", failureDescription);
    }

    // buildFailure() --------------------------------------------------------------------------------------------------

    @Test
    public void buildSuccess_String() throws Exception {

        ModelNode n = ModelNodeUtil.buildSuccess("test");

        assertTrue(Util.isSuccess(n));

        ModelNode result = n.get(Util.RESULT);

        assertEquals(ModelType.STRING, result.getType());
        assertEquals("test", result.asString());
    }

    @Test
    public void buildSuccess_Integer() throws Exception {

        ModelNode n = ModelNodeUtil.buildSuccess(1);

        assertTrue(Util.isSuccess(n));

        ModelNode result = n.get(Util.RESULT);

        assertEquals(ModelType.INT, result.getType());
        assertEquals(1, result.asInt());
    }

    @Test
    public void buildSuccess_Null() throws Exception {

        try {

            ModelNodeUtil.buildSuccess(null);
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null attribute value", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
