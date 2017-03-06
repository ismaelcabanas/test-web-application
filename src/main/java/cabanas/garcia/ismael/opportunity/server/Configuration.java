package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.util.Map;

public interface Configuration {
    Configuration controllerMapping(Map<String, Class<? extends Controller>> controllerMapping);
}
