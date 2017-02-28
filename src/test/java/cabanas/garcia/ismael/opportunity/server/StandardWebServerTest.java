package cabanas.garcia.ismael.opportunity.server;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class StandardWebServerTest {

    @Test
    public void when_i_start_server_on_a_port_then_the_server_is_up(){
        // given
        int port = 8002;
        WebServer standardWebServer = new StandardWebServer(port);

        // when
        standardWebServer.start();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(true)));

        standardWebServer.stop();
    }

    @Test
    public void when_i_stop_a_started_server_then_the_server_is_down(){
        // given
        int port = 8002;
        WebServer standardWebServer = new StandardWebServer(port);
        standardWebServer.start();

        // when
        standardWebServer.stop();

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }

    @Test
    public void if_the_server_is_not_started_the_server_is_down(){
        // given
        int port = 8002;
        WebServer standardWebServer = new StandardWebServer(port);

        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }
}
