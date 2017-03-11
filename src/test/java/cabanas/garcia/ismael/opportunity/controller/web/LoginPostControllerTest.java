package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.controller.web.LoginPostController;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithCredentialsStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithCredentialsAndRedirectParam;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPostControllerTest {

    @Mock
    private UserService userServiceLoginSuccess;

    @Mock
    private UserService userServiceLoginFailed;

    @Before
    public void setUp(){
        when(userServiceLoginFailed.login(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<User> ismael = Optional.of(User.builder().username("ismael").build());
        when(userServiceLoginSuccess.login(anyString(), anyString())).thenReturn(ismael);
    }

    @Test
    public void when_login_then_call_to_login_service(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginFailed);
        Request requestWithFailedCredentials = createRequestWithCredentials();

        // when
        sut.process(requestWithFailedCredentials);

        // then
        Mockito.verify(userServiceLoginFailed).login(anyString(), anyString());
    }

    @Test
    public void when_login_with_failed_credentials_then_return_unauthorized_view(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginFailed);
        Request requestWithFailedCredentials = createRequestWithCredentials();

        // when
        View actual = sut.process(requestWithFailedCredentials);

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_UNAUTHORIZED)));
    }

    @Test
    public void when_login_with_succes_credentials_then_return_home_view(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess);
        Request requestWithSuccessCredentials = createRequestWithCredentials();

        // when
        View actual = sut.process(requestWithSuccessCredentials);

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_OK)));
        assertThat(new String(actual.render().getContent()), containsString("Home"));
    }

    @Test
    public void when_login_with_succes_credentials_after_request_resource_then_return_redirect_view(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess);
        Request requestWithSuccessCredentialsAndRedirectParameter = createRequestWithCredentialsAndRedirectParameter();

        // when
        View actual = sut.process(requestWithSuccessCredentialsAndRedirectParameter);

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_MOVED_TEMP)));
    }

    private Request createRequestWithCredentialsAndRedirectParameter() {
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParam();
        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithCredentials() {
        HttpExchange httpExchange = new HttpExchangeWithCredentialsStub();
        return RequestFactory.create(httpExchange);
    }
}
