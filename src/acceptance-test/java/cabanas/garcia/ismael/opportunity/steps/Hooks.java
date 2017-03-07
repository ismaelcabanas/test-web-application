package cabanas.garcia.ismael.opportunity.steps;


import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.StandardWebServer;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cucumber.api.java8.En;

public class Hooks implements En{
    public static SunHttpServer httpServer;
    public static StandardWebServer standardWebServer;
    public static ControllerScanner controllerScanner;
    public static ControllerMapper controllerMapper;

    public Hooks(){
        After(() -> {
            if(standardWebServer.isRunning())
                standardWebServer.stop();
        });

        Before(() -> {
            controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller");
            controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());
            httpServer = new SunHttpServer();
        });
    }
}
