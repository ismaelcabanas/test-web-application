package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import cabanas.garcia.ismael.opportunity.repository.SessionRepository;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithSessionStub;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LogoutControllerTest {

    @Mock
    private SessionRepository sessionRepository;

    @Test
    public void mapping_path(){
        // given
        LogoutController sut = new LogoutController();

        // when
        String actual = sut.getMappingPath();

        // then
        assertThat(actual, is(equalTo(LogoutController.PATH)));
    }

    @Test
    public void method_path(){
        // given
        LogoutController sut = new LogoutController();

        // when
        String actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodConstants.GET)));
    }

    @Test
    public void process_request_should_invalidate_session(){
        // given
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionRepository);
        Request request = createRequestWithValidSession(aSessionId);

        // when
        sut.process(request);

        // then
        assertThat(request.getSession().isPresent(), is(equalTo(true)));
        assertThat(request.getSession().get().hasExpired(), is(equalTo(true)));
    }

    @Test
    public void process_request_should_delete_session(){
        // given
        String aSessionId = "aSessionId";
        LogoutController sut = new LogoutController(sessionRepository);
        Request request = createRequestWithValidSession(aSessionId);

        // when
        sut.process(request);

        // then
        Mockito.verify(sessionRepository).delete(aSessionId);
    }

    private Request createRequestWithValidSession(String aSessionId) {
        HttpExchange httpExchange = new HttpExchangeWithSessionStub("/logout", aSessionId);
        return RequestFactory.create(httpExchange);
    }
}
