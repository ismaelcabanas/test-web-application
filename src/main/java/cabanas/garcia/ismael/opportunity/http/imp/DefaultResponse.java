package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Response;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class DefaultResponse implements Response{
    private int statusCode;
    private byte[] content;
    private Object model;

    public static class DefaultResponseBuilder{
        private static final String EMPTY_CONTENT = "";
        private byte[] content = EMPTY_CONTENT.getBytes();
    }
}
