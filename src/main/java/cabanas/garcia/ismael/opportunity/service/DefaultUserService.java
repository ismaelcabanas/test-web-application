package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;

import java.util.Optional;

public class DefaultUserService implements UserService{
    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userFromRepository = userRepository.read(username);
        if(userFromRepository.isPresent()){
            if(userFromRepository.get().getPassword().equals(password))
                return Optional.of(User.builder()
                        .username(userFromRepository.get().getUsername())
                        .roles(userFromRepository.get().getRoles())
                        .build());
        }

        return Optional.empty();
    }

    @Override
    public User create(User newUser) {
        User userPersisted = userRepository.persist(newUser);

        return User.builder().username(userPersisted.getUsername()).roles(userPersisted.getRoles()).build();
    }
}
