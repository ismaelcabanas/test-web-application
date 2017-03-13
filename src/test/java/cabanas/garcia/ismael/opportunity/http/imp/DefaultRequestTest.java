package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Parameter;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.RequestMethodConstants;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultRequestTest {

    @Test
    public void request_with_path(){
        // when
        Request actual = DefaultRequest.builder().path("/page1").build();

        // then
        assertThat(actual.getPath(), is(equalTo("/page1")));
    }

    @Test
    public void request_with_method(){
        // when
        Request actual = DefaultRequest.builder().method(RequestMethodConstants.POST).build();

        // then
        assertThat(actual.getMethod(), is(equalTo(RequestMethodConstants.POST)));
    }

    @Test
    public void request_with_parameters(){
        // given
        List<Parameter> params = new ArrayList<>();
        Parameter param1 = Parameter.builder().name("param1").value("param1").build();
        params.add(param1);
        Parameter param2 = Parameter.builder().name("param2").value("param2").build();
        params.add(param2);

        // when
        Request actual = DefaultRequest.builder().parameters(params).build();

        // then
        assertThat(actual.getParameter("param1"), is(equalTo(param1.getValue())));
        assertThat(actual.getParameter("param3"), is(nullValue()));
    }

    @Test
    public void request_with_session(){
        // when
        Request actual = DefaultRequest.builder().session(Optional.empty()).build();

        // then
        assertThat(actual.getSession().isPresent(), is(equalTo(false)));
    }
}
