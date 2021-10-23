package showtime.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import showtime.service.EventUpdateService;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@JsonTypeName("3")
@Entity
@Table(name = "diary")
@PrimaryKeyJoinColumn(name = "eventid")
@DiscriminatorValue("diary")
public class Diary extends Event {

    public Diary(@NotNull int userid, @NotNull LocalDateTime start, LocalDateTime end,
                 @NotNull String title, String description,
                 @NotNull int visibility, @NotNull String type, String location) {
        super(userid, start, end, title, description, visibility, type, location);
    }

    public Diary() {
    }

    /**
     * Allows an external helper service to modify the contents of the
     * current {@code Event} given another {@code Event}.
     */
    public void accept(EventUpdateService eventUpdateSvc, Event update) {
        eventUpdateSvc.visit(this, update);
    }

    @Override
    public String toString() {
        return "Diary{} " + super.toString();
    }
}
