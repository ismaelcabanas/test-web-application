package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.UnknownResourceRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class UnknownResourceController extends Controller {
    @Override
    public View process(Request request) {
        return new UnknownResourceRawView();
    }

    @Override
    public Resource getMappingPath() {
        return Resource.empty();
    }
}
