package cabanas.garcia.ismael.opportunity.server;

import cabanas.garcia.ismael.opportunity.mapper.DefaultMapping;
import cabanas.garcia.ismael.opportunity.mapper.Mapping;
import cabanas.garcia.ismael.opportunity.util.ConfigurationBuilder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class ConfigurationBuilderTest {

    @Test
    public void configuration_with_port_and_mapping(){
        // given
        ConfigurationBuilder sut = new ConfigurationBuilder();

        // when
        Configuration actual = sut.controllerMapping(new DefaultMapping()).port(8000);

        // then
        Assert.assertThat(actual.getPort(), Is.is(IsEqual.equalTo(8000)));
        Assert.assertThat(actual.getControllerMapping(), Is.is(IsNull.notNullValue()));
    }
}
