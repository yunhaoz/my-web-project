package showtime.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import showtime.service.EventUpdateService;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonTypeName("1")
@Entity
@Table(name = "duration_event")
@PrimaryKeyJoinColumn(name = "eventid")
@DiscriminatorValue("durationevent")
public class DurationEvent extends Event {

    @Column(name = "remind_time")
    private LocalDateTime remindTime;

    public DurationEvent(@NotNull int userid, @NotNull LocalDateTime start, LocalDateTime end,
                         @NotNull String title, String description,
                         @NotNull int visibility, @NotNull String type, String location,
                         LocalDateTime remindTime) {
        super(userid, start, end, title, description, visibility, type, location);
        this.remindTime = remindTime;
    }

    public DurationEvent() {
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
        return "DurationEvent{" +
                "remindTime=" + remindTime +
                "} " + super.toString();
    }

    public LocalDateTime getRemindTime() {
        return remindTime;
    }
    
    @JsonSetter("remind_time")
    public void setRemindTime(LocalDateTime remind_time) {
        this.remindTime = remind_time;
    }
}
