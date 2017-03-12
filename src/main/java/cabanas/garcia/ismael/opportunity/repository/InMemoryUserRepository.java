package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private static InMemoryUserRepository instance = null;
    private Map<String, User> repository = new HashMap<>();

    private InMemoryUserRepository(){}

    @Override
    public User persist(User newUser) {
        repository.put(newUser.getUsername(), newUser);
        return newUser;
    }

    @Override
    public Optional<User> read(String username) {
        return Optional.ofNullable(repository.get(username));
    }

    public static InMemoryUserRepository getInstance(){
        if(instance == null)
            instance = new InMemoryUserRepository();
        return instance;
    }

    @Override
    public User update(User updateUser) {
        Optional<User> user = read(updateUser.getUsername());
        return User.builder()
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .roles(updateUser.getRoles())
                .build();
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }
}
