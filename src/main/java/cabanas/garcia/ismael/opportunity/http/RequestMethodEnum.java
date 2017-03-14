package cabanas.garcia.ismael.opportunity.http;

public enum RequestMethodEnum {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;


    RequestMethodEnum(String method) {
        this.method = method;
    }


    public String getName() {
        return method;
    }
}
