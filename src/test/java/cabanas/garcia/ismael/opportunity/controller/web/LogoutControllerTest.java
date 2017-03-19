package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionManager;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeWithSessionStub;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LogoutControllerTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionManager sessionManager;

    @Test
    public void mapping_path(){
        // given
        LogoutController sut = new LogoutController(sessionManager, "");

        // when
        Resource actual = sut.getMappingPath();

        // then
        assertThat(actual.getPath(), is(equalTo(LogoutController.PATH)));
    }

    @Test
    public void method_path(){
        // given
        LogoutController sut = new LogoutController(sessionManager, "");

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.GET)));
    }

    @Test
    public void process_request_should_invalidate_session(){
        // given
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionManager);
        Request request = createRequestWithValidSession(aSessionId);

        // when
        sut.process(request);

        // then
        assertThat(request.getSession().isPresent(), is(equalTo(false)));
        //assertThat(request.getSession().get().hasExpired(), is(equalTo(true)));
    }

    @Test
    public void process_request_should_delete_session(){
        // given
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionManager);
        Request request = createRequestWithValidSession(aSessionId);

        // when
        sut.process(request);

        // then
        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);
        Mockito.verify(sessionManager).invalidate(sessionArgumentCaptor.capture());

        Session session = sessionArgumentCaptor.getValue();
        assertThat(session, is(notNullValue()));
        assertThat(session.getSessionId(), is(equalTo(aSessionId)));
    }

    @Test
    public void process_request_should_return_redirect_view(){
        // given
        String redirectPath = "/login";
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionManager, redirectPath);
        Request request = createRequestWithValidSession(aSessionId);

        // when
        View actual = sut.process(request);

        // then
        assertThat(actual.render().getStatusCode(), is(equalTo(HttpURLConnection.HTTP_MOVED_TEMP)));
        assertThat(actual.render().getRedirectPath(), is(equalTo(redirectPath)));
    }

    @Test
    public void process_request_not_should_invalidate_session_when_not_exist_session(){
        // given
        String redirectPath = "/login";
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionManager, redirectPath);
        Request request = createRequestWithoutSession();

        // when
        View actual = sut.process(request);

        // then
        verifyZeroInteractions(sessionManager);
    }

    private Request createRequestWithoutSession() {
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("/logout");
        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithValidSession(String aSessionId) {
        HttpExchange httpExchange = new HttpExchangeWithSessionStub("/logout", aSessionId);
        return RequestFactory.create(httpExchange);
    }
}
