package cabanas.garcia.ismael.opportunity.server.sun.handlers;

import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RestHandlerTest {

    public static final String USER_1 = "user1";
    public static final String USERS_PATH = "/users";
    private static final Roles ROLES_ADMIN = Roles.builder().roleList(Arrays.asList(Role.ADMIN)).build();
    private static final Roles ROLES_PAGE1 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page1").build())).build();
    private static final Optional<User> ADMIN_USER = Optional.of(User.builder().username(USER_1).password("****").roles(ROLES_ADMIN).build());
    private static final Optional<User> NO_ADMIN_USER = Optional.of(User.builder().username(USER_1).password("****").roles(ROLES_PAGE1).build());

    @Mock
    private Controllers controllers;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @Test
    public void test(){

    }
    /*@Test
    public void should_handle_GET_requests_if_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestGetWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService);

        // when
        sut.handle(httpExchange);

        // then
        verify(controllers).select(requestCaptor.capture());
        assertThat(requestCaptor.getValue(), is(equalTo(request)));
        verifyZeroInteractions(userService);
    }

    @Test
    public void should_select_unauthorized_controller_if_not_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestWithoutPrincipalResourceStub(USERS_PATH);

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService);

        // when
        sut.handle(httpExchange);

        // then
        verify(controllers).unauthorized(any());
        verifyZeroInteractions(userService);
    }*/
    /*
    @Test
    public void should_filter_doChain_POST_requests_if_authenticated_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPostWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
    }

    @Test
    public void should_filter_doChain_PUT_requests_if_authenticated_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPutWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
    }

    @Test
    public void should_filter_doChain_DELETE_requests_if_authenticated_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestDeleteWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(ADMIN_USER);

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
        verify(userService).findByUsername(USER_1);
    }

    @Test
    public void should_redirect_to_unauthorization_if_not_principal_in_request() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestWithoutPrincipalResourceStub(USERS_PATH);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain, userService);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    @Test
    public void should_filter_doChain_POST_requests_if_not_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPostWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain, userService);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    @Test
    public void should_filter_doChain_PUT_requests_if_not_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestPutWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain, userService);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    @Test
    public void should_filter_doChain_DELETE_requests_if_not_admin_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestDeleteWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestAuthorizationFilter sut = new RestAuthorizationFilter(userService);

        when(userService.findByUsername(anyString())).thenReturn(NO_ADMIN_USER);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain, userService);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }*/
}
