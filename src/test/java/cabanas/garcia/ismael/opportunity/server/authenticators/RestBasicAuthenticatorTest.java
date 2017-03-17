package cabanas.garcia.ismael.opportunity.server.authenticators;

import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestBasicAuthenticatorTest {

    private static final String USER_1 = "user1";
    private static final Roles ROLES_ADMIN = Roles.builder().roleList(Arrays.asList(Role.ADMIN)).build();
    private static final Optional<User> ADMIN_USER = Optional.of(User.builder().username(USER_1).password("****").roles(ROLES_ADMIN).build());

    @Mock
    private UserService userService;

    @Test
    public void should_return_true_if_login_user_successfully(){
        // given
        RestBasicAuthenticator sut = new RestBasicAuthenticator("test", userService);

        String aUsername = ADMIN_USER.get().getUsername();
        String aPassword = ADMIN_USER.get().getPassword();
        when(userService.login(aUsername, aPassword)).thenReturn(ADMIN_USER);

        // when
        boolean actual = sut.checkCredentials(aUsername, aPassword);

        // then
        Mockito.verify(userService).login(aUsername, aPassword);

        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void should_return_false_if_login_user_failed(){
        // given
        RestBasicAuthenticator sut = new RestBasicAuthenticator("test", userService);

        String aUsername = ADMIN_USER.get().getUsername();
        String aPassword = ADMIN_USER.get().getPassword();
        when(userService.login(ADMIN_USER.get().getUsername(), ADMIN_USER.get().getPassword())).thenReturn(Optional.empty());

        // when
        boolean actual = sut.checkCredentials(aUsername, aPassword);

        // then
        Mockito.verify(userService).login(aUsername, aPassword);

        assertThat(actual, is(equalTo(false)));
    }
}
