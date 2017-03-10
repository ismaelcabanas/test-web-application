package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.Optional;

public interface UserRepository {
    User persist(User newUser);

    Optional<User> read(String username);
}
