package cabanas.garcia.ismael.opportunity.scanner;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultControllerScanner implements ControllerScanner {

    private List<Class<? extends Controller>> controllersScanned;

    public DefaultControllerScanner(String pathToScanner) {
        controllersScanned = new ArrayList<>();
        Reflections reflections = new Reflections(pathToScanner);
        Set<Class<? extends Controller>> controllers =
                reflections.getSubTypesOf(cabanas.garcia.ismael.opportunity.controller.Controller.class);
        controllers.forEach(aClass -> controllersScanned.add(aClass));
    }

    @Override
    public List<Class<? extends Controller>> scanner() {
        return controllersScanned;
    }
}
