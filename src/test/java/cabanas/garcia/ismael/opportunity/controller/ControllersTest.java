package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.imp.DefaultRequest;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.Instantiator;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ControllersTest {

    @Mock
    private Instantiator instantiator;

    @Before
    public void setUp(){
        Mockito.when(instantiator.newInstance(Test1Controller.class)).thenReturn(new Test1Controller("/test1"));
        Mockito.when(instantiator.newInstance(Test2Controller.class)).thenReturn(new Test2Controller("/test2"));
    }

    @Test
    public void select_find_controller_from_request(){
        // given
        Controller controller1 = new Test1Controller("/test1");
        Controller controller2 = new Test2Controller("/test2");
        Mapping mapping = new Mapping();
        mapping.addMapping(controller1.getMappingPath(), Test1Controller.class);
        mapping.addMapping(controller2.getMappingPath(), Test2Controller.class);

        Controllers sut = new Controllers(mapping, instantiator);

        Request request = DefaultRequest.builder().path("/test1").build();

        // when
        Optional<Controller> actual = sut.select(request);

        // then
        verify(instantiator).newInstance(Test1Controller.class);

        assertThat(actual.isPresent(), is(equalTo(true)));
        assertThat(actual.get().getMappingPath(), is(equalTo("/test1")));
    }

    @Test
    public void select_not_find_controller_from_request(){
        // given
        Controller controller1 = new Test1Controller("/test1");
        Controller controller2 = new Test2Controller("/test2");
        Mapping mapping = new Mapping();
        mapping.addMapping(controller1.getMappingPath(), Test1Controller.class);
        mapping.addMapping(controller2.getMappingPath(), Test2Controller.class);

        Controllers sut = new Controllers(mapping, instantiator);

        Request request = DefaultRequest.builder().path("/test3").build();

        // when
        Optional<Controller> actual = sut.select(request);

        // then
        verifyZeroInteractions(instantiator);

        assertThat(actual.isPresent(), is(equalTo(false)));
    }
}
