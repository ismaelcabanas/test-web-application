package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.LoginRawView;
import cabanas.garcia.ismael.opportunity.view.View;

public class LoginController extends Controller{
    @Override
    public View process(Request request) {
        return new LoginRawView();
    }

    @Override
    public String getMappingPath() {
        return "/login";
    }
}
