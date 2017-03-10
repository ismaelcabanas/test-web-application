package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void persist_data_user_when_create(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username("ismael").password("changeIt").build();
        when(userRepository.persist(any())).thenReturn(newUser);

        // when
        sut.create(newUser);

        // then
        verify(userRepository).persist(newUser);
    }
}
