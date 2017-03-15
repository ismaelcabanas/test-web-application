package cabanas.garcia.ismael.opportunity.support;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class Resource {
    private String path;

    public static Resource empty() {
        return Resource.builder().path("").build();
    }
}
