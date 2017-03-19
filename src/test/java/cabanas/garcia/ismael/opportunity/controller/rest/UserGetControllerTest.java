package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithPathVariableStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserGetControllerTest {

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
        UserGetController sut = new UserGetController(userService);

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.GET)));
    }

    @Test
    public void path_is_users(){
        // given
        UserGetController sut = new UserGetController(userService);

        // when
        Resource actual = sut.getMappingPath();

        // then
        assertThat(actual.getPath(), is(equalTo("^/users/.*$")));
    }

    @Test
    public void process_should_find_user(){
        // given
        UserGetController sut = new UserGetController(userService);
        Request getUserRequest = createGetUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

        // when
        View actual = sut.process(getUserRequest);

        // then
        verify(userService).findByUsername(USER_NAME_ISMAEL);
    }

    @Test
    public void process_request_return_view_with_status_code_200(){
        // given
        UserGetController sut = new UserGetController(userService);
        Request getUserRequest = createGetUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

        // when
        View actual = sut.process(getUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_OK)));
    }

    @Test
    public void process_request_return_resource_not_found_with_status_code_404_when_not_exist_user_to_update(){
        // given
        UserGetController sut = new UserGetController(userService);
        Request getUserRequest = createGetUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        View actual = sut.process(getUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_NOT_FOUND)));
    }

    private Request createGetUserRequest(String username) {
        HttpExchange httpExchange = new HttpExchangeWithPathVariableStub("/users", username);
        return RequestFactory.create(httpExchange);
    }

}
