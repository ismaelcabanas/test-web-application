package cabanas.garcia.ismael.opportunity.controller.web;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestFactory;
import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.http.imp.HttpExchangeWithQueryStringParam;
import cabanas.garcia.ismael.opportunity.support.Resource;
import cabanas.garcia.ismael.opportunity.view.LoginRawView;
import cabanas.garcia.ismael.opportunity.view.View;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class LoginControllerTest {
    @Test
    public void mapping_path(){
        // given
        LoginController sut = new LoginController();

        // when
        Resource actual = sut.getMappingPath();

        // then
        assertThat(actual.getPath(), is(equalTo(LoginController.PATH)));
    }

    @Test
    public void method_path(){
        // given
        LoginController sut = new LoginController();

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.GET)));
    }

    @Test
    public void should_return_login_view_with_redirect_parameter(){
        // given
        LoginController sut = new LoginController();

        HttpExchange httpExchange = new HttpExchangeWithQueryStringParam("/login","redirect","/page1");

        Request request = RequestFactory.create(httpExchange);

        // when
        LoginRawView actual = (LoginRawView) sut.process(request);

        // then
        assertThat(actual.getRedirectPath(), is(equalTo("/page1")));
    }
}
