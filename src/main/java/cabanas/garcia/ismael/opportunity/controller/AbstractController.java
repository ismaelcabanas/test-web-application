package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;

public abstract class AbstractController implements Controller{
    @Override
    public String getMethod() {
        return RequestMethodConstants.GET;
    }
}
