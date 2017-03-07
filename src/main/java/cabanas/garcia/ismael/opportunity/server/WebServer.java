package cabanas.garcia.ismael.opportunity.server;

/**
 * Created by XI317311 on 28/02/2017.
 */
public interface WebServer {

    void start();

    void stop();

    boolean isRunning();

    void addConfiguration(Configuration configuration);
}
