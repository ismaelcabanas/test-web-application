package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.UnknownResourceRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class UnknownResourceController extends Controller {
    @Override
    public View process(Request request) {
        return new UnknownResourceRawView();
    }

    @Override
    public String getMappingPath() {
        return null;
    }
}
