package cabanas.garcia.ismael.opportunity.security.permission;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.support.Resource;

public interface PermissionChecker {
    boolean hasPermission(User user, Resource resource);
}
