package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.Page2RawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class Page2Controller extends Controller {
    @Override
    public View process(Request request) {
        return new Page2RawView();
    }

    @Override
    public String getMappingPath() {
        return "/page2";
    }
}
