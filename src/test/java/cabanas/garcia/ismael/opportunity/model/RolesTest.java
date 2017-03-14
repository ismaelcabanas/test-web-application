package cabanas.garcia.ismael.opportunity.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RolesTest {

    @Test
    public void contains_roles(){
        // given
        Roles sut = Roles.builder().build();
        sut.add("role1");
        sut.add("role2");
        sut.add("role3");

        Roles roles = Roles.builder().build();
        roles.add("role3");
        roles.add("role4");

        // when
        boolean actual = sut.contains(roles);

        // then
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void not_contains_roles(){
        // given
        Roles sut = Roles.builder().build();
        sut.add("role1");
        sut.add("role2");
        sut.add("role3");

        Roles roles = Roles.builder().build();
        roles.add("role5");
        roles.add("role4");

        // when
        boolean actual = sut.contains(roles);

        // then
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    public void add_role(){
        // given
        Roles sut = Roles.builder().build();

        // when
        sut.add("role1");

        // then
        assertThat(sut.size(), is(equalTo(1)));
    }
}
