package showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import showtime.model.Reminder;

public interface ReminderRepository extends EventBaseRepository<Reminder> {
}
