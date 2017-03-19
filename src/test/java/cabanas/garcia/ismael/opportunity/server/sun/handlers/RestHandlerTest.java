package cabanas.garcia.ismael.opportunity.server.sun.handlers;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.security.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.stubs.HttpExchangeRestGetWithPrincipalResourceStub;
import cabanas.garcia.ismael.opportunity.service.UserService;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    private PermissionChecker permissionChecker;

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @Test
    public void should_handle_requests_if_authenticated_user_with_required_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestGetWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService, permissionChecker);

        when(controllers.select(request)).thenReturn(new SuccessController());
        when(userService.findByUsername(Mockito.anyString())).thenReturn(NO_ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(true);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_OK, "Test".getBytes().length);
    }

    @Test
    public void should_handle_requests_if_authenticated_user_without_required_permissions() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestGetWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService, permissionChecker);

        when(controllers.select(request)).thenReturn(new SuccessController());
        when(userService.findByUsername(Mockito.anyString())).thenReturn(NO_ADMIN_USER);
        when(permissionChecker.hasPermission(any(), any())).thenReturn(false);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.handle(httpExchangeSpy);

        // then
        verifyZeroInteractions(controllers);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
    }

    @Test
    public void should_handle_requests_if_not_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("/page1");

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService, permissionChecker);

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.handle(httpExchangeSpy);

        // then
        verifyZeroInteractions(permissionChecker, controllers, userService);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    @Test
    public void should_handle_requests_if_authenticated_user_not_exist_in_repository() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeRestGetWithPrincipalResourceStub(USERS_PATH, USER_1);

        Request request = RequestFactory.create(httpExchange);

        RestHandler sut = new RestHandler(controllers, userService, permissionChecker);

        when(controllers.select(request)).thenReturn(new SuccessController());
        when(userService.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        HttpExchange httpExchangeSpy = spy(httpExchange);

        // when
        sut.handle(httpExchangeSpy);

        // then
        verifyZeroInteractions(controllers, permissionChecker);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
    }

    private class SuccessController extends Controller {
        @Override
        public View process(Request request) {
            return new SuccessView();
        }

        @Override
        public Resource getMappingPath() {
            return null;
        }

        private class SuccessView implements View {
            @Override
            public Response render() {
                return DefaultResponse.builder().statusCode(HttpURLConnection.HTTP_OK).content("Test".getBytes()).build();
            }
        }
    }
}
