package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.UserRepository;
import cabanas.garcia.ismael.opportunity.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    public static final String USERNAME = "ismael";
    public static final String PASSWORD = "changeIt";
    private static final Roles ROLES = TestUtil.getDefaultRoles();

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

    @Test
    public void findByUsername_should_return_user_from_repository(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).roles(ROLES).build();
        when(userRepository.read(Mockito.anyString())).thenReturn(Optional.of(newUser));

        // when
        Optional<User> user = sut.findByUsername(USERNAME);

        // then
        verify(userRepository).read(USERNAME);

        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getUsername(), is(equalTo(USERNAME)));
        assertThat(user.get().getPassword(),is(equalTo(DefaultUserService.MASK)));
        assertThat(user.get().getRoles(),is(equalTo(ROLES)));
    }

    @Test
    public void findByUsername_should_return_empty_user_if_user_not_exist_in_repository(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);
        when(userRepository.read(Mockito.anyString())).thenReturn(Optional.empty());

        // when
        Optional<User> user = sut.findByUsername(USERNAME);

        // then
        verify(userRepository).read(USERNAME);

        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void update_should_update_user_roles_in_repository(){
        // given
        Roles newRoles = Roles.builder().build();
        newRoles.add("Page1");

        DefaultUserService sut = new DefaultUserService(userRepository);
        User newUser = User.builder().username(USERNAME).password(PASSWORD).roles(newRoles).build();
        when(userRepository.update(any())).thenReturn(newUser);

        User updateUser = User.builder().username(newUser.getUsername()).roles(newRoles).build();

        // when
        User actual = sut.update(updateUser);

        // then
        verify(userRepository).update(updateUser);

        assertThat(actual.getUsername(), is(equalTo(USERNAME)));
        assertThat(actual.getPassword(),is(equalTo(DefaultUserService.MASK)));
        assertThat(actual.getRoles(),is(equalTo(newRoles)));
    }

    @Test
    public void delete_should_delete_user_from_repository(){
        // given
        DefaultUserService sut = new DefaultUserService(userRepository);

        // when
        sut.delete(USERNAME);

        // then
        verify(userRepository).delete(USERNAME);
    }

}
