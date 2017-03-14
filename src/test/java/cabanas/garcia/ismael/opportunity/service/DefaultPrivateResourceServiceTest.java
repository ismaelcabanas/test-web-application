package cabanas.garcia.ismael.opportunity.service;

import cabanas.garcia.ismael.opportunity.support.PrivateResources;
import cabanas.garcia.ismael.opportunity.support.Resource;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DefaultPrivateResourceServiceTest {

    @Test
    public void has_private_resource(){
        // given
        Resource resource = Resource.builder().path("/page1").build();

        PrivateResources privateResources = new PrivateResources();
        privateResources.add(resource);

        PrivateResourcesService sut = new DefaultPrivateResourceService(privateResources);

        // when
        boolean actual = sut.hasResource(resource);

        // then
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void not_has_private_resource(){
        // given
        Resource resource = Resource.builder().path("/page1").build();

        PrivateResources privateResources = new PrivateResources();

        PrivateResourcesService sut = new DefaultPrivateResourceService(privateResources);

        // when
        boolean actual = sut.hasResource(resource);

        // then
        assertThat(actual, is(equalTo(false)));
    }
}
