package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class DefaultRequest implements Request{
    private String path;
    private Optional<Session> session;
}
