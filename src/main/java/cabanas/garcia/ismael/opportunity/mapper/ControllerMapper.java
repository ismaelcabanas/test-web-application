package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.util.List;

@FunctionalInterface
public interface ControllerMapper {
    Mapping mapping(List<Class<? extends Controller>> controllers);
}
