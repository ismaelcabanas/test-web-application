package cabanas.garcia.ismael.opportunity.server;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardWebServerIT {

    private int port;
    private WebServer standardWebServer;

    @Before
    public void setUp() throws Exception {
        port = 8003;
        standardWebServer = new SunHttpServer(port);
    }

    @After
    public void tearDown(){
        if(standardWebServer.isRunning()){
            standardWebServer.stop();
        }
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

    @Test(expected = UnavailableServerException.class)
    public void server_start_on_port_in_use() throws Exception{
        // given
        standardWebServer.start();

        // when
        standardWebServer.start();
    }

    @Test
    public void if_the_server_is_not_started_the_server_is_down(){
        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }
}
