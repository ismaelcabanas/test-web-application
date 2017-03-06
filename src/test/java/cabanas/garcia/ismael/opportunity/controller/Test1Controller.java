package cabanas.garcia.ismael.opportunity.test.util;


import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.View;

public class Test1Controller extends Controller {
    private final String path;

    public Test1Controller(String path) {
        super();
        this.path = path;
    }

    @Override
    public String getMappingPath() {
        return "/test1";
    }

    @Override
    public View process(Request request) {
        return null;
    }
}
