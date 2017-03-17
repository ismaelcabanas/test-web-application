package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Principal;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class DefaultPrincipal implements Principal {
    private String name;
}
