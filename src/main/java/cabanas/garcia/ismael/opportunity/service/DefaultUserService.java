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
                return Optional.of(newUserWithoutPassword(userFromRepository.get()));
        }

        return Optional.empty();
    }

    @Override
    public User create(User newUser) {
        User userPersisted = userRepository.persist(newUser);

        return newUserWithoutPassword(userPersisted);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userPersisted = userRepository.read(username);

        return userPersisted.isPresent() ? Optional.of(newUserWithoutPassword(userPersisted.get())) : Optional.empty();
    }

    private User newUserWithoutPassword(User user) {
        return User.builder().username(user.getUsername()).roles(user.getRoles()).build();
    }

    @Override
    public User update(User updateUser) {
        User userUpdated = userRepository.update(updateUser);
        return newUserWithoutPassword(userUpdated);
    }

    @Override
    public void delete(String username) {

    }
}
