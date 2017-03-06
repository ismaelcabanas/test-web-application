package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Request;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class DefaultRequest implements Request{
    private String path;

    @Override
    public String getPath() {
        return path;
    }
}
