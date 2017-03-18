package cabanas.garcia.ismael.opportunity.steps.model;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Response {
    private int statusCode;
    private String content;
}
