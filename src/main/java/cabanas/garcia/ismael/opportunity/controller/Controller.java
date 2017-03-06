package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;

public abstract class Controller {
    public abstract View process(Request request);

    public abstract String getMappingPath();
}
