package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    public static final String USERNAME = "ismael";
    public static final String PASSWORD = "changeIt";
    @Mock
    private UserRepository userRepository;

    @Test
    public void persist_data_user_when_create(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).build();
        when(userRepository.persist(any())).thenReturn(newUser);

        // when
        sut.create(newUser);

        // then
        verify(userRepository).persist(newUser);
    }

    @Test
    public void login_user(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).build();
        when(userRepository.read(Mockito.anyString())).thenReturn(Optional.of(newUser));

        // when
        Optional<User> user = sut.login(USERNAME, PASSWORD);

        // then
        verify(userRepository).read(USERNAME);

        assertThat(user.get(), is(notNullValue()));
    }


    @Test
    public void login_user_not_found(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).build();
        when(userRepository.read(Mockito.anyString())).thenReturn(Optional.empty());

        // when
        Optional<User> user = sut.login(USERNAME, PASSWORD);

        // then
        verify(userRepository).read(USERNAME);

        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void login_valid_user_but_incorrect_password(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).build();
        when(userRepository.read(Mockito.anyString())).thenReturn(Optional.of(newUser));

        // when
        Optional<User> user = sut.login(USERNAME, "wrongPassword");

        // then
        verify(userRepository).read(USERNAME);

        assertThat(user.isPresent(), is(false));
    }
}