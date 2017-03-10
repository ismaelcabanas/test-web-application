package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> login(String username, String password);

    User create(User newUser);
}
