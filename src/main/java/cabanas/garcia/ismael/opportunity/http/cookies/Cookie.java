package cabanas.garcia.ismael.opportunity.http.cookies;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Cookie {
    public static final String SESSION_TOKEN = "sessionToken";

    private String name;
    private String value;

    @Override
    public String toString(){
        return name + "=" + value;
    }
}
