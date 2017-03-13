package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private static InMemoryUserRepository instance = null;
    private Map<String, User> repository = new HashMap<>();

    private InMemoryUserRepository(){}

    @Override
    public User persist(User newUser) {
        log.debug("Persist user {} in repository", newUser);
        repository.put(newUser.getUsername(), newUser);
        return newUser;
    }

    @Override
    public Optional<User> read(String username) {
        log.debug("Getting user {} from repository", username);
        return Optional.ofNullable(repository.get(username));
    }

    public static InMemoryUserRepository getInstance(){
        if(instance == null)
            instance = new InMemoryUserRepository();
        return instance;
    }

    @Override
    public User update(User updateUser) {
        log.debug("Updating user {} in repository", updateUser);
        Optional<User> user = read(updateUser.getUsername());
        if(user.isPresent()) {
            User userFounded = user.get();

            User userUpdated = User.builder()
                    .username(userFounded.getUsername())
                    .password(userFounded.getPassword())
                    .roles(updateUser.getRoles())
                    .build();
            repository.put(userFounded.getUsername(), userUpdated);

            return userUpdated;
        }
        return null;
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }

    @Override
    public void delete(String username) {
        log.debug("Deleting user {} from repository", username);
        repository.remove(username);
    }
}
