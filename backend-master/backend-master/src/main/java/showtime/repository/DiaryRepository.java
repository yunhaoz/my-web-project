package showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import showtime.model.Diary;

public interface DiaryRepository extends EventBaseRepository<Diary> {
}
