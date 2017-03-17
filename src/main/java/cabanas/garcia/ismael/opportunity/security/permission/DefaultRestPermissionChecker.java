package cabanas.garcia.ismael.opportunity.security.permission;

import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.support.Resource;

public class DefaultRestPermissionChecker implements PermissionChecker {
    @Override
    public boolean hasPermission(User user, Resource resource) {
        return user.isAdmin() || resource.getMethod().equals(RequestMethodEnum.GET);
    }
}
