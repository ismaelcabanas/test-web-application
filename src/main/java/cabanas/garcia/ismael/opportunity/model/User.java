package cabanas.garcia.ismael.opportunity.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class User {
    private String username;
    private String password;
}
