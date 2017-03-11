package cabanas.garcia.ismael.opportunity.server.sun;

import cabanas.garcia.ismael.opportunity.http.ResponseHeaderConstants;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SunHttpAuthenticationFilterTest {

    @Mock
    private Filter.Chain chain;

    @Test
    public void if_authenticated_user_request_a_private_resource_then_the_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeAuthenticatedUserStub("/page1");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        sut.getConfiguration().addPrivateResource("/page1");

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void if_authenticated_user_request_a_non_private_resource_then_the_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeAuthenticatedUserStub("/page2");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        sut.getConfiguration().addPrivateResource("/page1");

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void if_unauthenticated_user_request_a_private_resource_then_redirect_to_login_view() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/page1");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        sut.getConfiguration().addPrivateResource("/page1");

        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verifyZeroInteractions(chain);
        verify(httpExchangeSpy).sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
    }

    @Test
    public void if_unauthenticated_user_request_a_private_resource_then_set_location_response_header_with_redirect_path_value() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/page1");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        sut.getConfiguration().redirectPath("/login");
        sut.getConfiguration().addPrivateResource("/page1");

        // when
        sut.doFilter(httpExchange, chain);

        // then
        Headers headers = httpExchange.getResponseHeaders();
        String locationHeaderValue = headers.getFirst(ResponseHeaderConstants.LOCATION);
        assertThat(locationHeaderValue, is(equalTo(sut.getConfiguration().getRedirectPath())));
    }

    @Test
    public void if_unauthenticated_user_request_a_non_private_resource_then_request_is_processed() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeUnauthenticatedUserStub("/publicPage");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        sut.getConfiguration().addPrivateResource("/page1");

        // when
        sut.doFilter(httpExchange, chain);

        // then
        verify(chain).doFilter(httpExchange);
    }

    @Test
    public void description(){
        // given
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();

        // when
        String actual = sut.description();

        // then
        assertThat(actual, is(equalTo(SunHttpAuthenticationFilter.DESCRIPTION)));
    }
/*    @Test
    public void process_request_made_for_aunthenticated_users() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeSuccessResourceStub("page1");
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        verify(chain).doFilter(httpExchangeSpy);
        verify(httpExchangeSpy, times(0)).sendResponseHeaders(anyInt(), any());
    }

    @Test
    public void generate_cookie_if_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeStub();
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();

        // when
        sut.doFilter(httpExchange, chain);

        // then
        Headers headers = httpExchange.getResponseHeaders();
        String cookieHeaderValue = headers.getFirst(ResponseHeaderConstants.SET_COOKIE);
        assertThat(cookieHeaderValue, is(not(nullValue())));
    }

    @Test
    public void not_generate_cookie_if_not_authenticated_user() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeStub();
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();

        // when
        sut.doFilter(httpExchange, chain);

        // then
        Headers headers = httpExchange.getResponseHeaders();
        String cookieHeaderValue = headers.getFirst(ResponseHeaderConstants.SET_COOKIE);
        assertThat(cookieHeaderValue, is(nullValue()));
    }

    @Test
    public void process_request_when_user_authenticated_previously() throws Exception{
        // given
        HttpExchange httpExchange = new HttpExchangeWithAuthenticatedCookieStub();
        SunHttpAuthenticationFilter sut = new SunHttpAuthenticationFilter();
        HttpExchange httpExchangeSpy = Mockito.spy(httpExchange);

        // when
        sut.doFilter(httpExchangeSpy, chain);

        // then
        //verify(httpExchangeSpy).get
    }*/
}
