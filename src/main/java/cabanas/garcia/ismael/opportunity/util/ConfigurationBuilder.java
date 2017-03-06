package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.server.Configuration;

import java.util.Map;

public class ConfigurationBuilder implements Configuration {
    private Map<String, Class<? extends Controller>> controllerMapping;

    @Override
    public Configuration controllerMapping(Map<String, Class<? extends Controller>> controllerMapping) {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.addControllerMapping(controllerMapping);
        
        return builder;
    }

    private void addControllerMapping(Map<String, Class<? extends Controller>> controllerMapping) {
        this.controllerMapping = controllerMapping;
    }
}
