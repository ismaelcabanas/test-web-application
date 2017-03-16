package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.LoginRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class LoginController extends Controller {
    static final String PATH = "/login";



    @Override
    public View process(Request request) {
        String redirectPath = request.getQueryParameter(Request.REDIRECCT_PARAM);
        return new LoginRawView(redirectPath);
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path(PATH).build();
    }
}
