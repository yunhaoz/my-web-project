package showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import showtime.model.DurationEvent;

public interface DurationEventRepository extends EventBaseRepository<DurationEvent> {
}
