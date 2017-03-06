package cabanas.garcia.ismael.opportunity.controller;


import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.util.List;

public interface ControllerScanner {
    List<Class<? extends Controller>> scanner();
}
