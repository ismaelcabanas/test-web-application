package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.util.ConfigurationBuilder;

import java.util.List;

public class StandardWebServer {
    private final int port;
    private final ControllerScanner controllerScanner;
    private final WebServer server;
    private final ControllerMapper controllerMapper;

    public StandardWebServer(int port, ControllerScanner controllerScanner, ControllerMapper controllerMapper, WebServer theWebServer) {
        this.port = port;
        this.controllerScanner = controllerScanner;
        this.controllerMapper = controllerMapper;
        this.server = theWebServer;
    }

    public void start() {
        List<Class<? extends Controller>> controllersScanned = controllerScanner.scanner();

        Mapping controllerMapping = controllerMapper.mapping(controllersScanned);

        Configuration serverConfiguration = new ConfigurationBuilder()
                .port(port)
                .controllerMapping(controllerMapping);

        server.addConfiguration(serverConfiguration);

        server.start();
    }

    public boolean isRunning() {
        return server.isRunning();
    }

    public void stop(){
        server.stop();
    }
}
