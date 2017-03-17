package cabanas.garcia.ismael.opportunity.support;

import cabanas.garcia.ismael.opportunity.http.RequestMethodEnum;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class Resource {
    private String path;
    private RequestMethodEnum method;

    public static Resource empty() {
        return Resource.builder().build();
    }

    public static class ResourceBuilder{
        private String path = StringUtils.EMPTY;
        private RequestMethodEnum method = RequestMethodEnum.GET;
    }
}
