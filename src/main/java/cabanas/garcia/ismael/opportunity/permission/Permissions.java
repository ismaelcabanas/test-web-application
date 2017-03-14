package cabanas.garcia.ismael.opportunity.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Permissions {

    private List<Permission> permissionList;

    public Permissions(){
        permissionList = new ArrayList<>();
    }

    public Optional<Permission> getPermissions(String resource){
        /*String[] roles =  permissions.entrySet().stream()
                .filter(map -> resource.equals(map.getName()))
                .map(map -> map.getName())
                .collect(Collectors.joining());

        return Optional.of(Permission.builder()
                .resource(resource)
                .roles(roles)
                .build());*/
        return Optional.empty();
    }

    public void add(Permission permission) {
        this.permissionList.add(permission);
    }

    public List<Permission> getPermissions() {
        return this.permissionList;
    }
}
