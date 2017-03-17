package cabanas.garcia.ismael.opportunity.server.authenticators;

import cabanas.garcia.ismael.opportunity.service.UserService;
import com.sun.net.httpserver.BasicAuthenticator;

public class RestBasicAuthenticator extends BasicAuthenticator {
    private final UserService userService;

    public RestBasicAuthenticator(String realm, UserService userService) {
        super(realm);
        this.userService = userService;
    }

    @Override
    public boolean checkCredentials (String username, String password) {
        return userService.login(username, password).isPresent();
    }
}
