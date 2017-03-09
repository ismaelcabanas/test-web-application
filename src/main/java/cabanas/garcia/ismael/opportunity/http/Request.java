package cabanas.garcia.ismael.opportunity.http;

import java.util.Optional;

public interface Request {
    String getPath();

    Optional<Session> getSession();

    String getMethod();
}
