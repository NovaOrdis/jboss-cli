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

import io.novaordis.utilities.Files;

import java.io.File;

/**
 *
 * Various static utilities for identifying and extracting information from JBoss deployments.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/17
 */
public class JBossUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Analyzes the path and decides whether is a valid JBoss client JAR. If it is, extracts information from it.
     *
     * @return a JBossInfo instance if the given path represents a valid JBoss client JAR, or null otherwise.
     *
     *
     */
    public static JBossInfo fromClientJar(String clientJarPath) {

        throw new RuntimeException("NOT YET IMPLEMENTED");

//        File d = new File(classpathElement.substring(0, i));
//
//        if (!d.isDirectory()) {
//
//            log.warn("the directory in which " + jbossCliClientJarName + " was declared, does not exist: " + d);
//            return;
//        }
//
//        d = d.getParentFile();
//
//        if (!d.isDirectory()) {
//
//            log.warn("one of the parent directories of " + jbossCliClientJarName + ", does not exist: " + d);
//            return;
//        }
//
//        File jbossHome = d.getParentFile();
//
//        if (!jbossHome.isDirectory()) {
//
//            log.warn(jbossHome + " is not a valid JBoss home directory");
//            return;
//        }
//
//        File versionFile = new File(jbossHome, "version.txt");
//
//        if (!versionFile.isFile()) {
//
//            log.warn(versionFile + " does not exist");
//        }
//
//        String versionString;
//
//        try {
//
//            versionString = Files.read(versionFile);
//        }
//        catch(Exception e) {
//
//            log.warn("failed to read JBoss version file " + versionFile, e);
//            return;
//        }



    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
