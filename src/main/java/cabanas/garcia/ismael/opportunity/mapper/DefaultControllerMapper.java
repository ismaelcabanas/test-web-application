package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.Instantiator;

import java.util.List;

public class DefaultControllerMapper implements ControllerMapper{
    private final Instantiator instantiator;

    public DefaultControllerMapper(Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    @Override
    public Mapping mapping(List<Class<? extends Controller>> controllers) {
        Mapping mapping = new Mapping();
        controllers.forEach(aClass -> {
            Controller instance = instantiator.newInstance(aClass);
            mapping.addMapping(instance.getMappingPath(), aClass);
        });
        return mapping;
    }
}
