package showtime.service;

import org.springframework.util.MultiValueMap;
import showtime.model.Diary;

// diary has no additional fields;
// however, user may call a concrete class instead of EventSpecBuilderService<Diary>
public class DiarySpecBuilder extends EventSpecBuilder<Diary> {

    public static DiarySpecBuilder createBuilder() {
        return new DiarySpecBuilder();
    }

    @Override
    public DiarySpecBuilder fromMultiValueMap(MultiValueMap<String, String> params) {
        super.fromMultiValueMap(params);
        return this;
    }
}
