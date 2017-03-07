package cabanas.garcia.ismael.opportunity.server;

import java.io.IOException;

public class UnavailableServerException extends RuntimeException {
    public UnavailableServerException(IOException e) {
        super(e);
    }
}
