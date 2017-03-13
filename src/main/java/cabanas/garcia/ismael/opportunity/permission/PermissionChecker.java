package cabanas.garcia.ismael.opportunity.permission;

import java.util.Optional;

public class PermissionChecker {
    Permissions permissions;

    public PermissionChecker(Permissions permissions){
        this.permissions = permissions;
    }
    public boolean check (String resource, String[] roles){
        Optional<Permission> permission = permissions.getPermissions(resource);
        return false; //!Collections.disjoint(permission.getRoles(), roles);
    }
}