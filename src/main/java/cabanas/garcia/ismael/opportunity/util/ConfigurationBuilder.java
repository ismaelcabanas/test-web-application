package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.server.Configuration;

public class ConfigurationBuilder implements Configuration {
    private Mapping controllerMapping;
    private int port;

    @Override
    public Configuration controllerMapping(Mapping controllerMapping) {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.addControllerMapping(controllerMapping);
        
        return builder;
    }

    @Override
    public Mapping getControllerMapping() {
        return controllerMapping;
    }

    @Override
    public Configuration port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public int getPort() {
        return port;
    }

    private void addControllerMapping(Mapping controllerMapping) {
        this.controllerMapping = controllerMapping;
    }
}
