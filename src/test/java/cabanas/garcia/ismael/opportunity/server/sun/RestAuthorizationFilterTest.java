package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.*;
import cabanas.garcia.ismael.opportunity.service.UserService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RestAuthorizationFilterTest {

    private static final String USER_1 = "user1";
    private static final String USERS_PATH = "/users";
    private static final Roles ROLES_ADMIN = Roles.builder().roleList(Arrays.asList(Role.ADMIN)).build();
    private static final Roles ROLES_PAGE1 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page1").build())).build();
    private static final Optional<User> ADMIN_USER = Optional.of(User.builder().username(USER_1).password("****").roles(ROLES_ADMIN).build());
    private static final Optional<User> NO_ADMIN_USER = Optional.of(User.builder().username(USER_1).password("****").roles(ROLES_PAGE1).build());

    @Mock
    private Filter.Chain chain;

    @Mock
    private UserService userService;

    @Mock
    private PermissionChecker permissionChecker;

    @Test
    public void should_filter_doChain_GET_requests_if_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestGetWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verifyZeroInteractions(userService, permissionChecker);
    }

    @Test
    public void should_filter_doChain_POST_requests_if_authenticated_user_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPostWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(true);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
    }

    @Test
    public void should_filter_doChain_PUT_requests_if_authenticated_user_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPutWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(true);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
    }

    @Test
    public void should_filter_doChain_DELETE_requests_if_authenticated_user_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestDeleteWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(true);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
    }

    @Test
    public void should_redirect_to_unauthorization_if_not_principal_in_request() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestWithoutPrincipalResourceStub(USERS_PATH);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain, userService, permissionChecker);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    @Test
    public void should_redirect_to_forbidden_POST_requests_if_authenticated_user_no_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPostWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(false);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
    }

    @Test
    public void should_redirect_to_forbidden_PUT_requests_if_authenticated_user_no_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPutWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(false);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
    }

    @Test
    public void should_redirect_to_forbidden_DELETE_requests_if_authenticated_user_no_has_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestDeleteWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService, permissionChecker);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(false);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain);
        verify(userService).findByUsername(USER_1);
        verify(permissionChecker).hasPermission(any(), any());
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
    }

}
