package cabanas.garcia.ismael.opportunity.support;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class PrivateResourcesTest {

    @Test
    public void add_resource(){
        // given
        Resource resource1 = Resource.builder().path("/page1").build();

        PrivateResources sut = new PrivateResources();

        // when
        sut.add(resource1);

        // then
        Assert.assertThat(sut.hasResource(resource1), Is.is(IsEqual.equalTo(true)));
    }

    @Test
    public void is_resource_private(){
        // given
        Resource resource1 = Resource.builder().path("/page1").build();
        Resource resource2 = Resource.builder().path("/page2").build();

        PrivateResources sut = new PrivateResources();
        sut.add(resource1);
        sut.add(resource2);

        // when
        boolean actual = sut.hasResource(resource1);

        // then
        Assert.assertThat(actual, Is.is(IsEqual.equalTo(true)));
    }

    @Test
    public void is_not_resource_private(){
        // given
        Resource resource1 = Resource.builder().path("/page1").build();
        Resource resource2 = Resource.builder().path("/page2").build();

        PrivateResources sut = new PrivateResources();
        sut.add(resource1);


        // when
        boolean actual = sut.hasResource(resource2);

        // then
        Assert.assertThat(actual, Is.is(IsEqual.equalTo(false)));
    }
}
