package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.model.User;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class DefaultUserRepositoryTest {

    @Test
    public void persist_user(){
        // given
        InMemoryUserRepository sut = new InMemoryUserRepository();
        User newUser = User.builder().username("ismael").password("changeIt").build();

        // when
        sut.persist(newUser);

        // then
        User userPersisted = sut.read(newUser.getUsername());

        Assert.assertThat(userPersisted.getUsername(), Is.is(IsEqual.equalTo("ismael")));
    }
}
