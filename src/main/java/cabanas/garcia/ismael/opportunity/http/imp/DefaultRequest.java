package cabanas.garcia.ismael.opportunity.http.imp;

import cabanas.garcia.ismael.opportunity.http.*;
import cabanas.garcia.ismael.opportunity.http.cookies.Cookie;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.support.Resource;
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
    private Resource resource;
    private Optional<Session> session;
    private RequestMethodEnum method;
    private List<Parameter> parameters;
    private Optional<Cookie> sessionCookie;

    @Override
    public String getParameter(String paramName) {
        Optional<Parameter> parameter = parameters.stream().filter(param -> param.getName().equals(paramName)).findFirst();
        if(parameter.isPresent())
            return parameter.get().getValue();
        return null;
    }

    @Override
    public void setSession(Session session) {
        this.session = Optional.ofNullable(session);
    }

    @Override
    public boolean hasRedirectParameter() {
        return getParameter("redirect") != null;
    }

    @Override
    public String getQueryParameter(String paramName) {
        return null;
    }

    @Override
    public Principal getPrincipal() {
        return null;
    }
}
