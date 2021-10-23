package showtime.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting values obtained from {@code get()}
 * using {@link org.springframework.util.LinkedMultiValueMap},
 * an implementation of {@code MultiValueMap} used in {@code Controller}.
 */
public class MVPUtil {

    /**
     * Converts the values from {@code LinkedList<String>} to
     * {@code ArrayList<Integer>}. Ignores malformed values during parsing.
     *
     * @param mapValue values from {@code MultiValueMap}
     * @return converted values
     */
    public static ArrayList<Integer> parseIntNoexcept(List<String> mapValue) {
        ArrayList<Integer> list = new ArrayList<>();
        if(mapValue != null) {
            for(String each : mapValue) {
                try {
                    list.add(Integer.parseInt(each));
                }
                catch(NumberFormatException nfe) { }
            }
        }
        return list;
    }

    /**
     * Further converts {@code ArrayList<Integer>} to {@code int[]}.
     */
    @Deprecated(since = "v0.4.3", forRemoval = true)
    public static int[] parseIntToArray(List<String> mapValue) {
        return parseIntNoexcept(mapValue).stream().mapToInt(i->i).toArray();
    }

    /**
     * Converts the values from {@code LinkedList<String>} to
     * {@code ArrayList<Double>}. Ignores malformed values during parsing.
     *
     * @param mapValue values from {@code MultiValueMap}
     * @return converted values
     */
    public static ArrayList<Double> parseDoubleNoexcept(List<String> mapValue) {
        ArrayList<Double> list = new ArrayList<>();
        if(mapValue != null) {
            for(String each : mapValue) {
                try {
                    list.add(Double.parseDouble(each));
                }
                catch(NumberFormatException nfe) { }
            }
        }
        return list;
    }

    /**
     * Converts the values from {@code LinkedList<String>} to
     * {@code ArrayList<String>}. Ignores empty {@code String}.
     *
     * @param mapValue values from {@code MultiValueMap}
     * @return converted values
     */
    public static ArrayList<String> toStringNoexcept(List<String> mapValue) {
        ArrayList<String> list = new ArrayList<>();
        if(mapValue != null) {
            for(String each : mapValue) {
                if(!each.equals("")) {
                    list.add(each);
                }
            }
        }
        return list;
    }

    /**
     * Further converts {@code ArrayList<String>} to {@code String[]}.
     */
    @Deprecated(since = "v0.4.3", forRemoval = true)
    public static String[] toStringToArray(List<String> mapValue) {
        return toStringNoexcept(mapValue).toArray(new String[0]);
    }



    /**
     * Converts the values from {@code LinkedList<String>} to
     * {@code ArrayList<LocalDateTime>}. Ignores malformed values during parsing.
     *
     * @param mapValue values from {@code MultiValueMap}
     * @return converted values
     */
    public static ArrayList<LocalDateTime> parseDateTimeNoexcept(List<String> mapValue) {
        ArrayList<LocalDateTime> list = new ArrayList<>();
        if(mapValue != null) {
            for(String each : mapValue) {
                try {
                    list.add(LocalDateTime.parse(each));
                } catch(DateTimeParseException dtpe) { }
            }
        }
        return list;
    }

    /**
     * Further converts {@code ArrayList<LocalDateTime>} to
     * {@code LocalDateTime[]}.
     */
    @Deprecated(since = "v0.4.3", forRemoval = true)
    public static LocalDateTime[] parseDateTimeToArray(List<String> mapValue) {
        return parseDateTimeNoexcept(mapValue).toArray(new LocalDateTime[0]);
    }
}
