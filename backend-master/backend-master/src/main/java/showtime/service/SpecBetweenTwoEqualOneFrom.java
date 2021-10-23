package showtime.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Creates a {@code Specification} that constrains between two values,
 * if the second value is {@code null}, constrains being equal to
 * the first value.
 *
 * @param <T> specification type, e.g. {@code Specification<Entity>}
 */
public class SpecBetweenTwoEqualOneFrom<T, U extends Comparable<? super U>> implements Specification<T> {

    private final String field;
    private final List<U> constraint;

    public SpecBetweenTwoEqualOneFrom(String field, List<U> constraint) {
        this.field = field;
        this.constraint = constraint;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(constraint.size() >= 2) {
            return criteriaBuilder.between(root.get(field), constraint.get(0), constraint.get(1));
        }
        else if(constraint.size() >= 1) {
            return criteriaBuilder.equal(root.get(field), constraint.get(0));
        }
        else {
            return null;
        }
    }
}
