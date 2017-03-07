package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.server.Configuration;

public class ConfigurationBuilder implements Configuration {
    private Mapping controllerMapping;

    @Override
    public Configuration controllerMapping(Mapping controllerMapping) {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.addControllerMapping(controllerMapping);
        
        return builder;
    }

    @Override
    public Mapping getControllerMapping() {
        return null;
    }

    private void addControllerMapping(Mapping controllerMapping) {
        this.controllerMapping = controllerMapping;
    }
}
