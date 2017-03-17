package cabanas.garcia.ismael.opportunity.http.session;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionCookieStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionStub;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import cabanas.garcia.ismael.opportunity.util.DefaultTimeProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDRandomProvider;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSessionManagerTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UUIDProvider uuidProvider;

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

        User anUser = User.builder().username("user1").build();
        Session sessionValid = new Session(anUser, 60, new UUIDRandomProvider(), new DefaultTimeProvider());
        when(sessionRepository.read(aSessionId)).thenReturn(Optional.of(sessionValid));

        Request requestSpy = Mockito.spy(requetWithSessionCookie);

        // when
        Optional<Session> actual = sut.validate(requestSpy);

        // then
        verify(requestSpy).getSessionCookie();

        assertThat(actual.isPresent(), is(equalTo(true)));
    }

    @Test
    public void should_invalidate_session(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository);

        String aSessionId = "aSessionId";
        Session session = Session.builder().sessionId(aSessionId).build();

        Session sessionSpy = Mockito.spy(session);

        // when
        sut.invalidate(sessionSpy);

        // then
        verify(sessionRepository).delete(aSessionId);
        verify(sessionSpy).invalidate();
    }

    @Test
    public void should_create_session(){
        // given
        SessionManager sut = new DefaultSessionManager(sessionRepository, uuidProvider);

        String aSessionId = "aSessionId";
        int sessionTimeout = 100;
        User user = User.builder().username("user1").build();

        when(uuidProvider.generateUUID()).thenReturn(aSessionId);

        // when
        Session actual = sut.create(user, sessionTimeout);

        // then
        verify(sessionRepository).persist(any());
        verify(uuidProvider).generateUUID();

        assertThat(actual, is(notNullValue()));
        assertThat(actual.getSessionId(), is(equalTo(aSessionId)));
        assertThat(actual.getUser(), is(equalTo(user)));
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
