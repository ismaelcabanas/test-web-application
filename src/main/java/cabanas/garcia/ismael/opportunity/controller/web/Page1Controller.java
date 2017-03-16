package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.Page1RawView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class Page1Controller extends Controller {
    @Override
    public View process(Request request) {
        Optional<Session> session = request.getSession();
        if(session.isPresent())
            return new Page1RawView(session.get().getUser());
        return new Page1RawView();
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path("/page1").build();
    }
}
