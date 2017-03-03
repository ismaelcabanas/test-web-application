package cabanas.garcia.ismael.opportunity.server.sun;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class PageSuccessRequestStub extends HttpExchange {

    private String path;

    public PageSuccessRequestStub(String path) {
        this.path = path;
    }

    @Override
    public Headers getRequestHeaders() {
        return null;
    }

    @Override
    public Headers getResponseHeaders() {
        return null;
    }

    @Override
    public URI getRequestURI() {
        return URI.create(path);
    }

    @Override
    public String getRequestMethod() {
        return "GET";
    }

    @Override
    public HttpContext getHttpContext() {
        return new HttpContext() {
            @Override
            public HttpHandler getHandler() {
                return null;
            }

            @Override
            public void setHandler(HttpHandler httpHandler) {

            }

            @Override
            public String getPath() {
                return "/";
            }

            @Override
            public HttpServer getServer() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public List<Filter> getFilters() {
                return null;
            }

            @Override
            public Authenticator setAuthenticator(Authenticator authenticator) {
                return null;
            }

            @Override
            public Authenticator getAuthenticator() {
                return null;
            }
        };
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return null;
    }

    @Override
    public OutputStream getResponseBody() {
        return null;
    }

    @Override
    public void sendResponseHeaders(int i, long l) throws IOException {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
}
