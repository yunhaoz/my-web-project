package showtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import showtime.model.Reminder;

import java.time.LocalDateTime;

public class ReminderSpecBuilder extends EventSpecBuilder<Reminder> {

    Logger logger = LoggerFactory.getLogger(ReminderSpecBuilder.class);

    public static ReminderSpecBuilder createBuilder() {
        return new ReminderSpecBuilder();
    }

    /**
     * Constrains remind time. If one value is given, finds exact matches;
     * if two values are given, finds matches between range.
     */
    public ReminderSpecBuilder byRemindTime(LocalDateTime... remindTime) {
        if(remindTime.length >= 2) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.between(root.get("remindTime"), remindTime[0], remindTime[1])
            );
        }
        else if(remindTime.length >= 1) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("remindTime"), remindTime[0])
            );
        }
        return this;
    }

    /**
     * Constrains priority.
     */
    public ReminderSpecBuilder byPriority(int... priority) {
        spec = priority.length > 0
                ? spec.and(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority[0]))
                : spec;
        return this;
    }

    @Override
    public ReminderSpecBuilder fromMultiValueMap(MultiValueMap<String, String> params) {
        super.fromMultiValueMap(params);
        byRemindTime( MVPUtil.parseDateTimeToArray(params.get("remind_time")) );
        byPriority( MVPUtil.parseIntToArray(params.get("priority")) );
        return this;
    }
}
