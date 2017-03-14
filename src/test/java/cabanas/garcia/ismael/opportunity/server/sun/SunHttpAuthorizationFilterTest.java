package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(MockitoJUnitRunner.class)
public class SunHttpAuthorizationFilterTest {

    @Mock
    private Filter.Chain chain;

    @Mock
    private SessionRepository sessionRepository;

    @Captor
    private ArgumentCaptor<Session> sessionCaptor;

    @Test
    public void if_authenticated_user_request_a_private_resource_then_the_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeAuthenticatedUserStub("/page1");
        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionRepository);
        sut.getConfiguration().addPrivateResource("/page1");

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId("aSessionId").user(User.builder().username("ismael").build()).build();
        when(sessionRepository.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void if_authenticated_user_request_a_non_private_resource_then_the_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeAuthenticatedUserStub("/page2");
        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");

        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void if_unauthenticated_user_request_a_private_resource_then_redirect_to_login_view() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/page1");

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration);

        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }

    @Test
    public void if_unauthenticated_user_request_a_private_resource_then_set_location_response_header_with_redirect_path_value() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/page1");

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration);
        sut.getConfiguration().redirectPath("/login");

        // when
        sut.doFilter(httpExchange, chain);

        // then
        Headers headers = httpExchange.getResponseHeaders();
        String locationHeaderValue = headers.getFirst(ResponseHeaderConstants.LOCATION);
        assertThat(locationHeaderValue, is(equalTo(sut.getConfiguration().getRedirectPath())));
    }

    @Test
    public void if_unauthenticated_user_request_a_non_private_resource_then_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/publicPage");

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void if_request_with_valid_session_cookie_to_a_non_private_resource_then_session_is_generated() throws Exception{
        // given
        String aSessionId = "aSessionId";
        HttpExchange httpExchange = new HttpExchangeWithSessionCookieStub("/page1", aSessionId);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");

        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionRepository);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionRepository.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionRepository).read(nonExpiredSession.getSessionId());
        verify(httpExchangeSpy).setAttribute("session", nonExpiredSession);
    }

    @Test
    public void if_request_with_valid_session_cookie_to_a_non_private_resource_then_session_is_updated() throws Exception{
        // given
        String aSessionId = "aSessionId";
        HttpExchange httpExchange = new HttpExchangeWithSessionCookieStub("/page1", aSessionId);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionRepository);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionRepository.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionRepository).read(nonExpiredSession.getSessionId());
        verify(sessionRepository).persist(any());
        verify(httpExchangeSpy).setAttribute(Mockito.anyString(), sessionCaptor.capture());

        Session sessionUpdated = sessionCaptor.getValue();
        assertThat(sessionUpdated.getLastAccess(), is(greaterThan(nonExpiredSession.getLastAccess())));
    }

    @Test
    public void should_delete_session_from_repository_if_session_has_expired() throws Exception{
        // given
        String aSessionId = "aSessionId";
        HttpExchange httpExchange = new HttpExchangeWithSessionCookieStub("/page1", aSessionId);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource("/page1");
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionRepository);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session session = Session.builder().timeout(1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionRepository.read(anyString())).thenReturn(Optional.of(session));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionRepository).delete(session.getSessionId());
    }

    @Test
    public void description(){
        // given
        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionRepository);

        // when
        String actual = sut.description();

        // then
        assertThat(actual, is(equalTo(SunHttpAuthorizationFilter.AUTHENTICATION_FILTER)));
    }

    private Session expiredSession() {
        Session sessionExpired = mock(Session.class);
        when(sessionExpired.hasExpired()).thenReturn(true);

        return sessionExpired;
    }

}
