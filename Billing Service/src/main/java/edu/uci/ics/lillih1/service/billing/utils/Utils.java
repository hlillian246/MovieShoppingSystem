package edu.uci.ics.lillih1.service.billing.utils;

import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;

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

    public static boolean validCCFormat(String creditCard)
    {
        int len = creditCard.length();

        return len >= 16 && len <= 20;
    }

}
