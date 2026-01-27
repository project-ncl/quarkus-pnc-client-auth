package org.jboss.pnc.quarkus.client.auth.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

class QuarkusPncClientAuthProcessor {

    private static final String FEATURE = "quarkus-pnc-client-auth";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerReflection(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        // Register both interface and implementation
        reflectiveClass.produce(
                ReflectiveClassBuildItem
                        .builder(
                                org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuth.class.getCanonicalName(),
                                org.jboss.pnc.quarkus.client.auth.runtime.PNCClientAuthImpl.class.getCanonicalName())
                        .methods(true)
                        .fields(true)
                        .build());
    }
}
