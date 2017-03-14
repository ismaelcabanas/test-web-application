package cabanas.garcia.ismael.opportunity.http;

public enum RequestHeadersEnum {

    COOKIE("Cookie");

    private String headerName;


    RequestHeadersEnum(String headerName) {
        this.headerName = headerName;
    }

    public String getName() {
        return headerName;
    }
}
