package cabanas.garcia.ismael.opportunity.controller;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
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
    public void select_controller_method_based(){
        // given
        Controller controller1 = new Test1Controller("/test1");
        Controller controller2 = new Test2Controller("/test2");
        Controller controller1Post = new Test1PostController("/test1");
        Mapping mapping = new Mapping();
        mapping.addMapping(controller1.getMappingPath(), Test1Controller.class);
        mapping.addMapping(controller2.getMappingPath(), Test2Controller.class);
        mapping.addMapping(controller1Post.getMappingPath(), controller1Post.getMethod(),Test1PostController.class);

        Controllers sut = new Controllers(mapping, instantiator);

        Request request = DefaultRequest.builder()
                .path("/test1")
                .method(RequestMethodConstants.POST)
                .build();

        // when
        Controller actual = sut.select(request);

        // then
        verify(instantiator).newInstance(Test1PostController.class);

        assertThat(actual.getMappingPath(), is(equalTo("/test1")));
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
        Controller actual = sut.select(request);

        // then
        verify(instantiator).newInstance(Test1Controller.class);

        assertThat(actual.getMappingPath(), is(equalTo("/test1")));
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
        Controller actual = sut.select(request);

        // then
        verifyZeroInteractions(instantiator);

        assertThat(actual.getMappingPath(), is(equalTo(new UnknownResourceController().getMappingPath())));
    }

    private class Test1PostController extends Test1Controller{
        public Test1PostController(String path) {
            super(path);
        }

        @Override
        public String getMethod() {
            return RequestMethodConstants.POST;
        }
    }
}
