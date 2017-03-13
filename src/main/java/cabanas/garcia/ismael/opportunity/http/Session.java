package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@ToString
@EqualsAndHashCode(exclude={"user"})
public class Session {
    private String sessionId;
    private User user;

    public static Session create(final User user) {
        return Session.builder()
                .sessionId(UUID.randomUUID().toString())
                .user(user)
                .build();
    }
}
