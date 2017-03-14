package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.http.session.SessionValidator;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.service.PrivateResourcesService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SunHttpAuthorizationFilterTest {

    private static final String PATH_PAGE_1 = "/page1";
    private static final String PATH_LOGIN = "/login";
    private static final Resource PRIVATE_RESOURCE_PAGE_1 = Resource.builder().path(PATH_PAGE_1).build();
    private static final Resource PUBLIC_RESOURCE = Resource.builder().path(PATH_LOGIN).build();
    private static final Roles ROLES_ADMIN_PAGE2 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page2").build(), Role.builder().name("Admin").build())).build();
    private static final User AUTHENTICATED_USER = User.builder().username("Admin").password("Admin").roles(ROLES_ADMIN_PAGE2).build();
    private static final Optional<Session> VALID_SESSION = Optional.of(Session.create(AUTHENTICATED_USER));
    private static final Optional<Session> INVALID_SESSION = Optional.empty();
    private static final String PATH_FORBIDDEN = "/forbidden";

    @Mock
    private Filter.Chain chain;

    @Mock
    private SessionValidator sessionValidator;

    @Mock
    private PermissionChecker permissionChecker;

    @Mock
    private PrivateResourcesService privateResourcesService;

    @Captor
    private ArgumentCaptor<Session> sessionCaptor;



    @Test
    public void should_chain_doFilter_when_resource_is_non_private() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PUBLIC_RESOURCE.getPath());

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource(PATH_PAGE_1);

        SunHttpAuthorizationFilter sut =
                new SunHttpAuthorizationFilter(configuration, sessionValidator, permissionChecker, privateResourcesService);

        when(privateResourcesService.hasResource(any())).thenReturn(false);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(privateResourcesService).hasResource(PUBLIC_RESOURCE);
        verifyZeroInteractions(sessionValidator, permissionChecker);
    }

    @Test
    public void should_chain_doFilter_when_resource_is_private_and_user_session_authenticated_and_user_authorizated() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PRIVATE_RESOURCE_PAGE_1.getPath());

        Request request = RequestFactory.create(httpExchange);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource(PRIVATE_RESOURCE_PAGE_1.getPath());

        SunHttpAuthorizationFilter sut =
                new SunHttpAuthorizationFilter(configuration, sessionValidator, permissionChecker, privateResourcesService);

        when(privateResourcesService.hasResource(any())).thenReturn(true);
        when(sessionValidator.validate(request)).thenReturn(VALID_SESSION);
        when(permissionChecker.hasPermission(AUTHENTICATED_USER, PRIVATE_RESOURCE_PAGE_1)).thenReturn(true);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(privateResourcesService).hasResource(PRIVATE_RESOURCE_PAGE_1);
        verify(chain).doFilter(httpExchange);
        verify(sessionValidator).validate(any());
        verify(permissionChecker).hasPermission(any(), any());
    }

    @Test
    public void should_redirect_to_login_when_resource_is_private_and_invalid_user_session() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PRIVATE_RESOURCE_PAGE_1.getPath());

        Request request = RequestFactory.create(httpExchange);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource(PRIVATE_RESOURCE_PAGE_1.getPath());
        configuration.redirectPath(PATH_LOGIN);


        SunHttpAuthorizationFilter sut =
                new SunHttpAuthorizationFilter(configuration, sessionValidator, permissionChecker, privateResourcesService);

        when(privateResourcesService.hasResource(any())).thenReturn(true);
        when(sessionValidator.validate(request)).thenReturn(INVALID_SESSION);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(privateResourcesService).hasResource(PRIVATE_RESOURCE_PAGE_1);
        verify(sessionValidator).validate(any());
        verifyZeroInteractions(chain, permissionChecker);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);

        Headers responseHeaders = httpExchange.getResponseHeaders();
        List<String> headersList = responseHeaders.get(ResponseHeaderConstants.LOCATION);
        assertThat(headersList, is(notNullValue()));
        assertThat(headersList.isEmpty(), is(equalTo(false)));
        assertThat(headersList.get(0), is(equalTo(PATH_LOGIN)));
    }

    @Test
    public void should_redirect_to_forbidden_when_resource_is_private_and_valid_user_session_but_user_dont_have_permisions_to_access_to_resource() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PRIVATE_RESOURCE_PAGE_1.getPath());

        Request request = RequestFactory.create(httpExchange);

        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        configuration.addPrivateResource(PRIVATE_RESOURCE_PAGE_1.getPath());
        configuration.redirectPath(PATH_LOGIN);
        configuration.redirectForbiddenPath(PATH_FORBIDDEN);

        SunHttpAuthorizationFilter sut =
                new SunHttpAuthorizationFilter(configuration, sessionValidator, permissionChecker, privateResourcesService);

        when(privateResourcesService.hasResource(any())).thenReturn(true);
        when(sessionValidator.validate(request)).thenReturn(VALID_SESSION);
        when(permissionChecker.hasPermission(AUTHENTICATED_USER, PRIVATE_RESOURCE_PAGE_1)).thenReturn(false);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(privateResourcesService).hasResource(PRIVATE_RESOURCE_PAGE_1);
        verify(sessionValidator).validate(any());
        verifyZeroInteractions(chain);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);

        Headers responseHeaders = httpExchange.getResponseHeaders();
        List<String> headersList = responseHeaders.get(ResponseHeaderConstants.LOCATION);
        assertThat(headersList, is(notNullValue()));
        assertThat(headersList.isEmpty(), is(equalTo(false)));
        assertThat(headersList.get(0), is(equalTo(PATH_FORBIDDEN)));
    }

/*    @Test
    public void if_authenticated_user_request_a_private_resource_then_the_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeAuthenticatedUserStub("/page1");
        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionValidator);
        sut.getConfiguration().addPrivateResource("/page1");

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId("aSessionId").user(User.builder().username("ismael").build()).build();
        when(sessionValidator.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

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

        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionValidator);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionValidator.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionValidator).read(nonExpiredSession.getSessionId());
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
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionValidator);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session nonExpiredSession = Session.builder().timeout(-1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionValidator.read(anyString())).thenReturn(Optional.of(nonExpiredSession));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionValidator).read(nonExpiredSession.getSessionId());
        verify(sessionValidator).persist(any());
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
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionValidator);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        Session session = Session.builder().timeout(1).sessionId(aSessionId).user(User.builder().username("ismael").build()).build();

        when(sessionValidator.read(anyString())).thenReturn(Optional.of(session));

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(sessionValidator).delete(session.getSessionId());
    }

    @Test
    public void description(){
        // given
        SunHttpAuthorizationFilter.AuthorizationFilterConfiguration configuration =
                new SunHttpAuthorizationFilter.AuthorizationFilterConfiguration();
        SunHttpAuthorizationFilter sut = new SunHttpAuthorizationFilter(configuration, sessionValidator);

        // when
        String actual = sut.description();

        // then
        assertThat(actual, is(equalTo(SunHttpAuthorizationFilter.AUTHENTICATION_FILTER)));
    }

    private Session expiredSession() {
        Session sessionExpired = mock(Session.class);
        when(sessionExpired.hasExpired()).thenReturn(true);

        return sessionExpired;
    }*/

}
