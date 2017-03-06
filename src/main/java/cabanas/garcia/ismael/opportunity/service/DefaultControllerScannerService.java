package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultControllerScannerService implements ControllerScannerService {

    private List<Class<? extends Controller>> controllersScanned;

    public DefaultControllerScannerService(String pathToScanner) {
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
