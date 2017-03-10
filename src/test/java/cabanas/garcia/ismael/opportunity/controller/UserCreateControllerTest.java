package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithDataForApiStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserCreateControllerTest {

    @Mock
    private UserService userService;

    @Test
    public void method_is_post(){
        // given
        UserCreateController sut = new UserCreateController(userService);

        // when
        String actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodConstants.POST)));
    }

    @Test
    public void path_is_users(){
        // given
        UserCreateController sut = new UserCreateController(userService);

        // when
        String actual = sut.getMappingPath();

        // then
        assertThat(actual, is(equalTo("/users")));
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
    public void process_request_return_view_with_user_created_data(){
        // given
        UserCreateController sut = new UserCreateController(userService);
        Request requestWithUserData = createRequestWithUserData("ismael", "changeIt", "Admin,Page1");

        // when
        View actual = sut.process(requestWithUserData);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_CREATED)));
    }

    private Request createRequestWithUserData(String username, String password, String roles) {
        HttpExchange httpExchange = new HttpExchangeWithDataForApiStub(username, password, roles);
        return RequestFactory.create(httpExchange);
    }
}
