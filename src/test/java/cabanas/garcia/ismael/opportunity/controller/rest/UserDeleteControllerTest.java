package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithPathVariableStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDeleteControllerTest {
    private static final String USER_NAME_ISMAEL = "ismael";

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<String> usernameParam;

    private final User updateUser = User.builder().username(USER_NAME_ISMAEL).build();

    private UserDeleteController sut;

    @Before
    public void setUp() throws Exception {
        sut = new UserDeleteController(userService);
    }

    @Test
    public void method_is_delete(){
        // when
        String actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodConstants.DELETE)));
    }

    @Test
    public void path_is_users(){
        // when
        String actual = sut.getMappingPath();

        // then
        assertThat(actual, is(equalTo("^/users/.*")));
    }

    @Test
    public void process_should_delete_user(){
        // given
        Request deleteUserRequest = createDeleteUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

        // when
        View actual = sut.process(deleteUserRequest);

        // then
        verify(userService).findByUsername(USER_NAME_ISMAEL);
        verify(userService).delete(usernameParam.capture());

        String usernameDeleted = usernameParam.getValue();
        assertThat(usernameDeleted, is(equalTo(USER_NAME_ISMAEL)));
    }

    @Test
    public void process_request_return_view_with_status_code_204(){
        // given
        Request deleteUserRequest = createDeleteUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mock(User.class)));

        // when
        View actual = sut.process(deleteUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_NO_CONTENT)));
    }

    @Test
    public void process_request_return_resource_not_found_with_status_code_404_when_not_exist_user_to_update(){
        // given
        Request deleteUserRequest = createDeleteUserRequest(USER_NAME_ISMAEL);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        View actual = sut.process(deleteUserRequest);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_NOT_FOUND)));
    }

    private Request createDeleteUserRequest(String username) {
        HttpExchange httpExchange = new HttpExchangeWithPathVariableStub("/users", username);
        return RequestFactory.create(httpExchange);
    }

}
