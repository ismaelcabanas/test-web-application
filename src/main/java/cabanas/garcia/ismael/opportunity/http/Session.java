package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import cabanas.garcia.ismael.opportunity.util.TimeProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDRandomProvider;
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

    private TimeProvider timeProvider;

    public static Session create(final User user) {
        return create(user, -1);
    }

    public static Session create(User user, int timeoutInMilliseconds) {
        return create(user, timeoutInMilliseconds, new UUIDRandomProvider());
    }

    public boolean hasExpired() {
        return timeout != -1 && (DateUtil.now() >= lastAccess + timeout);
    }

    public void resetLastAccess() {
        this.lastAccess = DateUtil.now();
    }

    public Session makeClone(){
        try {
            return (Session) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error cloning session", e);
        }
    }

    public void invalidate() {
        this.sessionId = null;
        this.lastAccess = 0;
        this.user = null;
    }

    public static Session create(User user, int sessionTimeout, UUIDProvider uuidProvider) {
        return Session.builder()
                .sessionId(uuidProvider.generateUUID())
                .user(user)
                .lastAccess(DateUtil.now())
                .timeout(sessionTimeout) // session don't expire
                .build();
    }
}
