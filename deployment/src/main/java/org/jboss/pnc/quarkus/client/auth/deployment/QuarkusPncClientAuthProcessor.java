package org.jboss.pnc.quarkus.client.auth.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuth;
import org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuthImpl;

class QuarkusPncClientAuthProcessor {

    private static final String FEATURE = "quarkus-pnc-client-auth";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerServiceBean() {
        // This tells Quarkus to index and manage these classes as CDI beans
        return AdditionalBeanBuildItem.builder()
                .addBeanClasses(PNCClientAuth.class, PNCClientAuthImpl.class)
                .setDefaultScope(DotNames.APPLICATION_SCOPED) // Optional: enforce scope
                .build();
    }
}
