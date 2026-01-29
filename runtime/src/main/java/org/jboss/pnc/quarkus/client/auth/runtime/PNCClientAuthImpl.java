package org.jboss.pnc.quarkus.client.auth.runtime;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

/**
 * Easily select the client authentication type (LDAP, OIDC [default]) that will be used to send authenticated requests
 * to other applications.
 *
 * If OIDC is used, the user will have to specify the quarkus-oidc-client fields. If LDAP is used, the uer will have to
 * specify the client_auth.ldap_credentials.path
 */
@ApplicationScoped
public class PNCClientAuthImpl implements PNCClientAuth {

    @Inject
    OidcClient oidcClient;

    /**
     * Used to get cached OIDC token value, rather than getting a fresh one all the time
     */
    @Inject
    Tokens tokens;

    @ConfigProperty(name = "pnc_client_auth.type", defaultValue = "OIDC")
    ClientAuthType clientAuthType;

    /**
     * The path must be a file with format: username:password
     */
    @ConfigProperty(name = "pnc_client_auth.ldap_credentials.path")
    Optional<String> ldapCredentialsPath;

    @Override
    public String getAuthToken() {
        try {
            return switch (clientAuthType) {
                case OIDC -> oidcClient.getTokens().await().atMost(Duration.ofMinutes(5)).getAccessToken();
                case LDAP -> {
                    if (ldapCredentialsPath.isEmpty()) {
                        throw new RuntimeException("client_auth.ldap_credentials.path is empty!");
                    }
                    yield Base64.getEncoder().encodeToString(Files.readString(Path.of(ldapCredentialsPath.get())).strip().getBytes(StandardCharsets.UTF_8));
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHttpAuthorizationHeaderValue() {
        return switch (clientAuthType) {
            case OIDC -> "Bearer " + getAuthToken();
            case LDAP -> "Basic " + getAuthToken();
        };
    }

    @Override
    public String getHttpAuthorizationHeaderValueWithCachedToken() {
        return switch (clientAuthType) {
            case OIDC -> "Bearer " + tokens.getAccessToken();
            case LDAP -> "Basic " + getAuthToken();
        };
    }
}