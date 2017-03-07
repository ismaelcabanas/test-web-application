package cabanas.garcia.ismael.opportunity.internal.creation.instance;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void newInstance_of_an_existent_class(){
        // given
        Instantiator sut = new ConstructorInstantiator();

        // when
        ConstructorInstantiator actual = sut.newInstance(ConstructorInstantiator.class);

        // then
        Assert.assertThat(actual, Is.is(IsNot.not(IsNull.nullValue())));
    }

    @Test(expected = InstantiationException.class)
    public void newInstance_of_not_existent_class(){
        // given
        Instantiator sut = new ConstructorInstantiator();

        // when
        TestInstantiatorClass actual = sut.newInstance(TestInstantiatorClass.class);
    }


    private class TestInstantiatorClass {
        public TestInstantiatorClass(){}
    }
}
