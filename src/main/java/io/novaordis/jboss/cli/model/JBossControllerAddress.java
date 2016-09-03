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

package io.novaordis.jboss.cli.model;

import io.novaordis.jboss.cli.JBossControllerClient;

/**
 * Encapsulates a hostname, port, username and password.
 *
 * Correctly implements equals() and hashCode();
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/2/16
 */
public class JBossControllerAddress {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String host;
    private int port;
    private String username;
    private char[] password;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JBossControllerAddress() {

        this.host = JBossControllerClient.DEFAULT_HOST;
        this.port = JBossControllerClient.DEFAULT_PORT;
    }

    /**
     * @exception IllegalArgumentException on null host or invalid port values.
     */
    public JBossControllerAddress(String host, int port, String username) {

        setHost(host);
        setPort(port);
        setUsername(username);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Will never return null. The default value is JBossControllerClient.DEFAULT_HOST.
     */
    public String getHost() {
        return host;
    }

    /**
     * @exception IllegalArgumentException if the argument is null.
     */
    public void setHost(String s) {

        if (s == null) {
            throw new IllegalArgumentException("null host");
        }
        this.host = s;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int i) {

        if (i < 1 || i > 65535) {
            throw new IllegalArgumentException("invalid port value " + i);
        }
        this.port = i;
    }

    /**
     * null means "local connection"
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String s) {
        this.username = s;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] chars) {

        this.password = new char[chars.length];
        System.arraycopy(chars, 0, password, 0, chars.length);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof JBossControllerAddress)) {
            return false;
        }

        JBossControllerAddress that = (JBossControllerAddress)o;

        return
                host.equals(that.host) &&
                        port == that.port &&
                        (username == null && that.username == null ||
                                username != null && username.equals(that.username));
    }

    @Override
    public int hashCode() {

        return 7 * host.hashCode() + 17 * port + (username == null ? 0 : username.hashCode());
    }

    @Override
    public String toString() {

        return (username == null ? "" : username + "@") + host + ":" + port;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
