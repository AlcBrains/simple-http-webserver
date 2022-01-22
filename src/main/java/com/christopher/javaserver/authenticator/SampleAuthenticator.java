package com.christopher.javaserver.authenticator;


import com.sun.net.httpserver.BasicAuthenticator;

/**
 * Basic in-memory authenticator
 */
public class SampleAuthenticator extends BasicAuthenticator {
    /**
     * Creates a BasicAuthenticator for the given HTTP realm
     *
     * @param realm The HTTP Basic authentication realm
     * @throws NullPointerException if the realm is an empty string
     */
    public SampleAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        return "username".equals(username) && password.equals("password");
    }
}
