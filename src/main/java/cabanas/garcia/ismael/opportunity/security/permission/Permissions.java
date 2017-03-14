package cabanas.garcia.ismael.opportunity.security.permission;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.support.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Permissions {

    private List<Permission> permissionList;

    public Permissions(){
        permissionList = new ArrayList<>();
    }

    public void add(Permission permission) {
        this.permissionList.add(permission);
    }

    public List<Permission> getPermissions() {
        return this.permissionList;
    }

    public boolean check(Roles roles, Resource resource) {
        Optional<Permission> permissionForResource = getPermission(resource);
        if(permissionForResource.isPresent()){
            Roles rolesPermission = permissionForResource.get().getRoles();
            return rolesPermission.contains(roles);
        }
        return false;
    }

    private Optional<Permission> getPermission(Resource resource){
        return this.permissionList.stream().filter(permission -> permission.getResource().equals(resource)).findFirst();
    }
}
