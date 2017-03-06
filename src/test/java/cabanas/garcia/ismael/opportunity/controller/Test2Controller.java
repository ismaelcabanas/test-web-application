package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.View;

/**
 * Created by XI317311 on 06/03/2017.
 */
public class Test2Controller extends Controller {
    private final String path;

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
