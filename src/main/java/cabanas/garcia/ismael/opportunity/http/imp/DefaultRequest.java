package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.Parameter;
import cabanas.garcia.ismael.opportunity.http.Request;
import cabanas.garcia.ismael.opportunity.http.Session;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Builder
@EqualsAndHashCode
@ToString
@Getter
public class DefaultRequest implements Request{
    private String path;
    private Optional<Session> session;
    private String method;
    private List<Parameter> parameters;

    @Override
    public String getParameter(String paramName) {
        Optional<Parameter> parameter = parameters.stream().filter(param -> param.getName().equals(paramName)).findFirst();
        if(parameter.isPresent())
            return parameter.get().getValue();
        return null;
    }
}
