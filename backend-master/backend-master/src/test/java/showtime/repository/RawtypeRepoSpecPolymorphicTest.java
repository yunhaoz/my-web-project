package showtime.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import showtime.model.Budget;
import showtime.model.Diary;
import showtime.service.BudgetSpecBuilder;
import showtime.service.DiarySpecBuilder;
import showtime.service.DurationEventSpecBuilder;
import showtime.service.EventSpecBuilder;
import showtime.service.ReminderSpecBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RawtypeRepoSpecPolymorphicTest {

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private DurationEventRepository durationEventRepo;
    @Autowired
    private ReminderRepository reminderRepo;
    @Autowired
    private DiaryRepository diaryRepo;
    @Autowired
    private BudgetRepository budgetRepo;

    private static final List<EventBaseRepository> repos = new ArrayList<>();
    private static final List<EventSpecBuilder> specs = new ArrayList<>();

    private static Diary diary;
    private static Budget budget;

    @BeforeAll
    public static void initRepoSpec(@Autowired EventRepository er,
                                    @Autowired DurationEventRepository der,
                                    @Autowired ReminderRepository rr,
                                    @Autowired DiaryRepository dr,
                                    @Autowired BudgetRepository br) {
        repos.add(er);
        repos.add(der);
        repos.add(rr);
        repos.add(dr);
        repos.add(br);

        specs.add(EventSpecBuilder.createBuilder());
        specs.add(DurationEventSpecBuilder.createBuilder());
        specs.add(ReminderSpecBuilder.createBuilder());
        specs.add(DiarySpecBuilder.createBuilder());
        specs.add(BudgetSpecBuilder.createBuilder());
    }

    @BeforeAll
    public static void initEntity() {
        diary = new Diary(11,
                LocalDateTime.parse("2020-11-11T03:50:59"),
                null,
                "test diary1",
                "test diary description 1",
                0, "diary",
                null
        );
        budget = new Budget(11,
                LocalDateTime.parse("2020-11-15T18:10:00"),
                null,
                "test budget1",
                "test budget description 1",
                0, "budget",
                null,
                -199801.01,
                "inferior goods",
                1);
    }

    @Test
    void givenBudgetSpec_whenQueryUsingRaw_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("amount", "5");
        mvp.add("category", "coffee");
        mvp.add("transactionuserid", "4");
        Specification specRaw = specs.get(4).fromMultiValueMap(mvp).build();
        List<Budget> eventList = repos.get(4).findAll(specRaw);

        assertEquals(1, eventList.size());
        assertEquals(7, eventList.get(0).getEventid());
        assertEquals(5, eventList.get(0).getAmount());
        assertEquals("coffee", eventList.get(0).getCategory());
        assertEquals(4, eventList.get(0).getEbudTransactionUserid());
    }

    @Test
    void givenDiary_whenSaveUsingRaw_thenSaveToOnlyEventBudgetTable() {
        //Budget newBudget = (Budget) repos.get(4).save(budget);
        Budget newBudget = budgetRepo.save(budget);

        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("userid", "11");
        Specification specRaw = specs.get(4).fromMultiValueMap(mvp).build();
        List<Budget> eventList = repos.get(4).findAll(specRaw);

        assertEquals(1, eventList.size());
        assertEquals(-199801.01, eventList.get(0).getAmount());
        assertEquals("inferior goods", eventList.get(0).getCategory());
        assertEquals(1, eventList.get(0).getEbudTransactionUserid());
    }

/*    static List<MultiValueMap<String, String>> genPerm() {

        final String[][] params = {{"amount", "-5"}, {"category", "coffee"}, {"transactionuserid", "4"}};

        List<MultiValueMap<String, String>> powerSet = new ArrayList<>();
        MultiValueMap<String, String> mapAmt = new LinkedMultiValueMap<>();
        mapAmt.add("amount", "-5");
        powerSet.add(mapAmt);
        MultiValueMap<String, String> mapCat = new LinkedMultiValueMap<>();
        mapCat.add("category", "coffee");
        powerSet.add(mapCat);
        MultiValueMap<String, String> mapTxid = new LinkedMultiValueMap<>();
        mapTxid.add("transactionuserid", "4");
        powerSet.add(mapTxid);

        return powerSet;
    }*/

}
