package cabanas.garcia.ismael.opportunity.controller;

import java.util.List;
import java.util.Map;

public interface ControllerMapper {
    Map<String, Class<? extends Controller>> mapping(List<Class<? extends Controller>> controllers);
}
