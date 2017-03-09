package cabanas.garcia.ismael.opportunity.http;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Parameter {
    private String name;
    private String value;
}
