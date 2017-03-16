package cabanas.garcia.ismael.opportunity.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class User implements Cloneable{
    private String username;
    private String password;
    private Roles roles;

    public boolean isAdmin() {
        Optional<Role> roleAdmin = roles.roleList.stream().filter(role -> role.equals(Role.ADMIN)).findFirst();
        return roleAdmin.isPresent();
    }
}
