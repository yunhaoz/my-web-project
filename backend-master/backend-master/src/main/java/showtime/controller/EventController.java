package showtime.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import showtime.model.Budget;
import showtime.model.Diary;
import showtime.model.DurationEvent;
import showtime.model.Event;
import showtime.model.Reminder;
import showtime.repository.BudgetRepository;
import showtime.repository.DiaryRepository;
import showtime.repository.DurationEventRepository;
import showtime.repository.EventBaseRepository;
import showtime.repository.EventRepository;
import showtime.repository.ReminderRepository;
import showtime.service.BudgetSpecBuilder;
import showtime.service.DiarySpecBuilder;
import showtime.service.DurationEventSpecBuilder;
import showtime.service.EventSpecBuilder;
import showtime.service.EventUpdateService;
import showtime.service.ReminderSpecBuilder;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/*
As of current, Jackson is unable to automatically deserialize JSON into
polymorphic entities. The workarounds are:

1. Assign a different Controller for each polymorphic entity type
+ validation and implementation is the easiest
- duplicated code and violation of OOP

2. Read raw JSON strings into the Controller and deserialize based on type
+ information is directly available in the Controller,
  validation and logic can be implemented easily
- Spring's existing functionalities are ignored

3. Customize Jackson's JSONDeserializer and annotations
+ Spring's existing functionalities are utilized
- Many separated DTOs may be required. e.g. If using @JsonTypeInfo,
  a "type" information will be required for all JSON,
  but polymorphism is required for POST not PUT

4. Intercept the request before it reaches the Controller
+ Spring's existing functionalities are utilized
- Complicated code. JSON validation logic is spread among classes.
 */
@RestController
@RequestMapping("/api/event")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

