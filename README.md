quarkus-pnc-client-auth Quarkus extension
-----------------------------------------

This extension provides a `ClientAuthentication` object that you can inject to
provide the authorization token to HTTP clients when communicating with other
services.

Add this to your project's `pom.xml`:
```xml
...
<dependencies>
    ...
    <dependency>
        <groupId>org.jboss.pnc</groupId>
        <artifactId>quarkus-pnc-client-auth</artifactId>
        <version>PROJECT_VERSION</version>
    </dependency>
    ...
</dependencies>
```

## Usage
```java
@ApplicationScoped
public class Yummy {
    @Inject
    PNCClientAuth clientAuth;

    ...
    public void method() {
        String authHeaderValue = clientAuth.getHttpAuthorizationHeaderValue();
        // if you only want the token
        String authToken = clientAuth.getAuthToken();
        ...
    }
}
```

## Configuration

In your application.yaml:
```yaml
pnc_client_auth:
    type: OIDC # or LDAP
    ldap_credentials:
        path: /mnt/secrets/ldap_credentials # file must be in format: <username>:<password>

# We also need to configure the oidc-client
# More here: https://quarkus.io/guides/security-openid-connect-client-reference
quarkus:
    oidc_client:
        auth-server-url: https://keycloak/auth
        client-id: quarkus_app
        credentials:
            secret: secret
        refresh-token-time-skew: 2m
```

It is recommended to set the `refresh-token-time-skew` so that the OIDC access
token gets refreshed way before they expire.
