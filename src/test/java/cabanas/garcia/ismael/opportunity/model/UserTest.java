package cabanas.garcia.ismael.opportunity.model;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UserTest {

    private static final Roles ROLES_ADMIN = Roles.builder().roleList(Arrays.asList(Role.builder().name(RoleEnum.ADMIN.getRoleName()).build())).build();
    private static final Roles ROLES_PAGE1 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page1").build())).build();

    @Test
    public void is_user_admin(){
        // given
        User userAdmin = User.builder().username("admin").password("admin").roles(ROLES_ADMIN).build();

        // when
        boolean actual = userAdmin.isAdmin();

        // then
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void not_is_user_admin(){
        // given
        User userAdmin = User.builder().username("admin").password("admin").roles(ROLES_PAGE1).build();

        // when
        boolean actual = userAdmin.isAdmin();

        // then
        assertThat(actual, is(equalTo(false)));
    }
}
