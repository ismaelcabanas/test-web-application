package cabanas.garcia.ismael.opportunity.server.authenticators;

import cabanas.garcia.ismael.opportunity.repository.InMemoryUserRepository;
import cabanas.garcia.ismael.opportunity.service.DefaultUserService;
import cabanas.garcia.ismael.opportunity.service.UserService;
import com.sun.net.httpserver.BasicAuthenticator;

public class RestBasicAuthenticator extends BasicAuthenticator {
    public RestBasicAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials (String username, String password) {
        UserService userService = new DefaultUserService(InMemoryUserRepository.getInstance());
        return userService.login(username, password).isPresent();
    }
}
