package cabanas.garcia.ismael.opportunity.controller;


import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.Instantiator;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;

import java.util.Optional;

public class Controllers {
    private final Mapping mapping;
    private final Instantiator instantiator;

    public Controllers(Mapping mapping, Instantiator instantiator) {
        this.mapping = mapping;
        this.instantiator = instantiator;
    }

    public Controller select(Request request) {
        assert request != null;

        Optional<Class<? extends Controller>> aControllerClass = mapping.getController(request.getPath(), request.getMethod());

        if(aControllerClass.isPresent()){
            return instantiator.newInstance(aControllerClass.get());
        }

        return new UnknownResourceController();
    }
}
