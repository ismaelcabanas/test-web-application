package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.Page3RawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class Page3Controller extends Controller {
    @Override
    public View process(Request request) {
        return new Page3RawView();
    }

    @Override
    public String getMappingPath() {
        return "/page3";
    }
}
