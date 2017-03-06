package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultRequest;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class ControllersTest {

    @Test
    public void select_find_controller_from_request(){
        // given
        Controller controller1 = new Test1Controller("/test1");
        Controller controller2 = new Test2Controller("/test2");
        List<Controller> controllers = Arrays.asList(controller1, controller2);
        Controllers sut = new Controllers(controllers);

        Request request = DefaultRequest.builder().path("/test1").build();

        // when
        Controller actual = sut.select(request);

        // then
        assertThat(actual, is(notNullValue()));
    }

    @Test
    public void select_not_find_controller_from_request(){
        // given
        Controller controller1 = new Test1Controller("/test1");
        Controller controller2 = new Test2Controller("/test2");
        List<Controller> controllers = Arrays.asList(controller1, controller2);
        Controllers sut = new Controllers(controllers);

        Request request = DefaultRequest.builder().path("/test3").build();

        // when
        Controller actual = sut.select(request);

        // then
        assertThat(actual, is(nullValue()));
    }
}
