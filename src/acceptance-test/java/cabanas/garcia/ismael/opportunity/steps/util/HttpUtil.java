package cabanas.garcia.ismael.opportunity.steps.util;

import org.apache.http.*;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.TextUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class HttpUtil {

    public static HttpClient create(){
        return HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
/*
                .setRedirectStrategy(new RedirectStrategy() {
                    @Override
                    public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                        return true;
                    }

                    @Override
                    public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                        URI uri = this.getLocationURI(httpRequest, httpResponse, httpContext);
                        String method = httpRequest.getRequestLine().getMethod();
                        if(method.equalsIgnoreCase("HEAD")) {
                            return new HttpHead(uri);
                        } else if(method.equalsIgnoreCase("GET")) {
                            return new HttpGet(uri);
                        } else {
                            int status = httpResponse.getStatusLine().getStatusCode();
                            return (HttpUriRequest)(status == 307? RequestBuilder.copy(httpRequest).setUri(uri).build():new HttpGet(uri));
                        }
                    }
                    public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        Args.notNull(request, "HTTP request");
                        Args.notNull(response, "HTTP response");
                        Args.notNull(context, "HTTP context");
                        HttpClientContext clientContext = HttpClientContext.adapt(context);
                        Header locationHeader = response.getFirstHeader("location");
                        if(locationHeader == null) {
                            throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
                        } else {
                            String location = locationHeader.getValue();

                            RequestConfig config = clientContext.getRequestConfig();
                            URI uri = this.createLocationURI(location);

                            try {
                                if(!uri.isAbsolute()) {
                                    if(!config.isRelativeRedirectsAllowed()) {
                                        throw new ProtocolException("Relative redirect location \'" + uri + "\' not allowed");
                                    }

                                    HttpHost redirectLocations = clientContext.getTargetHost();
                                    Asserts.notNull(redirectLocations, "Target host");
                                    URI requestURI = new URI(request.getRequestLine().getUri());
                                    URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, redirectLocations, false);
                                    uri = URIUtils.resolve(absoluteRequestURI, uri);
                                }
                            } catch (URISyntaxException var12) {
                                throw new ProtocolException(var12.getMessage(), var12);
                            }

                            RedirectLocations redirectLocations1 = (RedirectLocations)clientContext.getAttribute("http.protocol.redirect-locations");
                            if(redirectLocations1 == null) {
                                redirectLocations1 = new RedirectLocations();
                                context.setAttribute("http.protocol.redirect-locations", redirectLocations1);
                            }

                            if(!config.isCircularRedirectsAllowed() && redirectLocations1.contains(uri)) {
                                throw new CircularRedirectException("Circular redirect to \'" + uri + "\'");
                            } else {
                                redirectLocations1.add(uri);
                                return uri;
                            }
                        }
                    }

                    protected URI createLocationURI(String location) throws ProtocolException {
                        try {
                            URIBuilder ex = new URIBuilder((new URI(location)).normalize());
                            String host = ex.getHost();
                            if(host != null) {
                                ex.setHost(host.toLowerCase(Locale.ROOT));
                            }

                            String path = ex.getPath();
                            if(TextUtils.isEmpty(path)) {
                                ex.setPath("/");
                            }

                            return ex.build();
                        } catch (URISyntaxException var5) {
                            throw new ProtocolException("Invalid redirect URI: " + location, var5);
                        }
                    }
                }).build();
*/
    }


}
