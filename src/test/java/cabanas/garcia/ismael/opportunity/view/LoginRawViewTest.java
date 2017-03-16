package cabanas.garcia.ismael.opportunity.view;


import cabanas.garcia.ismael.opportunity.http.Response;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginRawViewTest {

    @Test
    public void render_should_write_redirect_parameter() throws UnsupportedEncodingException {
        // given
        String redirectPath = "/page1";
        LoginRawView sut = new LoginRawView(redirectPath);

        String expectedInputRedirect = "<input type=\"hidden\" name=\"redirect\" value=\"" + URLEncoder.encode(redirectPath, "UTF-8") + "\">";

        // when
        Response actual = sut.render();

        // then
        Assert.assertThat(new String(actual.getContent()), StringContains.containsString(expectedInputRedirect));
    }

    @Test
    public void render_should_not_write_redirect_parameter() throws UnsupportedEncodingException {
        // given
        LoginRawView sut = new LoginRawView();

        String expectedInputRedirect = "<input type=\"hidden\" name=\"redirect\" value=\"\">";

        // when
        Response actual = sut.render();

        // then
        Assert.assertThat(new String(actual.getContent()), StringContains.containsString(expectedInputRedirect));
    }
}
