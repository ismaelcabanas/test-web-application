package cabanas.garcia.ismael.opportunity.security.permission;


import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.support.Resource;

public class DefaultPermissionChecker implements PermissionChecker {
    private final Permissions permissions;

    public DefaultPermissionChecker(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(final User user, final Resource resource) {
        return user.isAdmin() || permissions.check(user.getRoles(), resource);
    }
}
