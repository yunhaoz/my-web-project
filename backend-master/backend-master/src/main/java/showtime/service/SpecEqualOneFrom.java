package showtime.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Creates a {@code Specification} that constrains being equal to
 * the first value.
 *
 * @param <T> specification type, e.g. {@code Specification<Entity>}
 */
public class SpecEqualOneFrom<T> implements Specification<T> {

    private final String field;
    private final List<?> constraint;

    public <U> SpecEqualOneFrom(String field, List<U> constraint) {
        this.field = field;
        this.constraint = constraint;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return constraint.size() > 0
                ? criteriaBuilder.equal(root.get(field), constraint.get(0))
                : null;
    }
}
