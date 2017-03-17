package cabanas.garcia.ismael.opportunity.support;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {

    @Test
    public void new_resource(){
        // given
        Resource sut = Resource.builder().path("/page1").build();

        // when
        String actual = sut.getPath();

        // then
        Assert.assertThat(actual, Is.is(IsEqual.equalTo("/page1")));
    }


}
