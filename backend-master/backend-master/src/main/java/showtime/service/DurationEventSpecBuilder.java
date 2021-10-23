package showtime.service;

import org.springframework.util.MultiValueMap;
import showtime.model.DurationEvent;

import java.time.LocalDateTime;
import java.util.List;

public class DurationEventSpecBuilder extends EventSpecBuilder<DurationEvent> {

    public static DurationEventSpecBuilder createBuilder() {
        return new DurationEventSpecBuilder();
    }

    public DurationEventSpecBuilder byRemindTime(List<LocalDateTime> remindTime) {
        spec = spec.and(new SpecBetweenTwoEqualOneFrom<>("remindTime", remindTime));
        return this;
    }

    @Override
    public DurationEventSpecBuilder fromMultiValueMap(MultiValueMap<String, String> params) {
        super.fromMultiValueMap(params);
        return this;
    }
}
