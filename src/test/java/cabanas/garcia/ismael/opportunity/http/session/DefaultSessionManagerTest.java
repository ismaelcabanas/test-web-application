package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionCookieStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionStub;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSessionManagerTest {

    @Mock
    private SessionRepository sessionRepository;

    @Test
    public void should_return_empty_session_when_not_exist_session_cookie(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        Request requetWithoutSessionCookie = createRequestWithoutSessionCookie();

        // when
        Optional<Session> actual = sut.validate(requetWithoutSessionCookie);

        // then
        assertThat(actual.isPresent(), is(equalTo(false)));
    }

    @Test
    public void should_return_empty_session_when_exist_session_cookie_but_the_session_not_is_persisted(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        String aSessionId = "aSessionId";
        Request requetWithSessionCookie = createRequestWithSessionCookie(aSessionId);

        when(sessionRepository.read(aSessionId)).thenReturn(Optional.empty());

        // when
        Optional<Session> actual = sut.validate(requetWithSessionCookie);

        // then
        verify(sessionRepository).read(aSessionId);
        verifyNoMoreInteractions(sessionRepository);

        assertThat(actual.isPresent(), is(equalTo(false)));
    }

    @Test
    public void should_return_empty_session_when_exist_session_expired(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        String aSessionId = "aSessionId";
        Request requetWithSessionCookie = createRequestWithSessionCookie(aSessionId);

        Session sessionExpired = Mockito.mock(Session.class);
        when(sessionExpired.hasExpired()).thenReturn(true);
        when(sessionRepository.read(aSessionId)).thenReturn(Optional.of(sessionExpired));

        // when
        Optional<Session> actual = sut.validate(requetWithSessionCookie);

        // then
        verify(sessionRepository).read(aSessionId);

        assertThat(actual.isPresent(), is(equalTo(false)));
    }

    @Test
    public void should_return_session_when_exist_session_a_valid_session(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        String aSessionId = "aSessionId";
        Request requetWithSessionCookie = createRequestWithSessionCookie(aSessionId);

        Session sessionValid = Session.builder().sessionId(aSessionId).timeout(3000*1000).lastAccess(DateUtil.now()).build();
        when(sessionRepository.read(aSessionId)).thenReturn(Optional.of(sessionValid));

        // when
        Optional<Session> actual = sut.validate(requetWithSessionCookie);

        // then
        assertThat(actual.isPresent(), is(equalTo(true)));
    }

    @Test
    public void should_invalidate_session(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        String aSessionId = "aSessionId";
        Request requestWithSessionCookie = createRequestWithSession(aSessionId);

        Session sessionSpy = Mockito.spy(requestWithSessionCookie.getSession().get());

        // when
        sut.invalidate(sessionSpy);

        // then
        verify(sessionRepository).delete(aSessionId);
        verify(sessionSpy).invalidate();
    }

    private Request createRequestWithSession(String aSessionId) {
        HttpExchange httpExchange =
                 new HttpExchangeWithSessionStub("/page1", aSessionId);

        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithSessionCookie(String aSessionId) {
        HttpExchange httpExchange =
                new HttpExchangeWithSessionCookieStub("/page1", aSessionId);

        return RequestFactory.create(httpExchange);
    }

    private Request createRequestWithoutSessionCookie() {
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("/page1");

        return RequestFactory.create(httpExchange);
    }
}
