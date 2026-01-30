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
    oidc-client:
        auth-server-url: https://keycloak/auth
        client-id: quarkus_app
        credentials:
            secret: secret
        refresh-token-time-skew: 2M
```

If you use `PNCClientAuth.getHttpAuthorizationHeaderValueWithCachedToken`, it is recommended to set the
`refresh-token-time-skew` configuration so that the OIDC access token gets refreshed way before they expire

## Testing in your app

You can mock `PNCClientAuth` in your Quarkus app for testing:
```java
@Mock
@ApplicationScoped
public class PNCClientAuthMock implements PNCClientAuth {
    @Override
    public String getAuthToken() {
        return "1234";
    }

    @Override
    public String getHttpAuthorizationHeaderValue() {
        return "Bearer 1234";
    }

    @Override
    public String getHttpAuthorizationHeaderValueWithCachedToken() {
        return getHttpAuthorizationHeaderValue();
    }

    @Override
    public LDAPCredentials getLDAPCredentials() throws IOException {
        return new LDAPCredentials("user", "password");
    }
}
```

and add in your `application.properties`:
```
%test.quarkus.arc.exclude-types=org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuthImpl
```

or in your `application.yaml`:
```yaml
%test:
    quarkus:
        arc:
            exclude-types: org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuthImpl
```

This is required so that the implementation is never loaded and your mock is
considered as the default implementation.
