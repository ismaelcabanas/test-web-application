package cabanas.garcia.ismael.opportunity.server.authenticators;

import cabanas.garcia.ismael.opportunity.http.ExtractorHttpExchange;
import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import cabanas.garcia.ismael.opportunity.http.Session;
import cabanas.garcia.ismael.opportunity.permission.PermissionChecker;
import cabanas.garcia.ismael.opportunity.permission.Permissions;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

public class AuthorizationFilter extends Filter{

    public static final String DESCRIPTION = "Authorization filter";

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        ExtractorHttpExchange extractorHttpExchange = new ExtractorHttpExchange(httpExchange);
        String path = extractorHttpExchange.extractPathFrom();

        log.info("Filtering resource {}", path);

        Optional<Session> session = extractorHttpExchange.extractSessionFrom();
        if(!session.isPresent() && session.getUser() != null){
            PermissionChecker permissionChecker = new PermissionChecker(Permissions.getInstance());
            if (!permissionChecker.check(path, session.getUser().gerRoles())) {
                return;
            }
        }
        chain.doFilter(httpExchange);
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
