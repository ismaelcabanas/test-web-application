package cabanas.garcia.ismael.opportunity.http.imp;


import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeSuccessResourceStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithCredentialsAndRedirectParamStub;
import cabanas.garcia.ismael.opportunity.server.sun.HttpExchangeWithCredentialsStub;
import cabanas.garcia.ismael.opportunity.support.Resource;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class ExchangeRequestTest {

    public static final String PAGE1_PATH = "/page1";

    @Test
    public void should_return_correct_resource(){
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PAGE1_PATH);
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        Resource actual = sut.getResource();

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPath(), is(equalTo(PAGE1_PATH)));
    }

    @Test
    public void should_return_correct_method(){
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PAGE1_PATH);
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        RequestMethodEnum actual = sut.getMethod();

        // then
        assertThat(actual, is(equalTo(RequestMethodEnum.GET)));
    }

    @Test
    public void should_return_parameter_when_exist_parameter(){
        // given
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParamStub("ismael", "", "");
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        String actual = sut.getParameter("username");

        // then
        assertThat(actual, is(equalTo("ismael")));
    }

    @Test
    public void should_return_null_when_parameter_not_exist(){
        // given
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParamStub("ismael", "", "");
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        String actual = sut.getParameter("wrongparameter");

        // then
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void should_return_parameter_from_query_string_when_exist_parameter(){
        // given
        HttpExchange httpExchange = new HttpExchangeWithQueryStringParamStub(PAGE1_PATH, "redirect", "/page2");
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        String actual = sut.getQueryParameter("redirect");

        // then
        assertThat(actual, is(equalTo("/page2")));
    }

    @Test
    public void should_return_parameter_from_query_string_when_not_exists_parameter(){
        // given
        HttpExchange httpExchange = new HttpExchangeWithQueryStringParamStub(PAGE1_PATH, "redirect", "/page2");
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        String actual = sut.getQueryParameter("wrongparameter");

        // then
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void should_return_null_when_get_parameter_from_query_string_and_not_exist_query_string(){
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub(PAGE1_PATH);
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        String actual = sut.getQueryParameter("redirect");

        // then
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void hasRedirect_should_true_when_redirect_parameter_has_value() throws UnsupportedEncodingException {
        // given
        String redirectParam = URLEncoder.encode("/page1", "UTF-8");
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParamStub("ismael", "", redirectParam);
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        boolean actual = sut.hasRedirectParameter();

        // then
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void hasRedirect_should_true_when_redirect_parameter_has_empty_value() throws UnsupportedEncodingException {
        // given
        String redirectParam = URLEncoder.encode("", "UTF-8");
        HttpExchange httpExchange = new HttpExchangeWithCredentialsAndRedirectParamStub("ismael", "", redirectParam);
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        boolean actual = sut.hasRedirectParameter();

        // then
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    public void hasRedirect_should_true_when_redirect_parameter_has_null_value() throws UnsupportedEncodingException {
        // given
        HttpExchange httpExchange = new HttpExchangeWithCredentialsStub();
        ExchangeRequest sut = new ExchangeRequest(httpExchange);

        // when
        boolean actual = sut.hasRedirectParameter();

        // then
        assertThat(actual, is(equalTo(false)));
    }
}
