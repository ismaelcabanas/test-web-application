package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.web.UnknownResourceController;
import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.RedirectView;
import cabanas.garcia.ismael.opportunity.view.UnknownResourceRawView;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SunHttpHandlerTest {

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @Mock
    private Controllers controllers;

    private SunHttpHandler sut;

    @Before
    public void setUp(){
        sut = new SunHttpHandler(controllers);
    }

    @Test
    public void select_controller_for_incoming_request() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("page1");
        Request request = RequestFactory.create(httpExchange);

        when(controllers.select(request)).thenReturn(new MyController());

        // when
        sut.handle(httpExchange);

        // then
        verify(controllers).select(requestCaptor.capture());
        assertThat(requestCaptor.getValue(), is(equalTo(request)));
    }

    @Test
    public void handle_success_resource() throws Exception {
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("page1");
        Request request = RequestFactory.create(httpExchange);
        HttpExchange httpExchangeSpy = spy(httpExchange);

        when(controllers.select(request)).thenReturn(new MyController());

        Response expectedResponse = new MyView().render();

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_OK, expectedResponse.getContent().length);

        assertThat(httpExchangeSpy.getResponseBody().toString(), is(equalTo(new String(expectedResponse.getContent()))));
    }

    @Test
    public void handle_unknown_resource() throws Exception {
        // given
        HttpExchange httpExchange = new HttpExchangeNotFoundResourceStub("page1");
        Request request = RequestFactory.create(httpExchange);
        HttpExchange httpExchangeSpy = spy(httpExchange);

        when(controllers.select(request)).thenReturn(new UnknownResourceController());

        Response expectedResponse = new UnknownResourceRawView().render();

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, expectedResponse.getContent().length);

        assertThat(httpExchangeSpy.getResponseBody().toString(), is(equalTo(new String(expectedResponse.getContent()))));
    }

    @Test
    public void handle_session() throws Exception {
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("/page1");
        Request request = RequestFactory.create(httpExchange);

        Session session = Session.create(User.builder().username("ismael").build());

        HttpExchange httpExchangeSpy = spy(httpExchange);

        when(controllers.select(request)).thenReturn(new MyControllerWithSession(session));

        Response expectedResponse = new MyView().render();

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_OK, expectedResponse.getContent().length);
        verifyCookieResponseHeader(httpExchange, session);
    }

    @Test
    public void handle_redirect() throws Exception {
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("/page1");
        Request request = RequestFactory.create(httpExchange);
        HttpExchange httpExchangeSpy = spy(httpExchange);

        when(controllers.select(request)).thenReturn(new MyRedirectController());

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }

    private void verifyCookieResponseHeader(HttpExchange httpExchange, Session session) {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        List<String> setCookieHeader = responseHeaders.get(ResponseHeaderConstants.SET_COOKIE);
        assertThat(setCookieHeader, is(notNullValue()));
        String setCookieHeaderValue = setCookieHeader.get(0);
        assertThat(setCookieHeaderValue, is(equalTo(String.format("%s=%s", Cookie.SESSION_TOKEN, session.getSessionId()))));
    }

    private class MyController extends Controller{
        @Override
        public View process(Request request) {
            return new MyView();
        }

        @Override
        public Resource getMappingPath() {
            return null;
        }
    }

    private class MyView implements View{

        public static final String SUCCESS_RENDERING = "Success rendering";

        @Override
        public Response render() {
            return DefaultResponse.builder()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .content(SUCCESS_RENDERING.getBytes())
                    .build();
        }
    }

    private class MyControllerWithSession extends MyController {
        private final Session session;

        public MyControllerWithSession(Session session) {
            this.session = session;
        }

        @Override
        public View process(Request request) {
            request.setSession(session);
            return super.process(request);
        }

        @Override
        public Resource getMappingPath() {
            return null;
        }
    }

    private class MyRedirectController extends Controller {
        @Override
        public View process(Request request) {
            return new RedirectView("/login");
        }

        @Override
        public Resource getMappingPath() {
            return null;
        }
    }
}
