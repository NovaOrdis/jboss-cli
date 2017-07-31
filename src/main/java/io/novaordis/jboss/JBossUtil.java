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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 *
 * Various static utilities for identifying and extracting information from JBoss deployments.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/17
 */
public class JBossUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossUtil.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Analyzes the path and decides whether is a valid JBoss client JAR. If it is, extracts information from it.
     *
     * @return a JBossInfo instance if the given path represents a valid JBoss client JAR, or null otherwise.
     *
     * @exception IOException on any file-related problems, such as file not existing, etc.
     */
    public static JBossInfo fromClientJar(String clientJarPath) throws IOException {

        log.debug("extracting JBoss info from " + clientJarPath);

        File f = new File(clientJarPath);

        if (f.isDirectory()) {

            throw new IOException("not a file: " + clientJarPath);
        }

        if (!f.isFile()) {

            throw new IOException("no such file: " + clientJarPath);
        }

        JarFile jarFile = new JarFile(f);

        //
        // look for Manifest
        //

        ZipEntry e = jarFile.getEntry("META-INF/MANIFEST.MF");

        if (e == null) {

            //
            // no manifest
            //

            throw new IOException("no META-INF/MANIFEST.MF found in " + f.getPath());
        }

        InputStream is = null;
        BufferedReader br;

        JBossInfo info = null;

        try {

            is = jarFile.getInputStream(e);
            br = new BufferedReader(new InputStreamReader(is));

            //
            // look for "Implementation Title:"
            //
            // EAP 6
            //  jboss-client.jar:
            //      Implementation-Title: JBoss Application Server: EJB and JMS client [...]
            //  jboss-cli.jar:
            //      Implementation-Title: JBoss Application Server: Command line interface
            //
            // EAP 7
            //  jboss-client.jar:
            //      Implementation-Title: WildFly: EJB and JMS client [...]
            //  jboss-cli.jar:
            //      Implementation-Title: WildFly: Command line interface

            String line;
            boolean isClientJar = false;
            boolean isCliJar = false;
            Integer majorVersion = null;

            while((line = br.readLine()) != null) {

                if (line.startsWith("Implementation-Title:")) {

                    line = line.substring("Implementation-Title:".length()).trim();

                    if (line.startsWith("JBoss Application Server")) {

                        majorVersion = 6;
                    }
                    else if (line.startsWith("WildFly")) {

                        majorVersion = 7;
                    }
                    else {

                        log.warn("we did not find \"JBoss Application Server\" or \"WildFly\" on the Implementation-Title line");
                        break;
                    }

                    if (line.contains("Command line interface")) {

                        isCliJar = true;

                    }
                    else if (line.contains("EJB and JMS client")) {

                        isClientJar = true;
                    }

                    break;
                }
            }

            if (isClientJar || isCliJar) {

                info = new JBossInfo();

                // TODO more heuristics required here, this is pretty thin

                info.setMajorVersion(majorVersion);
                info.setEAP(true);
            }
        }
        finally {

            if (is != null) {

                try {

                    is.close();
                }
                catch (IOException ioe) {

                    log.warn("failed to close input stream");
                }
            }
        }

        log.debug(info == null ? "no JBoss info identified in " +  clientJarPath: info + " identified based on " + clientJarPath);

        return info;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
