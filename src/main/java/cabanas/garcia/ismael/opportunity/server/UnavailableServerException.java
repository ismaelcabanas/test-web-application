package cabanas.garcia.ismael.opportunity.server;

import java.io.IOException;

/**
 *
 */
public class UnavailableServerException extends Exception {
    public UnavailableServerException(IOException e) {
        super(e);
    }
}
