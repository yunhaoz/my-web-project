package showtime.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import showtime.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Examines JSON before it reaches Jackson or {@link EventController}. Validates
 * {@link Event#type} to help Jackson auto deserialize polymorphic {@code Event}s.
 */
@ControllerAdvice(assignableTypes = {EventController.class})
public class EventJsonPostPutAdvice extends RequestBodyAdviceAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EventJsonPostPutAdvice.class);

    // https://stackoverflow.com/questions/22444/my-regex-is-matching-too-much-how-do-i-make-it-stop

    private static final Pattern jsonPat = Pattern.compile("\"type\"\\s*:\\s*\"(.*?)\"[,}]");
    private static final Pattern uriPat = Pattern.compile("\\/api\\/event\\/(.*?)(?:\\/|$)");

    @Autowired
    private HttpServletRequest req;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        boolean isSupported = targetType.getTypeName().equals(Event.class.getTypeName());
        logger.trace("supports=" + isSupported);
        return isSupported;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        logger.trace("Before Body Read for URI: " + req.getRequestURI());
        return new MappingJacksonInputMessage(
                new ByteArrayInputStream(validateType(inputMessage.getBody()).getBytes()),
                inputMessage.getHeaders()
        );
    }

    /**
     * Validates {@link Event#type}. In particular, {@code type} must match the URI address
     * the request is sent to. In case {@code type} is absent, automatically appends a type
     * according to the URI address.
     */
    private String validateType(InputStream input) throws IOException {

        String uriType = "";
        Matcher uriMatcher = uriPat.matcher(req.getRequestURI());
        if(uriMatcher.find()) {
            uriType = uriMatcher.group(1);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String json = br.lines().collect(Collectors.joining("\n"));
        Matcher matcher = jsonPat.matcher(json);
        if(matcher.find()) {
            String jsonType = matcher.group(1);

            if(!jsonType.equals(uriType)) {
                String errMsg = "Request to /api/event/expected=" + uriType +
                        " has JSON type mismatch, found=" + jsonType;
                logger.debug(errMsg);
                // wtf? deprecated?
                throw new JsonMappingException(errMsg);
            }
            logger.debug("Request to /api/event/expected=" + uriType + " has JSON type match");
        }
        else {
            logger.debug("Request to /api/event/expected=" + uriType + " has JSON type NOT found");
            json = json.substring(0, json.length()-1).concat(",\"type\":\"" + uriType + "\"}");
        }

        return json;
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleJsonValidationException(JsonMappingException jme) {
        return new ResponseEntity<>(jme.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
