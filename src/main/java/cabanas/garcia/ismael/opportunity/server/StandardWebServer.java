package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.service.ControllerScannerService;

public class StandardWebServer {
    private final int port;
    private final ControllerScannerService controllerScannerService;
    private final WebServer server;

    public StandardWebServer(int port, ControllerScannerService controllerScannerService, WebServer theWebServer) {
        this.port = port;
        this.controllerScannerService = controllerScannerService;
        this.server = theWebServer;
    }

    public void start() {
        controllerScannerService.scanner();
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
