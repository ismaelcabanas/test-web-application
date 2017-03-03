package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.service.ControllerRegistryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandardWebServerTest {

    @Mock
    WebServer theWebServer;

    @Mock
    ControllerRegistryService controllerRegistryService;

    public static final int PORT = 8000;

    private StandardWebServer sut;

    @Before
    public void setUp(){
        sut = new StandardWebServer(PORT, controllerRegistryService, theWebServer);
        when(theWebServer.isRunning()).thenReturn(true);
    }

    @Test
    public void start_server() throws Exception{
        // when
        sut.start();

        // then
        verify(theWebServer).start();
    }

    @Test
    public void when_start_server_controllers_are_registered() throws Exception{
        // when
        sut.start();

        // then
        verify(controllerRegistryService).register();
    }

    @Test
    public void start_server_then_is_running() throws Exception{
        // when
        sut.start();

        // then
        assertTrue(sut.isRunning());
    }
}
