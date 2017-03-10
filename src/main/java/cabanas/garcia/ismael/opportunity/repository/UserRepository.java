package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;

public interface UserRepository {
    User persist(User newUser);

    User read(String username);
}
