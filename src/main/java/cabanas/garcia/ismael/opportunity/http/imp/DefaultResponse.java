package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Response;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class DefaultResponse implements Response{
    private int statusCode;
    private byte[] content;
    private Object model;
    private String redirectPath;

    public static class DefaultResponseBuilder{
        private byte[] content = StringUtils.EMPTY.getBytes();
    }

    @Override
    public boolean isRedirect() {
        return statusCode >= 300 && statusCode <= 305 && statusCode != 304;
    }

}
