package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@ToString
@EqualsAndHashCode(exclude={"user", "lastAccess", "timeout"})
public class Session implements Cloneable{
    private String sessionId;
    private User user;
    private long lastAccess;
    // expire session in milliseconds
    private int timeout;

    public static Session create(final User user) {
        return create(user, -1);
    }

    public static Session create(User user, int timeoutInMilliseconds) {
        return Session.builder()
                .sessionId(UUID.randomUUID().toString())
                .user(user)
                .lastAccess(DateUtil.now())
                .timeout(timeoutInMilliseconds) // session don't expire
                .build();
    }

    public boolean hasExpired() {
        return (timeout != -1 && !(DateUtil.now() < lastAccess + timeout));
    }

    public void resetLastAccess() {
        this.lastAccess = DateUtil.now();
    }

    public Session makeClone(){
        try {
            return (Session) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
