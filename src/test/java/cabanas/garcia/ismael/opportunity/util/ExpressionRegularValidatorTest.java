package cabanas.garcia.ismael.opportunity.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExpressionRegularValidatorTest {

    @Test
    public void should_be_true_when_stringToCheck_matcher_pattern(){
        //given
        String pattern1 = "/page1";
        String stringToCheck1 = "/page1";

        String pattern2= "^/users/.*$";
        String stringToCheck2 = "/users/ismael";

        //when
        boolean resultMatcher1 = ExpressionRegularValidator.isValidate(pattern1,stringToCheck1);
        boolean resultMatcher2 = ExpressionRegularValidator.isValidate(pattern2,stringToCheck2);

        //then
        assertThat(resultMatcher1 , is(equalTo(true)));
        assertThat(resultMatcher2 , is(equalTo(true)));
    }

    @Test
    public void should_be_false_when_stringToCheck_not_matcher_pattern(){

        //given
        String pattern= "^/users/.*$";
        String stringToCheck = "/users1/ismael";

        //when
        boolean resultMatcher = ExpressionRegularValidator.isValidate(pattern,stringToCheck);

        //then
        assertThat(resultMatcher , is(equalTo(false)));
    }

}
