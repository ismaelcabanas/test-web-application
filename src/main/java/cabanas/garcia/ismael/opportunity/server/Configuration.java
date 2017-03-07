package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.mapper.Mapping;

public interface Configuration {
    Configuration controllerMapping(Mapping controllerMapping);

    Mapping getControllerMapping();

    Configuration port(int port);

    int getPort();
}
