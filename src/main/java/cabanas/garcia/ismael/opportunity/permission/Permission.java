package cabanas.garcia.ismael.opportunity.permission;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Permission {

    private String resource;

    private String[] roles;

}
