package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;

public abstract class Controller {
    public abstract View process(Request request);

    public abstract Resource getMappingPath();

    public RequestMethodEnum getMethod() {
        return RequestMethodEnum.GET;
    }
}
