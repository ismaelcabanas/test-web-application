package cabanas.garcia.ismael.opportunity.controller;


import cabanas.garcia.ismael.opportunity.controller.web.UnknownResourceController;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.support.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Controllers {
    private final Mapping mapping;
    private final DIControllerFactory controllerFactory;

    public Controllers(Mapping mapping, DIControllerFactory controllerFactory) {
        this.mapping = mapping;
        this.controllerFactory = controllerFactory;
    }

    public Controller select(final Request request) {
        assert request != null;

        Resource resource = request.getResource();
        Optional<Class<? extends Controller>> aControllerClass = mapping.getController(resource, request.getMethod());

        if(aControllerClass.isPresent()){
            Class<? extends Controller> clazz = aControllerClass.get();
            log.debug("Controller {} selected for handling resource {}", clazz.getName(), resource);
            return controllerFactory.getInstance(clazz);
        }
        log.debug("No controller for handling resource {}", resource);
        return new UnknownResourceController();
    }
}
