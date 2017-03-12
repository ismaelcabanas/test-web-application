package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithDataForApiStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserUpdateControllerTest {

    private static final String USER_NAME_ISMAEL = "ismael";
    private static final String ADMIN_PAGE1_ROLES = "Admin,Page1";

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userParam;

    private final User updateUser = User.builder().username(USER_NAME_ISMAEL).build();

    @Test
    public void method_is_post(){
        // given
        UserUpdateController sut = new UserUpdateController(userService);

        // when
        String actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodConstants.PUT)));
    }

    @Test
    public void path_is_users(){
        // given
        UserUpdateController sut = new UserUpdateController(userService);

        // when
        String actual = sut.getMappingPath();

        // then
        assertThat(actual, is(equalTo("/users")));
    }

    @Test
    public void process_should_update_user(){
        // given
        UserUpdateController sut = new UserUpdateController(userService);
        Request updateUserRequest = createUpdateUserRequest(USER_NAME_ISMAEL, ADMIN_PAGE1_ROLES);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

        // when
        View actual = sut.process(updateUserRequest);

        // then
        verify(userService).findByUsername(USER_NAME_ISMAEL);
        verify(userService).update(userParam.capture());

        User userToUpdate = userParam.getValue();
        assertThat(userToUpdate.getUsername(), Is.is(IsEqual.equalTo(USER_NAME_ISMAEL)));
        assertThat(userToUpdate.getRoles(), Is.is(IsEqual.equalTo(getRolesFrom(ADMIN_PAGE1_ROLES))));
    }

    @Test
    public void process_request_return_view_with_status_code_200(){
        // given
        UserUpdateController sut = new UserUpdateController(userService);
        Request updateUserRequest = createUpdateUserRequest(USER_NAME_ISMAEL, ADMIN_PAGE1_ROLES);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));
        when(userService.update(any())).thenReturn(updateUser);

        // when
        View actual = sut.process(updateUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_OK)));
    }

    @Test
    public void process_request_return_resource_not_foud_with_status_code_200_when_not_exist_user_to_update(){
        // given
        UserUpdateController sut = new UserUpdateController(userService);
        Request updateUserRequest = createUpdateUserRequest(USER_NAME_ISMAEL, ADMIN_PAGE1_ROLES);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        View actual = sut.process(updateUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_NOT_FOUND)));
    }

    private Request createUpdateUserRequest(String username, String roles) {
        HttpExchange httpExchange = new HttpExchangeWithDataForApiStub(username, roles);
        return RequestFactory.create(httpExchange);
    }

    private Roles getRolesFrom(String rolesSeparatedByComma){
        Roles roles = Roles.builder().roleList(new ArrayList<>()).build();

        String[] rolesSplitted = rolesSeparatedByComma.split(",");
        Arrays.stream(rolesSplitted).forEach(role -> roles.add(role));

        return roles;
    }
}
