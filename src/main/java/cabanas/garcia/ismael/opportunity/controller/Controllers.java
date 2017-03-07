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

        Optional<Class<? extends Controller>> aControllerClass = mapping.getController(request.getPath());

        if(aControllerClass.isPresent()){
            Controller instanceControllerSelected = instantiator.newInstance(aControllerClass.get());
            return instanceControllerSelected;
        }

        return new UnknownResourceController();
    }
}
