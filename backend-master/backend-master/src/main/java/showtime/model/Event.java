package showtime.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import showtime.service.EventUpdateService;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DurationEvent.class, name = "durationevent"),
        @JsonSubTypes.Type(value = Reminder.class, name = "reminder"),
        @JsonSubTypes.Type(value = Diary.class, name = "diary"),
        @JsonSubTypes.Type(value = Budget.class, name = "budget")
})
@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventid", nullable = false, unique = true)
    @NotNull
    private int eventid;

    @Column(name = "userid", nullable = false)
    @NotNull
    private int userid;

    @Column(name = "start", nullable = false)
    @NotNull
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "visibility", nullable = false)
    private int visibility = 0;

    @Column(name = "type", insertable = false, updatable = false, nullable = false)
    @NotNull
    private String type;

    @Column(name = "location")
    private String location;

    public Event(@NotNull int userid,
                 @NotNull LocalDateTime start, LocalDateTime end,
                 @NotNull String title, String description,
                 int visibility, @NotNull String type, String location) {
        this.eventid = eventid;
        this.userid = userid;
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.type = type;
        this.location = location;
    }

    public Event() {
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
        return "Event{" +
                "eventid=" + eventid +
                ", userid=" + userid +
                ", start=" + start +
                ", end=" + end +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", visibility=" + visibility +
                ", type=" + type +
                ", location='" + location + '\'' +
                '}';
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
