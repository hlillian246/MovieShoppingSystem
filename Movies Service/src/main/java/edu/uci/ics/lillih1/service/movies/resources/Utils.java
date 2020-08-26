package edu.uci.ics.lillih1.service.movies.resources;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeRequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean validInputFormat(String input, String regex)
    {
        ServiceLogger.LOGGER.info("Input: " + input + ", REGEX: " + regex);

        Pattern n = Pattern.compile(regex);
        Matcher m = n.matcher(input);
        return m.find();
    }

}
