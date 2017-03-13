package cabanas.garcia.ismael.opportunity.http.cookies;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class CookieAdapterTest {

    @Test
    public void toCookies_simple(){
        // given
        String simpleRawCookie = "cookieName1=cookieValue1";
        List<String> cookieList = Arrays.asList(simpleRawCookie);

        // when
        Cookies cookies = CookieAdapter.toCookies(cookieList);

        // then
        Assert.assertThat(cookies, is(notNullValue()));
        Assert.assertThat(cookies.get("cookieName1").get(), is(IsEqual.equalTo(Cookie.builder().name("cookieName1").value("cookieValue1").build())));
    }

    @Test
    public void toCookies_complex(){
        // given
        String complexRawCookie = "cookieName1=cookieValue1;cookieName2=cookieValue2";
        List<String> cookieList = Arrays.asList(complexRawCookie);

        // when
        Cookies cookies = CookieAdapter.toCookies(cookieList);

        // then
        Assert.assertThat(cookies, is(notNullValue()));
        Assert.assertThat(cookies.get("cookieName1").get(), is(IsEqual.equalTo(Cookie.builder().name("cookieName1").value("cookieValue1").build())));
    }
}
