package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.Page2RawView;
import cabanas.garcia.ismael.opportunity.view.View;

import java.util.Optional;

public class Page2Controller extends Controller {
    @Override
    public View process(Request request) {
        Optional<Session> session = request.getSession();
        if(session.isPresent())
            return new Page2RawView(session.get().getUser());
        return new Page2RawView();
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path("/page2").build();
    }
}
