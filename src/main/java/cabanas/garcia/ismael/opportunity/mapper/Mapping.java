package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;

import java.util.Optional;

public interface Mapping {

    boolean hasControllers();

    Optional<Class<? extends Controller>> getController(String mappingPath);

    void addMapping(String mappingPath, Class<? extends Controller> aClass);

    void addMapping(String mappingPath, RequestMethodEnum method, Class<? extends Controller> aClass);

    Optional<Class<? extends Controller>> getController(String mappingPath, RequestMethodEnum method);
}
