package cabanas.garcia.ismael.opportunity.server.sun;

import java.util.HashMap;
import java.util.Map;

public final class ServerConfiguration {

    public static final String SESSION_TIMEOUT = "session_timeout";
    public static final String REDIRECT_LOGOUT = "redirect_logout";

    private static ServerConfiguration instance = null;
    private Map<String, Object> configuration;

    private ServerConfiguration() {
        configuration = new HashMap<>();
    }

    public static ServerConfiguration getInstance() {
        if(instance == null)
            instance = new ServerConfiguration();
        return instance;
    }

    public void add(String propertyName, Object propertyValue) {
        this.configuration.put(propertyName, propertyValue);
    }

    public Object get(String propertyName) {
        return this.configuration.get(propertyName);
    }
}
