package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.service.ControllerRegistryService;

public class StandardWebServer {
    private final int port;
    private final ControllerRegistryService controllerRegistryService;
    private final WebServer server;

    public StandardWebServer(int port, ControllerRegistryService controllerRegistryService, WebServer theWebServer) {
        this.port = port;
        this.controllerRegistryService = controllerRegistryService;
        this.server = theWebServer;
    }

    public void start() {
        controllerRegistryService.register();
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
