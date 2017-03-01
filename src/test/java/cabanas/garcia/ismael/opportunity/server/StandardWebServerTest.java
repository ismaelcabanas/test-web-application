package cabanas.garcia.ismael.opportunity.server;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardWebServerTest {

    private int port;
    private WebServer standardWebServer;

    @Before
    public void setUp() throws Exception {
        port = 8003;
        standardWebServer = new StandardWebServer(port);
    }

    @After
    public void tearDown(){
        if(standardWebServer.isRunning()){
            standardWebServer.stop();
        }
    }

    @Test
    public void when_i_start_server_on_a_port_then_the_server_is_up(){
        // when
        standardWebServer.start();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(true)));
    }

    @Test
    public void when_i_stop_a_started_server_then_the_server_is_down(){
        // given
        standardWebServer.start();

        // when
        standardWebServer.stop();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }

    @Test
    public void if_the_server_is_not_started_the_server_is_down(){
        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }
}
