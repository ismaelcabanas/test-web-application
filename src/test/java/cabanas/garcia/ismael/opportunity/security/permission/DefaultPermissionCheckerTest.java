package cabanas.garcia.ismael.opportunity.security.permission;

import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.RoleEnum;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.support.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DefaultPermissionCheckerTest {

    private static final Roles ROLES_PAGE1_PAGE2 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page2").build(), Role.builder().name("Page1").build())).build();
    private static final Roles ROLES_PAGE1 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page1").build())).build();
    private static final Roles ROLES_PAGE3 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page3").build())).build();
    private static final Roles ROLES_ADMIN = Roles.builder().roleList(Arrays.asList(Role.builder().name(RoleEnum.ADMIN.getRoleName()).build())).build();

    private Permissions permissions;

    private static final Resource RESOURCE_PAGE_1 = Resource.builder().path("/page1").build();
    private static final Resource RESOURCE_PAGE_2 = Resource.builder().path("/page2").build();

    @Before
    public void setUp(){
        permissions = new Permissions();
        permissions.add(Permission.builder().roles(ROLES_PAGE1).resource(RESOURCE_PAGE_1).build());
    }

    @Test
    public void has_permission(){
        // given

        User user = User.builder().username("user1").roles(ROLES_PAGE1_PAGE2).build();

        PermissionChecker sut = new DefaultPermissionChecker(permissions);

        // when
        boolean actual = sut.hasPermission(user, RESOURCE_PAGE_1);

        // then
        assertThat(actual, is(equalTo(true)));
    }

    @Test
    public void has_permission_on_non_existen_resource(){
        // given

        User user = User.builder().username("user1").roles(ROLES_PAGE1_PAGE2).build();

        PermissionChecker sut = new DefaultPermissionChecker(permissions);

        // when
        boolean actual = sut.hasPermission(user, RESOURCE_PAGE_2);

        // then
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    public void not_has_permission(){
        // given
        User user = User.builder().username("user1").roles(ROLES_PAGE3).build();

        PermissionChecker sut = new DefaultPermissionChecker(permissions);

        // when
        boolean actual = sut.hasPermission(user, RESOURCE_PAGE_1);

        // then
        assertThat(actual, is(equalTo(false)));
    }

    @Test
    public void has_permission_if_user_is_admin(){
        // given
        User adminUser = User.builder().username("user1").roles(ROLES_ADMIN).build();

        PermissionChecker sut = new DefaultPermissionChecker(permissions);

        // when
        boolean actual = sut.hasPermission(adminUser, RESOURCE_PAGE_1);

        // then
        assertThat(actual, is(equalTo(true)));
    }
}
