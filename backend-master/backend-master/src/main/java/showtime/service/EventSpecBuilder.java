package showtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import showtime.model.Event;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A more versatile {@code EventSpecification}. Generates a
 * {@code Specification<Event>} using the given values in a
 * {@code MultiValueMap}, or supports dynamically building a
 * {@code Specification<Event>}.
 *
 * @param <T> type of {@code Specification}, must be subclass of {@code Event}
 */
public class EventSpecBuilder<T extends Event> {

    Logger logger = LoggerFactory.getLogger(EventSpecBuilder.class);

    public static <U extends Event> EventSpecBuilder<U> createBuilder() {
        return new EventSpecBuilder<>();
    }

    /**
     * Finds all.
     */
    protected Specification<T> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and();

    /**
     * Constrains eventid.
     */
    public EventSpecBuilder<T> byEventid(int... eventid) {
        spec = eventid.length > 0
                ? spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventid"), eventid[0]))
                : spec;
        return this;
    }

    /**
     * Constrains userid.
     */
    public EventSpecBuilder<T> byUserid(int... userid) {
        spec = userid.length > 0
                ? spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userid"), userid[0]))
                : spec;
        return this;
    }

    /**
     * Constrains start. If one value is given, finds exact matches;
     * if two values are given, finds matches between range.
     */
    public EventSpecBuilder<T> byStart(LocalDateTime... start) {
        if(start.length >= 2) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("start"), start[0], start[1])
            );
        }
        else if(start.length >= 1) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("start"), start[0])
            );
        }
        return this;
    }

    /**
     * Constrains end. If one value is given, finds exact matches;
     * if two values are given, finds matches between range.
     */
    public EventSpecBuilder<T> byEnd(LocalDateTime... end) {
        if(end.length >= 2) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("end"), end[0], end[1])
            );
        }
        else if(end.length >= 1) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("end"), end[0])
            );
        }
        return this;
    }

    /**
     * Constrains title. Matches using sql "like".
     */
    public EventSpecBuilder<T> byTitle(String... title) {
        spec = title.length > 0
                ? spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), title[0]))
                : spec;
        return this;
    }

    /**
     * Constrains description. Matches using sql "like".
     */
    public EventSpecBuilder<T> byDescription(String... description) {
        spec = description.length > 0
                ? spec.and(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get("description"), description[0]))
                : spec;
        return this;
    }

    /**
     * Constrains visibility.
     */
    public EventSpecBuilder<T> byVisibility(int... visibility) {
        spec = visibility.length > 0
                ? spec.and(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("visibility"), visibility[0]))
                : spec;
        return this;
    }

    /**
     * Constrains type. Finds matches that matches any of the given type.
     */
    public EventSpecBuilder<T> byType(String... type) {
        if(type.length > 0) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> {
                        List<Predicate> typePreds = new ArrayList<>();
                        for(String each : type) {
                            typePreds.add( criteriaBuilder.equal(root.get("type"), each) );
                        }
                        return criteriaBuilder.or(typePreds.toArray(new Predicate[0]));
                    }
            );
        }
        return this;
    }

    /**
     * Constrains location.
     */
    public EventSpecBuilder<T> byLocation(String... location) {
        spec = location.length > 0
                ? spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), location[0]))
                : spec;
        return this;
    }

    /**
     * Returns the actual {@code Specification}.
     */
    public Specification<T> build() {
        return this.spec;
    }

    /**
     * Automatically adds constraints from {@code MultiValueMap}.
     */
    public EventSpecBuilder<T> fromMultiValueMap(MultiValueMap<String, String> params) {
        byEventid( MVPUtil.parseIntToArray(params.get("eventid")) );
        byUserid( MVPUtil.parseIntToArray(params.get("userid")) );
        byStart( MVPUtil.parseDateTimeToArray(params.get("start")) );
        byEnd( MVPUtil.parseDateTimeToArray(params.get("end")) );
        byTitle( MVPUtil.toStringToArray(params.get("title")) );
        byDescription( MVPUtil.toStringToArray(params.get("description")) );
        byVisibility( MVPUtil.parseIntToArray(params.get("visibility")) );
        byType( MVPUtil.toStringToArray(params.get("type")) );
        byLocation( MVPUtil.toStringToArray(params.get("location")) );
        return this;
    }
}
