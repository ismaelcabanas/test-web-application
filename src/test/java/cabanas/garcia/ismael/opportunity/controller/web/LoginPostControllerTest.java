package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithCredentialsStub;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithCredentialsAndRedirectParamStub;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithSessionStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPostControllerTest {

    public static final String USERNAME_ISMAEL = "ismael";
    @Mock
    private UserService userServiceLoginSuccess;

    @Mock
    private UserService userServiceLoginFailed;

    @Mock
    private SessionManager sessionManager;

    @Before
    public void setUp(){
        when(userServiceLoginFailed.login(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<User> ismael = Optional.of(User.builder().username(USERNAME_ISMAEL).build());
        when(userServiceLoginSuccess.login(anyString(), anyString())).thenReturn(ismael);
    }

    @Test
    public void when_login_then_call_to_login_service(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginFailed, sessionManager);
        Request requestWithFailedCredentials = createRequestWithCredentials();

        // when
        sut.process(requestWithFailedCredentials);

        // then
        Mockito.verify(userServiceLoginFailed).login(anyString(), anyString());
    }

    @Test
    public void when_login_with_failed_credentials_then_return_unauthorized_view(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginFailed, sessionManager);
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
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager);
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
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager);
        Request requestWithSuccessCredentialsAndRedirectParameter = createRequestWithCredentialsAndRedirectParameter();

        // when
        RedirectView actual = (RedirectView) sut.process(requestWithSuccessCredentialsAndRedirectParameter);

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_MOVED_TEMP)));
        assertThat(actual.getRedirectPath(), is(equalTo(requestWithSuccessCredentialsAndRedirectParameter.getParameter(RequestConstants.REDIRECCT_PARAM))));
    }

    @Test
    public void mapping_path(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager);

        // when
        Resource actual = sut.getMappingPath();

        // then
        assertThat(actual.getPath(), is(equalTo(LoginPostController.PATH)));
    }

    @Test
    public void method_path(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager);

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.POST)));
    }

    @Test
    public void should_create_a_user_session_when_login_successfully(){
        // given
        int sessionTimeout = 3600;
        String aSessionId = "aSessionId";
        Request request = createRequestWithValidSession(aSessionId);

        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager, sessionTimeout);

        User user = User.builder().username(USERNAME_ISMAEL).build();
        Session aSession = Session.builder().sessionId(aSessionId).timeout(sessionTimeout).user(user).build();
        when(sessionManager.create(Mockito.any(), Mockito.anyInt())).thenReturn(aSession);

        Request requestSpy = Mockito.spy(request);

        // when
        View actual = sut.process(requestSpy);

        // then
        verify(requestSpy).setSession(aSession);
    }

    @Test
    public void should_persist_a_user_session_when_login_successfully(){
        // given
        LoginPostController sut = new LoginPostController(userServiceLoginSuccess, sessionManager);
        Request request = createRequestWithCredentialsAndRedirectParameter();

        // when
        View actual = sut.process(request);

        // then
        verify(sessionManager).create(Mockito.any(), Mockito.anyInt());
    }

    private Request createRequestWithCredentialsAndRedirectParameter() {
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParamStub(USERNAME_ISMAEL, "changeIt", "/page1");
        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithCredentials() {
        HttpExchange httpExchange = new HttpExchangeWithCredentialsStub();
        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithValidSession(String aSessionId) {
        HttpExchange httpExchange = new HttpExchangeWithSessionStub("/logout", aSessionId);
        return RequestFactory.create(httpExchange);
    }
}
