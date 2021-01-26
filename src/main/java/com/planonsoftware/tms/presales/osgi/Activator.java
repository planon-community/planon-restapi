package com.planonsoftware.tms.presales.osgi;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.ServiceRegistration;

import com.planonsoftware.tms.presales.services.Dartmouth;

import nl.planon.hades.osgi.integration.ServerBundleActivator;
import nl.planon.json.server.container.services.common.AbstractJaxRsRegistry;
import nl.planon.json.server.container.services.common.IJsonJaxRsRegistry;


/**
 * @author
 *
 */
public class Activator extends ServerBundleActivator {
    private final List<ServiceRegistration> mRegistrations = new ArrayList<>();

    /**
     * 
     */
    public Activator() {
        super("SDK16");
    }

    private class JaxRSRegistry extends AbstractJaxRsRegistry {
        public JaxRSRegistry() {
            super("Dartmouth");
            addPerRequestResource(Dartmouth.class);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start() {
        mRegistrations.add(getBundleContext().registerService(IJsonJaxRsRegistry.class.getName(), new JaxRSRegistry(), null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void stop() {
        for (ServiceRegistration registration : mRegistrations) {
            registration.unregister();
        }
    }

}
