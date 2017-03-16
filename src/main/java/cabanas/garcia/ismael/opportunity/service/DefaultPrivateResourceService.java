package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultPrivateResourceService implements PrivateResourcesService {
    private final PrivateResources privateResources;

    public DefaultPrivateResourceService(PrivateResources privateResources) {
        this.privateResources = privateResources;
    }

    @Override
    public boolean hasResource(final Resource resource) {
        boolean hasResource = privateResources.hasResource(resource);
        log.debug("Is {} a private resource? {}", resource, hasResource);
        return hasResource;
    }
}
