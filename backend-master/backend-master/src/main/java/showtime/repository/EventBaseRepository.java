package showtime.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import showtime.model.Event;

import java.util.List;

@NoRepositoryBean
public interface EventBaseRepository<T extends Event>
        extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {
}
