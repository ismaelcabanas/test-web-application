package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardWebServerIT {

    private int port;
    private SunHttpServer standardWebServer;

    @Before
    public void setUp() throws Exception {
        port = 8003;

        standardWebServer = new SunHttpServer(port);
    }

    @After
    public void tearDown(){
        standardWebServer.stop();
    }

    @Test
    public void when_i_start_server_on_a_port_then_the_server_is_up() throws Exception{
        // when
        standardWebServer.start();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(true)));
    }

    @Test
    public void when_i_stop_a_started_server_then_the_server_is_down() throws Exception{
        // given
        standardWebServer.start();

        // when
        standardWebServer.stop();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }

}
