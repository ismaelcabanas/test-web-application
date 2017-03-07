package cabanas.garcia.ismael.opportunity.scanner;

import cabanas.garcia.ismael.opportunity.controller.Controller;
import cabanas.garcia.ismael.opportunity.scanner.DefaultControllerScanner;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.view.View;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultControllerScannerTest {

    @Test
    public void scanner_controllers(){
        // given
        DefaultControllerScanner sut = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.scanner");

        // when
        List<Class<? extends Controller>> actual = sut.scanner();

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.size(), is(equalTo(2)));
    }

    @Test
    public void scanner_controllers_in_package_that_not_exist_controllers(){
        // given
        DefaultControllerScanner sut = new DefaultControllerScanner("cabanas.garcia.ismael.opportunity.server");

        // when
        List<Class<? extends Controller>> actual = sut.scanner();

        // then
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.size(), is(equalTo(0)));
    }

    private class Test3Controller implements Controller{

        @Override
        public View process(Request request) {
            return null;
        }

        @Override
        public String getMappingPath() {
            return "/test3";
        }
    }

    private class Test4Controller implements Controller{

        @Override
        public View process(Request request) {
            return null;
        }

        @Override
        public String getMappingPath() {
            return "/test4";
        }
    }
}
