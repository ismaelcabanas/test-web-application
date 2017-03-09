package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
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
    public String getMappingPath() {
        return "/test2";
    }

    @Override
    public View process(Request request) {
        return null;
    }
}
