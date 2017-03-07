package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Mapping {

    private Map<String, Class<? extends Controller>> mapper = new HashMap<String, Class<? extends Controller>>();

    public boolean hasControllers() {
        return !mapper.isEmpty();
    }

    public Optional<Class<? extends Controller>> getController(String pathController) {
        return Optional.of(mapper.get(pathController));
    }

    public void addMapping(String mappingPath, Class<? extends Controller> aClass) {
        mapper.put(mappingPath, aClass);
    }
}
