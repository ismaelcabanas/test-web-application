package cabanas.garcia.ismael.opportunity.repository;

import cabanas.garcia.ismael.opportunity.http.Session;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class InMemorySessionRepositoryTest {

    private InMemorySessionRepository sut;

    @Before
    public void setUp() throws Exception {
        sut = InMemorySessionRepository.getInstance();
    }

    @After
    public void tearDown(){
        InMemorySessionRepository.getInstance().deleteAll();
    }

    @Test
    public void delete_existent_session(){
        // given
        String aSessionId = "aSessionId";

        sut.persist(Session.builder().sessionId(aSessionId).build());

        // when
        sut.delete(aSessionId);

        // then
        Optional<Session> actual = sut.read(aSessionId);
        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void find_existent_session(){
        // given
        String aSessionId = "aSessionId";

        sut.persist(Session.builder().sessionId(aSessionId).build());

        // when
        Optional<Session> actual = sut.read(aSessionId);

        // then
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get().getSessionId(), is(equalTo(aSessionId)));
    }

    @Test
    public void find_not_existent_session(){
        // given
        String aSessionId = "aSessionId";

        // when
        Optional<Session> actual = sut.read(aSessionId);

        // then
        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void persist_session(){
        // given
        String aSessionId = "aSessionId";

        Session session = Session.builder().sessionId(aSessionId).build();
        sut.persist(session);

        // when
        Session actual = sut.persist(session);

        // then
        assertThat(actual, is(IsNull.notNullValue()));
        assertThat(actual.getSessionId(), is(equalTo(aSessionId)));
    }

    @Test
    public void delete_all_sessions(){
        // given
        String aSessionId = "aSessionId";

        Session session = Session.builder().sessionId(aSessionId).build();
        sut.persist(session);

        // when
        sut.deleteAll();

        // then
        assertThat(sut.isEmpty(), is(true));
    }

}
