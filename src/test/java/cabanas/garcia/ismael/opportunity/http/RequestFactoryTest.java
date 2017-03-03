package cabanas.garcia.ismael.opportunity.http;

import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestFactoryTest {

    public static final String PATH_PAGE1 = "/page1";

    @Mock
    private HttpExchange httpExchange;

    @Test
    public void create_request_instance_with_path(){
        // given
        URI anUri = URI.create(PATH_PAGE1);
        when(httpExchange.getRequestURI()).thenReturn(anUri);

        // when
        Request request = RequestFactory.create(httpExchange);

        // then
        assertThat(request.getPath(), is(equalTo(PATH_PAGE1)));
    }
}
