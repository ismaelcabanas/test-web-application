package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private static InMemoryUserRepository instance = null;

    private InMemoryUserRepository(){}

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

    public static InMemoryUserRepository getInstance(){
        if(instance == null)
            instance = new InMemoryUserRepository();
        return instance;
    }
}
