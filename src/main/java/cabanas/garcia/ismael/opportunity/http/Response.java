package cabanas.garcia.ismael.opportunity.http;

public interface Response {
    int getStatusCode();

    byte[] getContent();

    boolean isRedirect();
}
