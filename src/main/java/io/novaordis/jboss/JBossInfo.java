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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/17
 */
public class JBossInfo {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean eap;

    // null means that the major version could not be identified
    private Integer majorVersion;

    private String version;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JBossInfo() {

        // EAP by default
        this.eap = true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public boolean isEAP() {

        return eap;
    }

    public String getVersion() {

        return version;
    }

    /**
     * @return 3, 4, 5, 6, 7 or null if the major version could not be identified.
     */
    public Integer getMajorVersion() {

        return majorVersion;
    }

    @Override
    public String toString() {

        String s = "";

        if (eap) {

            s += "EAP ";
        }
        else {

            s += "? ";
        }

        if (majorVersion != null) {

            s += majorVersion + " ";
        }
        else {

            s += "? ";
        }

        if (version != null) {

            s += version;
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setEAP(boolean b) {

        this.eap = b;
    }

    void setVersion(String s) {

        this.version = s;
    }

    void setMajorVersion(Integer i) {

        if (i == null) {

            majorVersion = null;
            return;
        }

        if (i < 3 || i > 8) {

            throw new IllegalArgumentException("invalid major version: " + i);
        }

        this.majorVersion = i;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
