package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.ControllerMapper;
import cabanas.garcia.ismael.opportunity.service.ControllerScannerService;

import java.util.List;
import java.util.Map;

public class StandardWebServer {
    private final int port;
    private final ControllerScannerService controllerScannerService;
    private final WebServer server;
    private final ControllerMapper controllerMapper;

    public StandardWebServer(int port, ControllerScannerService controllerScannerService, ControllerMapper controllerMapper, WebServer theWebServer) {
        this.port = port;
        this.controllerScannerService = controllerScannerService;
        this.controllerMapper = controllerMapper;
        this.server = theWebServer;
    }

    public void start() {
        List<Class<? extends Controller>> controllersScanned = controllerScannerService.scanner();

        Map<String, Class<? extends Controller>> controllerMapping = controllerMapper.mapping(controllersScanned);

        try {
            server.start();
        } catch (UnavailableServerException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return server.isRunning();
    }
}
