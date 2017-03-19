package cabanas.garcia.ismael.opportunity.controller.rest;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithDataForApiStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserCreateControllerTest {

    public static final String USER_NAME_ISMAEL = "ismael";
    public static final String PASSWORD = "changeIt";
    public static final String ADMIN_PAGE1_ROLES = "Admin,Page1";
    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userParam;

    private final User newUser = User.builder().username(USER_NAME_ISMAEL).password(PASSWORD).build();

    @Test
    public void method_is_post(){
        // given
        UserCreateController sut = new UserCreateController(userService);

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.POST)));
    }

    @Test
    public void path_is_users(){
        // given
        UserCreateController sut = new UserCreateController(userService);

        // when
        Resource actual = sut.getMappingPath();

        // then
        assertThat(actual.getPath(), is(equalTo("^/users$")));
    }

    @Test
    public void when_process_request_call_to_create_user_service(){
        // given
        UserCreateController sut = new UserCreateController(userService);
        Request anyRequest = Mockito.mock(Request.class);

        // when
        sut.process(anyRequest);

        // then
        verify(userService).create(any(User.class));
    }

    @Test
    public void process_request_return_view_with_status_code_201(){
        // given
        UserCreateController sut = new UserCreateController(userService);
        Request requestWithUserData = createRequestWithUserData(USER_NAME_ISMAEL, PASSWORD, ADMIN_PAGE1_ROLES);
        when(userService.create(Mockito.any())).thenReturn(newUser);

        // when
        View actual = sut.process(requestWithUserData);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_CREATED)));
    }

    @Test
    public void process_request_create_user_with_data_from_request(){
        // given
        UserCreateController sut = new UserCreateController(userService);
        Request requestWithUserData = createRequestWithUserData(USER_NAME_ISMAEL, PASSWORD, ADMIN_PAGE1_ROLES);
        when(userService.create(Mockito.any())).thenReturn(newUser);

        // when
        View actual = sut.process(requestWithUserData);

        // then
        verify(userService).create(userParam.capture());

        assertThat(userParam.getValue().getUsername(), is(equalTo(USER_NAME_ISMAEL)));
        assertThat(userParam.getValue().getRoles().size(), is(equalTo(2)));
    }

    private Request createRequestWithUserData(String username, String password, String roles) {
        HttpExchange httpExchange = new HttpExchangeWithDataForApiStub(username, password, roles);
        return RequestFactory.create(httpExchange);
    }
}
