package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private Map<String, User> repository = new HashMap<>();

    @Override
    public User persist(User newUser) {
        repository.put(newUser.getUsername(), newUser);
        return newUser;
    }

    @Override
    public User read(String username) {
        return repository.get(username);
    }
}
