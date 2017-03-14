package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;

public class Test2Controller extends Controller {
    private String path;

    public Test2Controller() {
    }

    public Test2Controller(String path) {
        super();
        this.path = path;
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path("/test2").build();
    }

    @Override
    public View process(Request request) {
        return null;
    }
}
