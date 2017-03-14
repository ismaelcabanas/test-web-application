package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;

public class Test1PostController extends Controller {
    private String path;

    public Test1PostController() {
    }

    @Override
    public View process(Request request) {
        return null;
    }

    @Override
    public Resource getMappingPath() {
        return Resource.builder().path("/test1").build();
    }

    public Test1PostController(String path) {
        super();
        this.path = path;
    }

    @Override
    public RequestMethodEnum getMethod() {
        return RequestMethodEnum.POST;
    }
}
