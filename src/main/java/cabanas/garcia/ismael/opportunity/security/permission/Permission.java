package cabanas.garcia.ismael.opportunity.security.permission;

import cabanas.garcia.ismael.opportunity.model.Roles;
import cabanas.garcia.ismael.opportunity.support.Resource;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Permission {

    private Resource resource;

    private Roles roles;

}
