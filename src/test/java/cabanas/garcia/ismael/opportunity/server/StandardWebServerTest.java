package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandardWebServerTest {

    @Mock
    WebServer theWebServer;

    @Mock
    ControllerScanner controllerScanner;

    @Mock
    ControllerMapper controllerMapper;

    @Captor
    ArgumentCaptor<Configuration> configurationCaptor;

    public static final int PORT = 8000;

    private StandardWebServer sut;

    @Before
    public void setUp(){
        sut = new StandardWebServer(PORT, controllerScanner, controllerMapper, theWebServer);
        when(theWebServer.isRunning()).thenReturn(true);
    }

    @Test
    public void start_server() throws Exception{
        // when
        sut.start();

        // then
        verify(theWebServer).addConfiguration(configurationCaptor.capture());
        verify(theWebServer).start();

        assertThat(configurationCaptor.getValue().getPort(), is(equalTo(PORT)));
    }

    @Test
    public void when_start_server_controllers_are_scanned() throws Exception{
        // when
        sut.start();

        // then
        verify(controllerScanner).scanner();
    }

    @Test
    public void when_start_server_then_is_running() throws Exception{
        // when
        sut.start();

        // then
        assertTrue(sut.isRunning());
    }

    @Test
    public void when_start_server_controllers_are_mapped() throws Exception{
        // when
        sut.start();

        // then
        verify(controllerMapper).mapping(Mockito.any());
    }

}
