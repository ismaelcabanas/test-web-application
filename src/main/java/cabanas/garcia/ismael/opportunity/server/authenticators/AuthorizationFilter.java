package cabanas.garcia.ismael.opportunity.server.authenticators;

import cabanas.garcia.ismael.opportunity.http.ExtractorHttpExchange;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends Filter{

    public static final String DESCRIPTION = "Authorization filter";

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);
        String path = extractorHttpExchange.extractPathFrom();

        log.info("Filtering resource {}", path);

      /*  Optional<Session> session = extractorHttpExchange.extractSessionFrom();
        if(!session.isPresent() && session.getUser() != null){
            PermissionChecker permissionChecker = new PermissionChecker(Permissions.getInstance());
            if (!permissionChecker.check(path, session.getUser().gerRoles())) {
                return;
            }
        }*/
        chain.doFilter(httpExchange);
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
