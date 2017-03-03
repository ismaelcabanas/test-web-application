package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.controller.Controllers;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SunHttpHandlerTest {

    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @Mock
    private Controllers controllers;

    @Mock
    private Controller page1Controller;

    @Mock
    private View page1View;

    private SunHttpHandler sut;

    private HttpExchange httpExchange;

    private Request request;

    @Before
    public void setUp(){
        sut = new SunHttpHandler(controllers);
        httpExchange = new PageSuccessRequestStub("page1");
        request = RequestFactory.create(httpExchange);
    }

    @Test
    public void select_controller_for_incoming_request() throws Exception{
        // when
        sut.handle(httpExchange);

        // then
        verify(controllers).select(requestCaptor.capture());
        assertThat(requestCaptor.getValue(), is(equalTo(request)));
    }

    @Test
    public void controller_handle_incoming_request() throws Exception{
        // given
        when(controllers.select(request)).thenReturn(page1Controller);

        // when
        sut.handle(httpExchange);

        // then
        verify(page1Controller).process(requestCaptor.capture());
        assertThat(requestCaptor.getValue(), is(equalTo(request)));
    }

    @Test
    public void handle_process_response() throws Exception{
        // given
        HttpExchange httpExchangeSpy = Mockito.spy(HttpExchange.class);
        when(controllers.select(request)).thenReturn(page1Controller);
        when(page1View.render()).thenReturn("Hello page1");

        // when
        sut.handle(httpExchangeSpy);

        // then
        verify(httpExchangeSpy).getResponseBody();
        assertThat(httpExchangeSpy.getResponseCode(), is(equalTo(200)));
        assertThat(httpExchangeSpy.getResponseBody().toString(), is(equalTo("Hello page1")));
    }

}
