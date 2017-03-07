package cabanas.garcia.ismael.opportunity.scanner;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultControllerScanner implements ControllerScanner {

    private final String pathToScanner;

    public DefaultControllerScanner(String pathToScanner) {
        this.pathToScanner = pathToScanner;
    }

    @Override
    public List<Class<? extends Controller>> scanner() {
        List<Class<? extends Controller>> controllersScanned = new ArrayList<>();

        Reflections reflections = new Reflections(pathToScanner);
        Set<Class<? extends Controller>> controllers =
                reflections.getSubTypesOf(cabanas.garcia.ismael.opportunity.controller.Controller.class);

        controllers.forEach(aClass -> controllersScanned.add(aClass));

        return controllersScanned;
    }
}
