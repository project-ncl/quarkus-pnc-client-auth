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
     * The current client authentication schemes supported
     */
    public static enum ClientAuthType {
        OIDC, LDAP
    }

    /**
     * Only return the HTTP auth scheme token. Example: Authorization {Scheme} TOKEN
     * 
     * @return auth scheme token
     */
    String getAuthToken();

    /**
     * Return the full value for the HTTP Authorization header. e.g Basic TOKEN
     * 
     * @return full HTTP Authorization header value
     */
    String getHttpAuthorizationHeaderValue();

    /**
     * Variant of getHttpAuthorizationHeaderValue where the OIDC token is obtained from a cached value. It is guaranteed
     * to not have expired yet at the time of calling, but you can adjust the freshness of the token by setting:
     * quarkus.oidc_client.refresh-token-time-skew=3m for example. It is recommended to just use
     * {@link #getHttpAuthorizationHeaderValue()} instead since the cached one might not be refreshed properly due to
     * some bugs. The LDAP token is not cached, but it doesn't need to be cached anyways.
     *
     * @return full Http Authorization header value
     */
    String getHttpAuthorizationHeaderValueWithCachedToken();
}
