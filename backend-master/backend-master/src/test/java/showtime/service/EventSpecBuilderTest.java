package showtime.service;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import showtime.model.Event;
import showtime.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test values in showtime_wevent_20201112.sql
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventSpecBuilderTest {

    @Autowired
    EventRepository eventRepo;

    @Test
    void givenUserid_whenQuery_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("userid", "1");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(4, event.size());
        for(int i=0; i<3; i++) {
            assertEquals(1, event.get(i).getUserid());
        }
    }

    @Test
    void givenMultipleUserid_whenQuery_thenReturnMatchOnFirst() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.addAll("userid", List.of("1", "2", "3", "NaNImAStr"));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(4, event.size());
        for(int i=0; i<3; i++) {
            assertEquals(1, event.get(i).getUserid());
        }
    }

    @Test
    void givenTwoStart_whenQuery_thenReturnWithinRange() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.addAll("start", List.of("2020-10-05T13:59:59", "2020-10-05T14:00:01"));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(1, event.size());
        assertEquals(1, event.get(0).getEventid());
    }

    @Test
    void givenOneStartInList_whenQuery_thenReturnExact() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.addAll("start", List.of("2020-10-05T14:00:00"));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(1, event.size());
        assertEquals(1, event.get(0).getEventid());
    }

    @Test
    void givenTwoEndThatIsSame_whenQuery_thenReturnExact() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.addAll("end", List.of("2020-10-02T17:30:00", "2020-10-02T17:30:00"));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(2, event.size());
        for(int i=0; i<2; i++) {
            assertEquals(LocalDateTime.parse("2020-10-02T17:30:00"), event.get(i).getEnd());
        }
    }

    @Test
    void givenTitle_whenQuery_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("title", "teach 104");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        System.out.println(event);
        assertEquals(2, event.size());
        for(int i=0; i<2; i++) {
            assertEquals("teach 104", event.get(i).getTitle());
        }
    }

    @Test
    void givenDescription_whenQuery_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("description", "owe goodknee coffee");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(1, event.size());
        assertEquals("owe goodknee coffee", event.get(0).getDescription());
    }

    @Test
    void givenVisibility_whenQuery_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("visibility", "0");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(3, event.size());
        for(int i=0; i<3; i++) {
            assertEquals(0, event.get(i).getVisibility());
        }
    }

    @Test
    void givenOneType_whenQuery_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("type", "diary");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(2, event.size());
        for(int i=0; i<2; i++) {
            assertEquals("diary", event.get(i).getType());
        }
    }

    @ParameterizedTest
    @CsvSource({"3, durationevent, reminder", "4, durationevent, diary"})
    void givenMultipleType_whenQuery_thenReturnMatch(int expected, String input1, String input2) {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.addAll("type", List.of(input1, input2));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(expected, event.size());
        for(int i=0; i<expected; i++) {
            // full hamcrest
            MatcherAssert.assertThat(event.get(i).getType(), Matchers.is(Matchers.oneOf(input1, input2)));
        }
    }

    @Test
    void givenZeroType_whenQuery_thenReturnAll() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("type", "");
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(8, event.size());
    }

    @Test
    void givenSpecWithAllCriteria_whenQuery_thenNarrowToOne() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("userid", "1");
        mvp.addAll("start", List.of("2020-10-01T00:00:00", "2020-10-31T23:59:59"));
        mvp.add("title", "teach 104");
        mvp.add("description", "array");
        mvp.add("visibility", "0");
        mvp.addAll("type", List.of("budget", "diary", "reminder", "durationevent"));
        Specification<Event> spec = new EventSpecBuilder<>()
                .fromMultiValueMap(mvp)
                .build();

        List<Event> event = eventRepo.findAll(spec);
        assertEquals(1, event.size());
        assertEquals(1, event.get(0).getEventid());
    }

}
