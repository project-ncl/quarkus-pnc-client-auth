package org.jboss.pnc.quarkus.client.auth.runtime;

/**
 * Easily select the client authentication type (LDAP, OIDC [default]) that will be used to send authenticated requests
 * to other applications.
 *
 * If OIDC is used, the user will have to specify the quarkus-oidc-client fields. If LDAP is used, the uer will have to
 * specify the client_auth.ldap_credentials.path
 */
public interface PNCClientAuth {

    /**
     * Only return the HTTP auth scheme token. Example: Authorization {Scheme} TOKEN
     * 
     * @return auth scheme token
     */
    String getAuthToken();

    /**
     * Return the full value for the HTTP Authorization header. e.g Basic TOKEN
     * 
     * @return full HTTP AUthorization header value
     */
    String getHttpAuthorizationHeaderValue();
}
