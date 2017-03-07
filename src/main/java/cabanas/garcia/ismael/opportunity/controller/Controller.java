package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.View;

public interface Controller {
    View process(Request request);

    abstract String getMappingPath();
}
