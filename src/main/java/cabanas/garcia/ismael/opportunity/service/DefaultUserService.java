package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.User;

import java.util.Optional;

public class DefaultUserService implements UserService{
    @Override
    public Optional<User> login(String username, String password) {
        if(username == null || password == null)
            return Optional.empty();

        if(username.equalsIgnoreCase("ismael")
                && password.equalsIgnoreCase("changeIt")){
            return Optional.of(User.builder().username(username).build());
        }

        return Optional.empty();
    }
}
