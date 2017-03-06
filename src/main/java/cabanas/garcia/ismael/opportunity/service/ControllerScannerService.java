package cabanas.garcia.ismael.opportunity.service;


import cabanas.garcia.ismael.opportunity.controller.Controller;

import java.util.List;

public interface ControllerScannerService {
    List<Class<? extends Controller>> scanner();
}
