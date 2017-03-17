package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.ForbiddenRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class UnAuthorizedResourceController extends Controller{
    @Override
    public View process(Request request) {
        return new ForbiddenRawView();
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path("/forbidden").build();
    }
}
