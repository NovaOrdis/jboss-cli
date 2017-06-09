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

import io.novaordis.jboss.cli.JBossCliException;
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

    public static final String PROTOCOL = "jbosscli";
    public static final String PROTOCOL_SEPARATOR = "://";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Parses the following format [jbosscli://][[username:password]@]host[:port].
     */
    public static JBossControllerAddress parseAddress(String s) throws JBossCliException {

        if (s == null) {

            throw new IllegalArgumentException("null address");
        }

        int i = s.indexOf(PROTOCOL_SEPARATOR);

        if (i != -1) {

            String protocol = s.substring(0, i);

            if (!PROTOCOL.equals(protocol)) {

                throw new JBossCliException("not a JBoss controller address: " + s);
            }

            s = s.substring(i + PROTOCOL_SEPARATOR.length());
        }

        i = s.indexOf('@');

        String username = null;
        char[] password = null;
        String host;
        String hostLiteral;
        int port;
        String portLiteral = null;

        String hostAndPort;

        if (i == -1) {

            //
            // no username:password present
            //

            hostAndPort = s;
        }
        else {

            //
            // username:password present
            //

            hostAndPort = s.substring(i + 1);

            String usernameAndPassword = s.substring(0, i);

            int j = usernameAndPassword.indexOf(':');

            if (j == -1) {

                throw new JBossCliException("no password specified");
            }

            username = usernameAndPassword.substring(0, j);
            usernameAndPassword = usernameAndPassword.substring(j + 1);
            password = usernameAndPassword.toCharArray();

            if (password.length == 0) {
                throw new JBossCliException("empty password");
            }
        }

        i = hostAndPort.lastIndexOf(":");

        if (i == -1) {

            host = hostAndPort;
            port = JBossControllerClient.DEFAULT_PORT;
        }
        else {

            host = hostAndPort.substring(0, i);

            if (i == hostAndPort.length() - 1) {

                throw new JBossCliException("missing port information");
            }

            portLiteral = hostAndPort.substring(i + 1);

            try {

                port = Integer.parseInt(portLiteral);
            }
            catch (Exception e) {

                throw new JBossCliException("invalid port value \"" + portLiteral + "\"", e);
            }
        }

        if (host.length() == 0) {

            host = JBossControllerClient.DEFAULT_HOST;
            hostLiteral = null;
        }
        else {
            hostLiteral = host;
        }

        JBossControllerAddress address;

        try {

            address = new JBossControllerAddress(username, password, host, hostLiteral, port, portLiteral);
        }
        catch(IllegalArgumentException e) {

            throw new JBossCliException(e.getMessage(), e);
        }

        return address;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String host;
    private String hostLiteral;
    private int port;
    private String portLiteral;
    private String username;
    private char[] password;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Defaults
     */
    public JBossControllerAddress() {

        this(null, null, JBossControllerClient.DEFAULT_HOST, null, JBossControllerClient.DEFAULT_PORT, null);
    }

    /**
     * @exception IllegalArgumentException on null host or invalid port values.
     * @exception IllegalArgumentException if null password when the username is not null
     */
    public JBossControllerAddress(String username, char[] password,
                                   String host, String hostLiteral,
                                   int port, String portLiteral) {

        if (host == null) {
            throw new IllegalArgumentException("null host");
        }

        this.host = host;
        this.hostLiteral = hostLiteral;

        if (!JBossControllerClient.DEFAULT_HOST.equals(host)) {

            if (hostLiteral == null) {
                throw new IllegalArgumentException("null host literal");
            }
        }

        if (hostLiteral != null && !host.equals(hostLiteral)) {
            throw new IllegalArgumentException(
                    "host \"" + host + "\" does not match host literal \"" + hostLiteral + "\"");
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("invalid port value " + port);
        }

        this.port = port;
        this.portLiteral = portLiteral;

        if (JBossControllerClient.DEFAULT_PORT != port) {

            if (portLiteral == null) {
                throw new IllegalArgumentException("null port literal");
            }
        }

        if (portLiteral != null && !portLiteral.equals("" + port)) {
            throw new IllegalArgumentException(
                    "port \"" + port + "\" does not match port literal \"" + portLiteral + "\"");
        }

        this.username = username;

        if (password == null) {

            if (username != null) {
                throw new IllegalArgumentException("null password");
            }

            this.password = null;
        }
        else {

            this.password = new char[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Will never return null. The default value is JBossControllerClient.DEFAULT_HOST.
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the string representation of the host name. Always a non-null String, with the exception of the case
     * when the host is "localhost" (JBossControllerClient.DEFAULT_HOST) and it is specified implicitly.
     */
    public String getHostLiteral() {
        return hostLiteral;
    }

    public int getPort() {
        return port;
    }

    /**
     * @return the string representation of the port. Always a non-null String, with the exception of the case when the
     * port is 9999 (JBossControllerClient.DEFAULT_PORT) and it is specified implicitly.
     */
    public String getPortLiteral() {
        return portLiteral;
    }

    /**
     * null means "local connection"
     */
    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    /**
     * @return the canonical string representation of the entire address [username@]<host>[:port]. The canonical
     *          representation does not display the password, even if it was specified, and it also does not include
     *          a protocol prefix.
     */
    public String getLiteral() {

        String s = "";

        if (username != null) {

            s += username + "@";
        }

        s += hostLiteral;

        if (portLiteral != null) {

            s += ":" + portLiteral;
        }

        return s;
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

        return (username == null ? "" : username + ":***@") + host + ":" + port;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
