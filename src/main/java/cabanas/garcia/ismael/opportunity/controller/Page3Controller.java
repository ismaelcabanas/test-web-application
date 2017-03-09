package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.Page3RawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class Page3Controller extends AbstractController {
    @Override
    public View process(Request request) {
        return new Page3RawView();
    }

    @Override
    public String getMappingPath() {
        return "/page3";
    }
}
