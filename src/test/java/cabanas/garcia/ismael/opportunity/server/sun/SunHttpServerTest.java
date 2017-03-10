package cabanas.garcia.ismael.opportunity.server.sun;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

public class SunHttpServerTest {

    @Test
    public void create_server_instance(){
        // when
        SunHttpServer httpServer = SunHttpServer.getInstance();

        // then
        Assert.assertThat(httpServer.isRunning(), Is.is(false));
    }

}
