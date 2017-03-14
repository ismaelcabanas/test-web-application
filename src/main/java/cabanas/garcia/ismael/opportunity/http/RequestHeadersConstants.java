package cabanas.garcia.ismael.opportunity.http;

public enum RequestHeadersConstants {

    COOKIE("Cookie");

    private String headerName;


    RequestHeadersConstants(String headerName) {
        this.headerName = headerName;
    }

    public String getName() {
        return headerName;
    }
}
