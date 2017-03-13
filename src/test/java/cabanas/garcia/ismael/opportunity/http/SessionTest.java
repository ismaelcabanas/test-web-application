package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class SessionTest {

    private static final String ISMAEL_USERNAME = "ismael";

    @Test
    public void create_session(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();

        // when
        Session actual = Session.create(anUser);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getSessionId(), is(notNullValue()));
        assertThat(actual.getUser(), is(notNullValue()));
        assertThat(actual.getUser().getUsername(), is(equalTo(ISMAEL_USERNAME)));
        assertThat(actual.getTimeout(), is(equalTo(-1)));
        assertThat(actual.getLastAccess(), is(notNullValue()));
    }

    @Test
    public void create_session_with_timeout(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeoutInSeconds = 30;

        // when
        Session actual = Session.create(anUser, timeoutInSeconds);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getSessionId(), is(notNullValue()));
        assertThat(actual.getUser(), is(notNullValue()));
        assertThat(actual.getUser().getUsername(), is(equalTo(ISMAEL_USERNAME)));
        assertThat(actual.getTimeout(), is(equalTo(30)));
        assertThat(actual.getLastAccess(), is(notNullValue()));
    }

/*    @Test
    public void session_expired(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeoutInSeconds = 30;

        // when
        Session actual = Session.create(anUser, timeoutInSeconds);

    }*/
}
