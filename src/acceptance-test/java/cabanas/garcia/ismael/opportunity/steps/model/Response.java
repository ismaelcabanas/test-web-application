package cabanas.garcia.ismael.opportunity.steps.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

@Builder
@Getter
@ToString
public class Response {
    private int statusCode;
    private String content;
    private HttpResponse response;

    public Header getHeader(String headerName) {
        return response.getFirstHeader(headerName);
    }
}
