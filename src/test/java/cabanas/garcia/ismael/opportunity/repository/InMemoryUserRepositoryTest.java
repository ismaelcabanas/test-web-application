package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class InMemoryUserRepositoryTest {

    @Test
    public void persist_user(){
        // given
        InMemoryUserRepository sut = InMemoryUserRepository.getInstance();
        User newUser = User.builder().username("ismael").password("changeIt").build();

        // when
        sut.persist(newUser);

        // then
        Optional<User> userPersisted = sut.read(newUser.getUsername());

        Assert.assertThat(userPersisted.get().getUsername(), Is.is(IsEqual.equalTo("ismael")));
    }

    @Test
    public void in_memory_persitent_storage(){
        // given
        InMemoryUserRepository sut = InMemoryUserRepository.getInstance();
        User newUser = User.builder().username("ismael").password("changeIt").build();
        sut.persist(newUser);

        // when
        sut = InMemoryUserRepository.getInstance();

        // then
        Optional<User> userPersisted = sut.read(newUser.getUsername());

        Assert.assertThat(userPersisted.get().getUsername(), Is.is(IsEqual.equalTo("ismael")));
    }
}