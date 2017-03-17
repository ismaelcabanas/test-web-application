package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.util.TimeProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode(exclude={"user", "lastAccess", "timeout"})
public class Session {
    private static final int SESSION_NEVER_EXPIRE = -1;
    public static final int SESSION_ALWAYS_EXPIRED = 0;
    private String sessionId;
    private User user;
    private long lastAccess;

    /*
        Expire session in seconds.
        -1 .- the session never expire
        0 .- the session es invalidate
        >0 .- expire session time
     */
    private int timeout;

    private UUIDProvider uuidProvider;

    private TimeProvider timeProvider;

    public Session(User user, int timeout, UUIDProvider uuidProvider, TimeProvider timeProvider) {
        this.user = user;
        this.timeout = timeout;
        this.uuidProvider = uuidProvider;
        this.timeProvider = timeProvider;
        this.sessionId = uuidProvider.generateUUID();
        this.lastAccess = timeProvider.now();
    }

    public boolean hasExpired() {
        return timeout != SESSION_NEVER_EXPIRE && (timeProvider.now() >= lastAccess + (timeout * 1000));
    }

    public void resetLastAccess() {
        this.lastAccess = timeProvider.now();
    }

    public void invalidate() {
        this.sessionId = StringUtils.EMPTY;
        this.lastAccess = 0;
        this.timeout = SESSION_ALWAYS_EXPIRED;
        this.user = null;
    }

    public String getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public int getTimeout() {
        return timeout;
    }

    public long getLastAccess() {
        return lastAccess;
    }
}
