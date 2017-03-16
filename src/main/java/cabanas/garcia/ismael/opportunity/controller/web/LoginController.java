package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestConstants;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.LoginRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class LoginController extends Controller {
    static final String PATH = "/login";



    @Override
    public View process(Request request) {
        String redirectPath = request.getQueryParameter(RequestConstants.REDIRECCT_PARAM);
        if(redirectPath != null)
            return new LoginRawView(redirectPath);
        return new LoginRawView();
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path(PATH).build();
    }
}
