package cabanas.garcia.ismael.opportunity.util;

import cabanas.garcia.ismael.opportunity.model.Roles;

import java.util.Arrays;

public class TestUtil {
    public static final String DEFAULT_LIST_ROLES = "Admin, Page1";

    public static Roles getDefaultRoles() {
        String[] splittedRoles = DEFAULT_LIST_ROLES.split(",");

        Roles defaultRoles = Roles.builder().build();

        Arrays.stream(splittedRoles).forEach(role -> defaultRoles.add(role));

        return defaultRoles;
    }
}
