package showtime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import showtime.model.User;
import showtime.repository.UserRepository;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;

    /**
     * Responds to "api/user.GET" requests. Retrieves user information from the database
     * when Cookies can be used to authenticate that the current user matches the user
     * whose information is being retrieved.
     *
     * @param email email of user whose information is being retrieved
     * @param principal principal of the current user
     * @return {@code 200 OK} if credentials match <br>
     *         {@code 401 UNAUTHORIZED} if missing credentials or credentials mismatch
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email, Principal principal) {
        // locate current user from Principal
        // as suggested in Spring Security topical guide
        Authentication authentication = (Authentication) principal;
        // no credentials, perhaps missing Cookie?
        if(authentication == null) {
            return new ResponseEntity<>("Unauthorized access from non-login user.", HttpStatus.UNAUTHORIZED);
        }
        org.springframework.security.core.userdetails.User authorizedUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        // mismatch credentials, requested user is not current user
        if(!authorizedUser.getUsername().equals(email)) {
            return new ResponseEntity<>("Unauthorized access to other-than-login user.", HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOpt = userRepo.findUserByEmail(email);
        if(userOpt.isPresent()) {
            return new ResponseEntity<>(userOpt.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/userid")
    public int getCurrentUserid(Principal principal) {
        Authentication authentication = (Authentication) principal;
        // no credentials, perhaps missing Cookie?
        if(authentication == null) {
            return -1;
        }

        String email = principal.getName();
        //System.out.println(email);
        Optional<User> userOpt = userRepo.findUserByEmail(email);
        if(userOpt.isPresent()) {
            return userOpt.get().getId();
        }
        else {
            return -1;
        }
    }
    
    @GetMapping("/username")
    public String getCurrentUserName(Principal principal) {
        Authentication authentication = (Authentication) principal;
        // no credentials, perhaps missing Cookie?
        if(authentication == null) {
            return "";
        }

        String email = principal.getName();
        System.out.println(email);
        Optional<User> userOpt = userRepo.findUserByEmail(email);
        if(userOpt.isPresent()) {
            return userOpt.get().getUsername();
        }
        else {
            return "";
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserinfo(Principal principal) {
        // locate current user from Principal
        // as suggested in Spring Security topical guide
        Authentication authentication = (Authentication) principal;
        // no credentials, perhaps missing Cookie?
        if(authentication == null) {
            return new ResponseEntity<>("Unauthorized access from non-login user.", HttpStatus.UNAUTHORIZED);
        }
        
        String email = principal.getName();
        Optional<User> userOpt = userRepo.findUserByEmail(email);
        if(userOpt.isPresent()) {
            return new ResponseEntity<>(userOpt.get(), HttpStatus.OK);
        }
        
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Responds to "/api/user.POST" requests. Registers a new user in the database.
     *
     * @param user user in {@code JSON} format
     * @return {@code 201 CREATED} and the new user's {@code JSON} if user is created. <br>
     *         {@code 400 BAD REQUEST} if {@code JSON} cannot be parsed <br>
     *         {@code 409 CONFLICT} if user with given email already exists <br>
     *         {@code 500 INTERNAL SERVER ERROR} if server cannot handle the request
     */
    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // creates User with custom Jackson deserializer, or custom @JsonSetter annotations
        logger.debug(user.toString());
        Optional<User> userOpt = userRepo.findUserByEmail(user.getEmail());
        // user exists
        if(userOpt.isPresent()) {
            logger.debug(user.getEmail() + " 409 CONFLICT");
            return new ResponseEntity<>(user.getEmail() + " exists.", HttpStatus.CONFLICT);
        }
        else {
            // creates new user
            try {
                logger.debug(user.getEmail() + " 201 CREATED");
                User newUser = userRepo.save(user);
                return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            }
            // unknown exception
            catch(Exception e) {
                logger.debug(user.getEmail() + " 500 INTERNAL SERVER ERROR");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Handles {@code JsonProcessingException} when parsing {@code User} for
     * {@code registerNewUser()}. Exceptions are thrown when given invalid {@code JSON}
     * in {@link showtime.service.UserDeserializerService}.
     *
     * @param jpe exception
     * @return {@code 400 BAD REQUEST} if {@code JSON} cannot be parsed
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException jpe) {
        logger.debug(jpe.getMessage() + " 400 BAD REQUEST");
        return new ResponseEntity<>(jpe.getMessage() + " at \"/user.POST\".", HttpStatus.BAD_REQUEST);
    }

    /**
     * A temporary solution for updating users.
     */
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        // creates User with custom Jackson deserializer, or custom @JsonSetter annotations
        logger.debug(user.toString());
        Optional<User> userOpt = userRepo.findUserByEmail(user.getEmail());
        // user exists
        if(userOpt.isPresent()) {
            User userGet = userOpt.get();
            userGet.setFname(user.getFname());
            userGet.setLname(user.getLname());
            if(user.getPassword() != null && user.getPassword() != "") {
                userGet.setPassword(user.getPassword());
            }
            User userUpdated = userRepo.save(userGet);

            return new ResponseEntity<>(userUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}