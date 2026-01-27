package org.jboss.pnc.quarkus.client.auth.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.pnc.quarkus.client.auth.runtime.ClientAuthentication;

class QuarkusPncClientAuthProcessor {

    private static final String FEATURE = "quarkus-pnc-client-auth";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerMyBean() {
        // Force Quarkus to recognize a class as a CDI bean without needing beans.xml
        return new AdditionalBeanBuildItem(ClientAuthentication.class);
    }
}
