package showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import showtime.model.Budget;

import java.util.List;

public interface BudgetRepository extends EventBaseRepository<Budget>, JpaSpecificationExecutor<Budget> {
}
