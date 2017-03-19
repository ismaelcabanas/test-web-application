package cabanas.garcia.ismael.opportunity.steps.util;

import cabanas.garcia.ismael.opportunity.steps.model.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpUtil {

    private final String uri;
    List<NameValuePair> urlFormParameters = new ArrayList<>();
    List<Header> headers = new ArrayList<>();

    public HttpUtil(String uri) {
        this.uri = uri;
    }

    public static HttpClient create(){
        return HttpClientBuilder.create()
                .disableRedirectHandling()
                .build();
    }

    public void addFormParameter(String parameterName, String parameterValue){
        urlFormParameters.add(new BasicNameValuePair(parameterName, parameterValue));
    }

    public Response sendPut() throws Exception{
        HttpPut httpPut = new HttpPut(this.uri);
        if(!this.urlFormParameters.isEmpty()) {
            httpPut.setEntity(new UrlEncodedFormEntity(this.urlFormParameters));
        }

        httpPut.setHeaders(this.headers.toArray(new Header[this.headers.size()]));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpPut);
        return Response.builder()
                .statusCode(response.getStatusLine().getStatusCode())
                .content(getStringFromInputStream(response.getEntity().getContent()))
                .build();
    }


    public void addAuthorizationHeader(String authUser, String authPassword) {
        String auth = authUser + ":" + authPassword;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("UTF-8")));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        addHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);
    }

    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public Response sendDelete() throws Exception{
        HttpDelete httpDelete = new HttpDelete(this.uri);

        httpDelete.setHeaders(this.headers.toArray(new Header[this.headers.size()]));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpDelete);
        return Response.builder()
                .statusCode(response.getStatusLine().getStatusCode())
                .build();
    }

    public Response sendPost() throws Exception{
        HttpPost httpPost = new HttpPost(this.uri);
        if(!this.urlFormParameters.isEmpty()) {
            httpPost.setEntity(new UrlEncodedFormEntity(this.urlFormParameters));
        }

        httpPost.setHeaders(this.headers.toArray(new Header[this.headers.size()]));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpPost);
        return Response.builder()
                .statusCode(response.getStatusLine().getStatusCode())
                .content(getStringFromInputStream(response.getEntity().getContent()))
                .response(response)
                .build();
    }

    public Response sendGet() throws Exception{
        HttpGet httpGet = new HttpGet(this.uri);

        httpGet.setHeaders(this.headers.toArray(new Header[this.headers.size()]));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpGet);
        return Response.builder()
                .statusCode(response.getStatusLine().getStatusCode())
                .content(getStringFromInputStream(response.getEntity().getContent()))
                .build();
    }

    public void addHeader(String headerName, String headerValue) {
        headers.add(new BasicHeader(headerName, headerValue));
    }
}
