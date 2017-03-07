package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.controller.UnknownResourceController;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.Response;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultResponse;
import cabanas.garcia.ismael.opportunity.view.UnknownResourceRawView;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

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
        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

        when(controllers.select(request)).thenReturn(new UnknownResourceController());

        Response expectedResponse = new UnknownResourceRawView().render();

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, expectedResponse.getContent().length);

        assertThat(httpExchangeSpy.getResponseBody().toString(), is(equalTo(new String(expectedResponse.getContent()))));
    }


    private class MyController implements Controller{
        @Override
        public View process(Request request) {
            return new MyView();
        }

        @Override
        public String getMappingPath() {
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
}