/*    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private DurationEventRepository durationEventRepo;
    @Autowired
    private ReminderRepository reminderRepo;
    @Autowired
    private DiaryRepository diaryRepo;
    @Autowired
    private BudgetRepository budgetRepo;
    @Autowired
    private EventRepoSpecService eventSvc;*/

    // Because of constraint on JpaSpecificationExecutor, currently used as raw type
    @SuppressWarnings("rawtypes")
    private final Map<String, EventBaseRepository> repos;
    @SuppressWarnings("rawtypes")
    private final Map<String, Supplier<EventSpecBuilder>> specs;

    @Autowired
    private EventUpdateService eventUpdateSvc;

    @Autowired
    public EventController(EventRepository er,
                           DurationEventRepository der,
                           ReminderRepository rr,
                           DiaryRepository dr,
                           BudgetRepository br) {
        repos = new HashMap<>();
        repos.put("/api/event/rawevent", er);
        repos.put("/api/event/durationevent", der);
        repos.put("/api/event/reminder", rr);
        repos.put("/api/event/diary", dr);
        repos.put("/api/event/budget", br);

        specs = new HashMap<>();
        specs.put("/api/event/rawevent", EventSpecBuilder::createBuilder);
        specs.put("/api/event/durationevent", DurationEventSpecBuilder::createBuilder);
        specs.put("/api/event/reminder", ReminderSpecBuilder::createBuilder);
        specs.put("/api/event/diary", DiarySpecBuilder::createBuilder);
        specs.put("/api/event/budget", BudgetSpecBuilder::createBuilder);
    }

    // TODO: Security checks

    // TODO: Exception Handling
    /**
     * Responds to "/api/event/GET" requests. Retrieves {@link Event} or subclasses
     * from the database. Supports dynamic creation of queries backed by
     * {@code Specification} by including corresponding fields and values in the
     * request URL. Also supports paging, using the URL parameters
     * {@code sort=field,asc/desc}, {@code page=?} and {@code size=?}.
     */
    @GetMapping({"/rawevent", "/durationevent", "/reminder", "/diary", "/budget"})
    public ResponseEntity<?> getEventByCriteria(
            HttpServletRequest request,
            @RequestParam MultiValueMap<String, String> params,
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

        // "/api/event/rawevent"
        String requestURI = request.getRequestURI();
        logger.debug("Request GET to " + requestURI);

        @SuppressWarnings("unchecked")
        Slice<Event> eventList = repos.get(requestURI).findAll(
                specs.get(requestURI).get().fromMultiValueMap(params).build(),
                pageable
        );

        return new ResponseEntity<>(eventList.getContent(), HttpStatus.OK);
    }
    
    /**
     * Return Event that belongs to the current user
     */
    @GetMapping({"/personalEvent"})
    public ResponseEntity<?> getPersonalEventByCriteria(
            HttpServletRequest request,
            @RequestParam MultiValueMap<String, String> params) {
        
        List<String> repoURIs = Arrays.asList("/api/event/durationevent", "/api/event/diary", "/api/event/budget");
        @SuppressWarnings("unchecked")
        List<Event> eventList = repos.get("/api/event/reminder").findAll(
                specs.get("/api/event/reminder").get().fromMultiValueMap(params).build()
                ,Sort.by(Sort.Direction.ASC, "start")
        );
        for (String repoURI : repoURIs) {
        	@SuppressWarnings("unchecked")
        	List<Event> childEventList = repos.get(repoURI).findAll(
                   specs.get(repoURI).get().fromMultiValueMap(params).build()
                   ,Sort.by(Sort.Direction.ASC, "start")
            );
        	eventList.addAll(childEventList);
        }
        

        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }
    
    /**
     * Return Public Event that visibility >= 1
     */
    @GetMapping({"/publicEvent"})
    public ResponseEntity<?> getPublicEventByCriteria(
            HttpServletRequest request,
            @RequestParam MultiValueMap<String, String> params) {
    	
        String currentUserId = params.getFirst("userid");
        params.remove("userid");
        // System.out.println("Current User Id Remove" + currentUserId);
        
        List<String> repoURIs = Arrays.asList("/api/event/durationevent", "/api/event/diary", "/api/event/budget");
        @SuppressWarnings("unchecked")
        List<Event> eventList = repos.get("/api/event/durationevent").findAll(
                specs.get("/api/event/durationevent").get().fromMultiValueMap(params).build()
                ,Sort.by(Sort.Direction.ASC, "start")
        );
        
        
        // System.out.println("My List" + eventList.toString());
        List<Event> returnList = new ArrayList<Event>();
        for (Event event : eventList) {
        	if (Integer.toString(event.getUserid()).equals(currentUserId) || event.getVisibility() < 1) {
        		// System.out.println("To Remove" + event.toString());
        	} else {
        		// System.out.println("To Keep" + event.toString());
        		returnList.add(event);
        	}
        }
        
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }
    
    /**
     * Responds to "/api/event/POST" requests. Creates a new {@link Event} in the database.
     */
    @PostMapping({"/durationevent", "/reminder", "/diary", "/budget"})
    public ResponseEntity<Event> createEvent(HttpServletRequest request, @RequestBody Event event) {

        String requestURI = request.getRequestURI();
        logger.debug("Request POST to " + requestURI + " with RequestBody=" + event);
        // System.out.println(event.toString());
        // because of auto increment, no duplicates will exist
        Event saved = (Event) repos.get(requestURI).save(event);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
	
    /**
     * Responds to "/api/event/PUT" requests. Updates an existing {@link Event}
     * in the database.
     */
    @PutMapping({"/durationevent", "/reminder", "/diary", "/budget"})
    public ResponseEntity<? extends Event> updateEvent(HttpServletRequest request, @RequestBody Event event) {

        String requestURI = request.getRequestURI();
        logger.debug("Request PUT to " + requestURI + "with RequestBody=" + event);

        @SuppressWarnings("unchecked")
        Optional<Event> eventO = repos.get(requestURI).findById(event.getEventid());

        if(eventO.isPresent()) {
            Event eventGet = eventO.get();
            eventGet.accept(eventUpdateSvc, event);
            Event eventUpdate = (Event) repos.get(requestURI).save(eventGet);
            return new ResponseEntity<>(eventUpdate, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
