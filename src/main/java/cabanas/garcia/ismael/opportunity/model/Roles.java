package cabanas.garcia.ismael.opportunity.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Roles {

    private List<Role> roleList = new ArrayList<>();

    public void add(String roleName) {
        roleList.add(Role.builder().name(roleName).build());
    }

    public int size() {
        return roleList.size();
    }

    public static class RolesBuilder{
        private List<Role> roleList = new ArrayList<>();
    }
}
