package cabanas.garcia.ismael.opportunity.mapper;

import cabanas.garcia.ismael.opportunity.controller.*;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DefaultMappingTest {

    @Test
    public void mapping_has_not_controllers(){
        // given
        DefaultMapping sut = new DefaultMapping();

        // when
        boolean actual = sut.hasControllers();

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void mapping_has_controllers(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("/path", Test1Controller.class);

        // when
        boolean actual = sut.hasControllers();

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void mapping_get_controller_for_path_and_get_method(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("/path1", Test1Controller.class);
        sut.addMapping("/path2", Test2Controller.class);
        sut.addMapping("/path1", RequestMethodEnum.POST, Test1PostController.class);

        // when
        Optional<Class<? extends Controller>> controller = sut.getController("/path1");

        // then
        assertThat(controller.isPresent(), is(true));
        assertThat(controller.get().getName(), is(IsEqual.equalTo(Test1Controller.class.getName())));
    }

    @Test
    public void mapping_get_controller_for_path_and_post_method_with_regular_expression(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("^/path1$", Test1Controller.class);
        sut.addMapping("^/path2$", Test2Controller.class);
        sut.addMapping("^/path1/.*$", RequestMethodEnum.POST, Test1PostController.class);

        // when
        Optional<Class<? extends Controller>> controller = sut.getController("/path1/test", RequestMethodEnum.POST);

        // then
        assertThat(controller.isPresent(), is(true));
        assertThat(controller.get().getName(), is(IsEqual.equalTo(Test1PostController.class.getName())));
    }

    @Test
    public void mapping_get_controller_for_path_and_post_method(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("/path1", Test1Controller.class);
        sut.addMapping("/path2", Test2Controller.class);
        sut.addMapping("/path1", RequestMethodEnum.POST, Test1PostController.class);

        // when
        Optional<Class<? extends Controller>> controller = sut.getController("/path1", RequestMethodEnum.POST);

        // then
        assertThat(controller.isPresent(), is(true));
        assertThat(controller.get().getName(), is(IsEqual.equalTo(Test1PostController.class.getName())));
    }

    @Test
    public void mapping_get_empty_controller_for_not_matched_path(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("/path1", Test1Controller.class);
        sut.addMapping("/path2", Test2Controller.class);
        sut.addMapping("/path1", RequestMethodEnum.POST, Test1PostController.class);

        // when
        Optional<Class<? extends Controller>> controller = sut.getController("/path3");

        // then
        assertThat(controller.isPresent(), is(false));
    }

    @Test
    public void mapping_get_empty_controller_for_matched_path_but_unmatched_method(){
        // given
        DefaultMapping sut = new DefaultMapping();
        sut.addMapping("/path1", Test1Controller.class);
        sut.addMapping("/path2", Test2Controller.class);
        sut.addMapping("/path1", RequestMethodEnum.POST, Test1PostController.class);
        sut.addMapping("/path4", RequestMethodEnum.POST, Test1PostController.class);

        // when
        Optional<Class<? extends Controller>> controller = sut.getController("/path4");

        // then
        assertThat(controller.isPresent(), is(false));
    }
}
