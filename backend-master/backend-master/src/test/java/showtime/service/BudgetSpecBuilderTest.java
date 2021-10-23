package showtime.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import showtime.model.Budget;
import showtime.repository.BudgetRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BudgetSpecBuilderTest {

    @Autowired
    private BudgetRepository budgetRepo;

    @Test
    void givenBudgetSpec_whenQueryBudget_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("amount", "0.00");
        Specification<Budget> specBudget = new BudgetSpecBuilder()
                .fromMultiValueMap(mvp)
                .build();
        List<Budget> eventList = budgetRepo.findAll(specBudget);

        assertEquals(0, eventList.size());
    }

    @Test
    void givenBudgetSpecFromScratch_whenQueryBudget_thenReturnMatch() {
        Specification<Budget> specBudget = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("amount"), 0);
        List<Budget> eventList = budgetRepo.findAll(specBudget);

        assertEquals(0, eventList.size());
    }

    @Test
    void givenBudgetSpec_whenQueryBudgetAmount_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("amount", "-5");
        Specification<Budget> specBudget = new BudgetSpecBuilder()
                .fromMultiValueMap(mvp)
                .build();
        List<Budget> eventList = budgetRepo.findAll(specBudget);

        assertEquals(1, eventList.size());
    }

    @Test
    void givenBudgetSpec_whenQueryBudgetCategory_thenReturnMatch() {
        MultiValueMap<String, String> mvp = new LinkedMultiValueMap<>();
        mvp.add("category", "coffee");
        Specification<Budget> specBudget = new BudgetSpecBuilder()
                .fromMultiValueMap(mvp)
                .build();
        List<Budget> eventList = budgetRepo.findAll(specBudget);

        assertEquals(1, eventList.size());
    }
}
