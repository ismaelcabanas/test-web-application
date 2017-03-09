package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.view.View;

public class Test1PostController extends Controller {
    private String path;

    public Test1PostController() {
    }

    @Override
    public View process(Request request) {
        return null;
    }

    @Override
    public String getMappingPath() {
        return "/test1";
    }

    public Test1PostController(String path) {
        super();
        this.path = path;
    }

    @Override
    public String getMethod() {
        return RequestMethodConstants.POST;
    }
}
