package cabanas.garcia.ismael.opportunity.controller;


import cabanas.garcia.ismael.opportunity.controller.web.UnknownResourceController;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;

import java.util.Optional;

public class Controllers {
    private final Mapping mapping;
    private final DIControllerFactory controllerFactory;

    public Controllers(Mapping mapping, DIControllerFactory controllerFactory) {
        this.mapping = mapping;
        this.controllerFactory = controllerFactory;
    }

    public Controller select(Request request) {
        assert request != null;

        Optional<Class<? extends Controller>> aControllerClass = mapping.getController(request.getPath(), request.getMethod());

        if(aControllerClass.isPresent()){
            return controllerFactory.getInstance(aControllerClass.get());
        }

        return new UnknownResourceController();
    }
}
