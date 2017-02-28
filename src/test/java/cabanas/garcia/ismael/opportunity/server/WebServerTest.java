package cabanas.garcia.ismael.opportunity.server;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class WebServerTest {

    @Test
    public void when_i_start_server_on_a_port_then_the_server_is_up(){
        // given
        int port = 8000;
        WebServer webServer = new WebServer(port);

        // when
        webServer.start();

        // then
        Assert.assertThat(webServer.isUp(), Is.is(IsEqual.equalTo(true)));
    }

    @Test
    public void when_i_stop_a_started_server_then_the_server_is_down(){
        // given
        int port = 8000;
        WebServer webServer = new WebServer(port);
        webServer.start();

        // when
        webServer.stop();

        // then
        Assert.assertThat(webServer.isUp(), Is.is(IsEqual.equalTo(false)));
    }

    @Test
    public void if_the_server_is_not_started_the_server_is_down(){
        // given
        int port = 8000;
        WebServer webServer = new WebServer(port);

        // then
        Assert.assertThat(webServer.isUp(), Is.is(IsEqual.equalTo(false)));
    }
}
