package cabanas.garcia.ismael.opportunity.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@EqualsAndHashCode
@ToString
public class Roles {

    List<Role> roleList = new ArrayList<>();

    public void add(String roleName) {
        roleList.add(Role.builder().name(roleName).build());
    }

    public int size() {
        return roleList.size();
    }

    public boolean contains(Roles roles) {
        for(Role role : roleList){
            for(Role role2 : roles.roleList){
                if(role.equals(role2))
                    return true;
            }
        }
        return false;
    }

    public static class RolesBuilder{
        private List<Role> roleList = new ArrayList<>();
    }
}
