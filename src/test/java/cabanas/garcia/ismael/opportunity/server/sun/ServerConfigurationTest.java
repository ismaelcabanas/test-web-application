package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.model.Role;
import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.permission.Permission;
import cabanas.garcia.ismael.opportunity.permission.Permissions;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ServerConfigurationTest {

    private static final Roles ROLES_PAGE1 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page1").build())).build();
    private static final Roles ROLES_ADMIN_PAGE2 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page2").build(), Role.builder().name("Admin").build())).build();
    private static final Roles ROLES_PAGE3 = Roles.builder().roleList(Arrays.asList(Role.builder().name("Page3").build())).build();

    @Test
    public void add_configuration(){
        // given
        ServerConfiguration sut = ServerConfiguration.getInstance();

        // when
        sut.add("property1", "value1");

        // then
        String actual = (String) sut.get("property1");
        assertThat(actual, is(equalTo("value1")));
    }

    @Test
    public void add_permissions(){
        // given
        ServerConfiguration sut = ServerConfiguration.getInstance();

        Permission permission1 = Permission.builder().resource("/page1").roles(ROLES_PAGE1).build();
        Permission permission2 = Permission.builder().resource("/page2").roles(ROLES_ADMIN_PAGE2).build();
        Permission permission3 = Permission.builder().resource("/page3").roles(ROLES_PAGE3).build();
        sut.getPermissions().add(permission1);
        sut.getPermissions().add(permission2);
        sut.getPermissions().add(permission3);

        // when
        Permissions actual = sut.getPermissions();

        // then
        assertThat(actual.getPermissions(), Matchers.containsInAnyOrder(permission1, permission2, permission3));
    }
}
