package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(exclude={"user"})
public class Session {
    private String sessionId;
    private User user;
}
