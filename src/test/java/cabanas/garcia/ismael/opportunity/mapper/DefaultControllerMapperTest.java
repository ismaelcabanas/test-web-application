package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.AbstractController;
import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.internal.creation.instance.Instantiator;
import cabanas.garcia.ismael.opportunity.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultControllerMapperTest {

    public static final String TEST3_PATH = "/test3";
    public static final String TEST4_PATH = "/test4";

    @Mock
    private Instantiator instantiator;

    @Test
    public void mapping_controllers(){
        // given
        ControllerMapper sut = new DefaultControllerMapper(instantiator);
        List<Class<? extends Controller>> controllers = new ArrayList<>();
        controllers.add(Test3Controller.class);
        controllers.add(Test4Controller.class);

        when(instantiator.newInstance(Test3Controller.class)).thenReturn(new Test3Controller());
        when(instantiator.newInstance(Test4Controller.class)).thenReturn(new Test4Controller());

        // when
        Mapping actual = sut.mapping(controllers);

        // then
        verify(instantiator, times(2)).newInstance(any());

        assertThat(actual.hasControllers(), is(equalTo(true)));
        assertThat(actual.getController(TEST3_PATH).get(), is(equalTo(Test3Controller.class)));
        assertThat(actual.getController(TEST4_PATH).get(), is(equalTo(Test4Controller.class)));
    }

    class Test3Controller extends AbstractController {
        @Override
        public View process(Request request) {
            return null;
        }

        @Override
        public String getMappingPath() {
            return TEST3_PATH;
        }
    }

    class Test4Controller extends AbstractController{
        @Override
        public View process(Request request) {
            return null;
        }

        @Override
        public String getMappingPath() {
            return TEST4_PATH;
        }
    }
}
