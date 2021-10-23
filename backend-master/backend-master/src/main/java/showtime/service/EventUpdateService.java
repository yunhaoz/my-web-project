package showtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import showtime.model.Budget;
import showtime.model.Diary;
import showtime.model.DurationEvent;
import showtime.model.Event;
import showtime.model.Reminder;

/**
 * Updates {@link Event} using values from a new {@code Event}.
 * The current implementation does not support validating values in
 * the new {@code Event}, and everything will be passed as is.
 * If an invalid field or value is encountered, JPA is required to
 * catch the errors.
 */
@Component
public class EventUpdateService {

    Logger logger = LoggerFactory.getLogger(EventUpdateService.class);

    public void visit(Event event, Event update) {
        logger.debug("Visited Event");
        visitBaseEvent(event, update);
    }

    public void visit(DurationEvent event, Event update) {
        logger.debug("Visited DurationEvent");
        visitBaseEvent(event, update);
        if(update instanceof DurationEvent) {
            DurationEvent deUpdate = (DurationEvent) update;
            event.setRemindTime(deUpdate.getRemindTime());
        }
    }

    public void visit(Reminder event, Event update) {
        logger.debug("Visited Reminder");
        visitBaseEvent(event, update);
        if(update instanceof Reminder) {
            Reminder rUpdate = (Reminder) update;
            event.setRemindTime(rUpdate.getRemindTime());
            event.setPriority(rUpdate.getPriority());
        }
    }

    public void visit(Diary event, Event update) {
        logger.debug("Visited Diary");
        visitBaseEvent(event, update);
    }

    public void visit(Budget event, Event update) {
        logger.debug("Visited Budget");
        visitBaseEvent(event, update);
        if(update instanceof Budget) {
            Budget bUpdate = (Budget) update;
            event.setAmount(bUpdate.getAmount());
            event.setCategory(bUpdate.getCategory());
            event.setEbudTransactionUserid(bUpdate.getEbudTransactionUserid());
        }
    }

    private void visitBaseEvent(Event event, Event update) {
        // eventid is immutable
        // userid is immutable
        event.setStart(update.getStart());
        event.setEnd(update.getEnd());
        event.setTitle(update.getTitle());
        event.setDescription(update.getDescription());
        event.setVisibility(update.getVisibility());
        // type is immutable
        event.setLocation(update.getLocation());
    }
}
