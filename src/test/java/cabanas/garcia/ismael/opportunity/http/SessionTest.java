package cabanas.garcia.ismael.opportunity.http;

import cabanas.garcia.ismael.opportunity.model.User;
import cabanas.garcia.ismael.opportunity.util.DateUtil;
import cabanas.garcia.ismael.opportunity.util.TimeProvider;
import cabanas.garcia.ismael.opportunity.util.UUIDProvider;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionTest {

    private static final String ISMAEL_USERNAME = "ismael";

    @Mock
    private UUIDProvider uuidProvider;

    @Mock
    private TimeProvider timeProvider;

    @Test
    public void create_session(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeout = 60;
        String aSessionId = "aSessionId";
        Long now = 10000L;

        when(uuidProvider.generateUUID()).thenReturn(aSessionId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Session actual = new Session(anUser, timeout, uuidProvider, timeProvider);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getSessionId(), is(equalTo(aSessionId)));
        assertThat(actual.getUser(), is(equalTo(anUser)));
        assertThat(actual.getTimeout(), is(equalTo(timeout)));
        assertThat(actual.getLastAccess(), is(equalTo(now)));
        assertThat(actual.hasExpired(), is(equalTo(false)));
    }

    @Test
    public void session_expired(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeout = 60;
        Long now = 10000L;

        when(timeProvider.now()).thenReturn(now, now*10);

        // when
        Session actual = new Session(anUser, timeout, uuidProvider, timeProvider);

        // then
        assertThat(actual.hasExpired(), is(equalTo(true)));

    }

    @Test
    public void session_expire_when_timeout_is_0(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeout = 0;
        Long now = 10000L;

        when(timeProvider.now()).thenReturn(now);

        // when
        Session actual = new Session(anUser, timeout, uuidProvider, timeProvider);

        // then
        assertThat(actual.hasExpired(), is(equalTo(true)));

    }

    @Test
    public void session_never_expire(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeout = -1;
        Long now = 10000L;

        when(timeProvider.now()).thenReturn(now, now*10);

        // when
        Session actual = new Session(anUser, timeout, uuidProvider, timeProvider);

        // then
        assertThat(actual.hasExpired(), is(equalTo(false)));

    }

    @Test
    public void session_invalidate(){
        // given
        User anUser = User.builder().username(ISMAEL_USERNAME).build();
        int timeInSeconds = 30;
        Long now = 10000L;

        when(timeProvider.now()).thenReturn(now, now*10);

        Session session = new Session(anUser, timeInSeconds, uuidProvider, timeProvider);

        // when
        session.invalidate();

        // then
        assertThat(session, is(notNullValue()));
        assertThat(session.getSessionId(), is(equalTo(StringUtils.EMPTY)));
        assertThat(session.getUser(), is(nullValue()));
        assertThat(session.getLastAccess(), is(equalTo(0L)));
        assertThat(session.hasExpired(), is(true));
    }
}
