package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionStub;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RequestFactoryTest {

    public static final String PATH_PAGE1 = "/page1";

    @Test
    public void create_request_instance_with_path(){
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PATH_PAGE1);
        URI anUri = URI.create(PATH_PAGE1);

        // when
        Request request = RequestFactory.create(httpExchange);

        // then
        assertThat(request.getPath().getPath(), is(equalTo(PATH_PAGE1)));
    }

    @Test
    public void create_request_instance_with_session(){
        // given
        String aSessionId = "mysessionid";
        HttpExchange httpExchange = new HttpExchangeWithSessionStub("/page1", aSessionId);

        // when
        Request request = RequestFactory.create(httpExchange);

        // then
        assertThat(request.getSession().isPresent(), is(true));
        assertThat(request.getSession().get().getSessionId(), is(equalTo(aSessionId)));
    }

    @Test
    public void create_request_instance_without_session(){
        // given
        HttpExchange httpExchange = new HttpExchangeWithSessionStub("/page1");

        // when
        Request request = RequestFactory.create(httpExchange);

        // then
        assertThat(request.getSession().isPresent(), is(false));
    }
}
