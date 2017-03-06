package cabanas.garcia.ismael.opportunity.controller;


import cabanas.garcia.ismael.opportunity.http.Request;

import java.util.List;
import java.util.Optional;

public class Controllers {
    private final List<Controller> controllers;

    public Controllers(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public Controller select(Request request) {
        assert controllers != null;
        assert request != null;

        Optional<Controller> optionalController = controllers
                .stream()
                .filter(controller -> controller.getMappingPath().equals(request.getPath()))
                .findFirst();

        if(optionalController.isPresent())
            return optionalController.get();

        return null;
    }
}
