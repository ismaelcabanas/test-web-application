package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateUtil.class})
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
        assertThat(actual.isExpired(), is(false));
    }

    @Test
    public void create_session_with_timeout(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeoutInMilliSeconds = 30;

        // when
        Session actual = Session.create(anUser, timeoutInMilliSeconds);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getSessionId(), is(notNullValue()));
        assertThat(actual.getUser(), is(notNullValue()));
        assertThat(actual.getUser().getUsername(), is(equalTo(ISMAEL_USERNAME)));
        assertThat(actual.getTimeout(), is(equalTo(30)));
        assertTrue(actual.getLastAccess() > 0);
        assertThat(actual.isExpired(), is(false));
    }

    @Test
    public void session_expired(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeoutInMilliSeconds = 30;

        PowerMockito.mockStatic(DateUtil.class);

        Long lastAccess = Long.valueOf(1300);
        Long expiredAccess = Long.valueOf(1350);
        PowerMockito.when(DateUtil.now()).thenReturn(lastAccess, expiredAccess);

        // when
        Session actual = Session.create(anUser, timeoutInMilliSeconds);

        // then
        assertThat(actual.isExpired(), is(true));

    }
}
