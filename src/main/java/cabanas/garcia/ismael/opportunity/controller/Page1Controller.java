package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.Page1RawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class Page1Controller extends AbstractController{
    @Override
    public View process(Request request) {
        return new Page1RawView();
    }

    @Override
    public String getMappingPath() {
        return "/page1";
    }
}
