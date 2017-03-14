package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.support.Resource;

import java.util.Optional;

public interface Mapping {

    boolean hasControllers();

    Optional<Class<? extends Controller>> getController(String mappingPath);

    Optional<Class<? extends Controller>> getController(Resource resource);

    void addMapping(String mappingPath, Class<? extends Controller> aClass);

    void addMapping(Resource resource, Class<? extends Controller> aClass);

    void addMapping(Resource resource, RequestMethodEnum method, Class<? extends Controller> aClass);

    void addMapping(String mappingPath, RequestMethodEnum method, Class<? extends Controller> aClass);

    Optional<Class<? extends Controller>> getController(Resource mappingPath, RequestMethodEnum method);

    Optional<Class<? extends Controller>> getController(String mappingPath, RequestMethodEnum method);
}
