/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.jboss;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/17
 */
public class JBossUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // fromClientJar() -------------------------------------------------------------------------------------------------

    @Test
    public void fromClientJar_NonExistentFile() throws Exception {

        try {

            JBossUtil.fromClientJar("/I/am/sure/this/file/does/not/exist.jar");
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            assertTrue(msg.startsWith("no such file:"));
        }
    }

    @Test
    public void fromClientJar_DirectoryInsteadOfFile() throws Exception {

        File d = new File(System.getProperty("basedir"), "src/test/resources/data/jboss-client-files/eap/6.4.15");
        assertTrue(d.isDirectory());

        try {

            JBossUtil.fromClientJar(d.getPath());
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            assertTrue(msg.startsWith("not a file:"));
        }
    }

    @Test
    public void fromClientJar_InvalidJar() throws Exception {

        File f = new File(System.getProperty("basedir"), "src/test/resources/data/invalid-jar-file.jar");

        assertTrue(f.isFile());

        try {

            JBossUtil.fromClientJar(f.getPath());
            fail("should throw exception");
        }
        catch(IOException e) {

            ZipException ze = (ZipException)e;
            String msg = ze.getMessage();
            assertTrue(msg.contains("error"));
            assertTrue(msg.contains("opening"));
        }
    }

    @Test
    public void fromClientJar_JarWithoutAManifest() throws Exception {

        File f = new File(System.getProperty("basedir"), "src/test/resources/data/jar-without-manifest.jar");

        assertTrue(f.isFile());

        try {

            JBossUtil.fromClientJar(f.getPath());
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no META-INF/MANIFEST.MF found in"));
        }
    }

    @Test
    public void fromClientJar_jboss_cli_client() throws Exception {

        File f = new File(System.getProperty("basedir"),
                "src/test/resources/data/jboss-client-files/eap/6.4.15/jboss-cli-client.jar");

        assertTrue(f.isFile());

        JBossInfo i = JBossUtil.fromClientJar(f.getPath());
        assertNotNull(i);
        assertTrue(i.isEAP());
        assertEquals(6, i.getMajorVersion());
    }

    @Test
    public void fromClientJar_jboss_client() throws Exception {

        File f = new File(System.getProperty("basedir"),
                "src/test/resources/data/jboss-client-files/eap/6.4.15/jboss-client.jar");

        assertTrue(f.isFile());

        JBossInfo i = JBossUtil.fromClientJar(f.getPath());
        assertNotNull(i);
        assertTrue(i.isEAP());
        assertEquals(6, i.getMajorVersion());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
