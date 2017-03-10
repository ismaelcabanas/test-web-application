package cabanas.garcia.ismael.opportunity.http;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Parameter {
    private String name;
    private String value;
}
