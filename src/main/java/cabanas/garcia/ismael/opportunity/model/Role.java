package cabanas.garcia.ismael.opportunity.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Role {
    public static final Role ADMIN = Role.builder().name(RoleEnum.ADMIN.getRoleName()).build();

    private String name;
}
