package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;

public class DefaultPrivateResourceService implements PrivateResourcesService {
    private final PrivateResources privateResources;

    public DefaultPrivateResourceService(PrivateResources privateResources) {
        this.privateResources = privateResources;
    }

    @Override
    public boolean hasResource(final Resource resource) {
        return privateResources.hasResource(resource);
    }
}
