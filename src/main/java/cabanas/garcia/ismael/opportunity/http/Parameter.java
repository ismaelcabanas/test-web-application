package cabanas.garcia.ismael.opportunity.http;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Parameter {
    private String name;
    private String value;
}
