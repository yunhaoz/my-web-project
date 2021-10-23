package showtime.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import showtime.model.User;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class UserDeserializerService extends JsonDeserializer<User> {

    // special thanks to PiKeXi
    private static final Pattern pattern = Pattern.compile(
            "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    );

    @Override
    public User deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        ObjectCodec oc = parser.getCodec();
        JsonNode node = oc.readTree(parser);

        User user = new User();

        // email, required, not null
        JsonNode email = node.get("email");
        String emailStr;
        if(email == null || !pattern.matcher(emailStr = email.asText()).find()) {
            throw new JsonProcessingException(email + " is not a valid email") { };
        }
        user.setEmail(emailStr);

        // username, not null, default to email if field not included or empty
        JsonNode username = node.get("username");
        String usernameStr = username == null || username.asText().equals("") ? emailStr : username.asText();
        user.setUsername(usernameStr);

        // password, required, not null, only tests if less than 8 characters
        JsonNode password = node.get("password");
        String passwordStr;
        if(password == null || (passwordStr = password.asText()).trim().length() < 8) {
            throw new JsonProcessingException(password + " is not a valid password") { };
        }
        user.setPassword(passwordStr);

        // fname, required, not null
        JsonNode fname = node.get("fname");
        String fnameStr;
        if(fname == null || (fnameStr = fname.asText().trim()).length() == 0) {
            throw new JsonProcessingException(fname + " is not a valid first name") { };
        }
        user.setFname(fnameStr);

        // lname, required, not null
        JsonNode lname = node.get("lname");
        String lnameStr;
        if(lname == null || (lnameStr = lname.asText().trim()).length() == 0) {
            throw new JsonProcessingException(lname + " is not a valid last name") { };
        }
        user.setLname(lnameStr);

        // latitude, not null, default to 91
        JsonNode lat = node.get("latitude");
        double latDouble = lat == null ? 91 : lat.asDouble();
        user.setLatitude(latDouble);

        // longitude, not null, default to 181
        JsonNode lon = node.get("longitude");
        double lonDouble = lon == null ? 181 : lon.asDouble();
        user.setLongitude(lonDouble);

        return user;
    }
}
