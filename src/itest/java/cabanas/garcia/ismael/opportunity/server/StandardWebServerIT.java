package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.internal.creation.instance.ConstructorInstantiator;
import cabanas.garcia.ismael.opportunity.mapper.ControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.DefaultControllerMapper;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.scanner.ControllerScanner;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.server.sun.SunHttpServer;
import cabanas.garcia.ismael.opportunity.util.ConfigurationBuilder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StandardWebServerIT {

    private int port;
    private StandardWebServer standardWebServer;

    @Before
    public void setUp() throws Exception {
        port = 8003;
        ControllerScanner controllerScanner = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.controller");
        ControllerMapper controllerMapper = new DefaultControllerMapper(new ConstructorInstantiator());

        standardWebServer = new StandardWebServer(port, controllerScanner, controllerMapper, new SunHttpServer());
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

    /*@Test(expected = UnavailableServerException.class)
    public void server_start_on_port_in_use() throws Exception{
        // given
        standardWebServer.start();

        // when
        standardWebServer.start();
    }*/

    @Test
    public void if_the_server_is_not_started_the_server_is_down(){
        // then
        Assert.assertThat(standardWebServer.isRunning(), Is.is(IsEqual.equalTo(false)));
    }
}
