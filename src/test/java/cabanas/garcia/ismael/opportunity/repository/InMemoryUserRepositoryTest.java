package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.*;
import static org.junit.Assert.assertThat;

public class InMemoryUserRepositoryTest {

    public static final String USER_NAME_ISMAEL = "ismael";

    @Test
    public void persist_user(){
        // given
        InMemoryUserRepository sut = InMemoryUserRepository.getInstance();
        User newUser = User.builder().username(USER_NAME_ISMAEL).password("changeIt").build();

        // when
        User userPersisted = sut.persist(newUser);

        // then
        assertThat(userPersisted.getUsername(), is(equalTo(USER_NAME_ISMAEL)));
        assertThat(userPersisted.getRoles(), is(nullValue()));
    }

    @Test
    public void update_user(){
        // given
        InMemoryUserRepository sut = InMemoryUserRepository.getInstance();

        Roles roles = Roles.builder().roleList(new ArrayList<>()).build();
        roles.add("Admin");
        User newUser = User.builder().username(USER_NAME_ISMAEL).password("changeIt").roles(roles).build();
        sut.persist(newUser);

        Roles newRoles = Roles.builder().roleList(new ArrayList<>()).build();
        newRoles.add("Page2");
        newRoles.add("Page3");
        User updateUser = User.builder().username(newUser.getUsername()).roles(newRoles).build();

        // when
        User updatedUser = sut.update(updateUser);

        // then
        assertThat(updatedUser.getUsername(), is(equalTo(USER_NAME_ISMAEL)));
        assertThat(updatedUser.getRoles(), is(equalTo(newRoles)));
    }

    @Test
    public void in_memory_persitent_storage(){
        // given
        InMemoryUserRepository sut = InMemoryUserRepository.getInstance();
        User newUser = User.builder().username(USER_NAME_ISMAEL).password("changeIt").build();
        sut.persist(newUser);

        // when
        sut = InMemoryUserRepository.getInstance();

        // then
        Optional<User> userPersisted = sut.read(newUser.getUsername());

        assertThat(userPersisted.get().getUsername(), is(equalTo(USER_NAME_ISMAEL)));
    }
}
