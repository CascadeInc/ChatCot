package by.cascade.chatcot.actor;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class for parsing command for getting arguments:
 * example:
 *  filter since {12.03.2017} until {21.04.2018}
 */
public class CommandParser {
    private static final String ARGUMENT_PATTERN = "\\{.+?}";
    private static final String ARGUMENT_REPLACE = "{}";

    /**
     * getting list of arguments from command
     * @param command - command with arguments
     * @return - list of arguments
     */
    public LinkedList<String> parse(String command) {
        LinkedList<String> arguments = new LinkedList<>();
        Pattern pattern = Pattern.compile(ARGUMENT_PATTERN);
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            arguments.add(command.substring(matcher.start() + 1, matcher.end() - 1));
        }
        return arguments;
    }

    /**
     * removing all arguments from command
     * @param command - input command with arguments. Example:
     *                "filter since {12.03.2017} until {21.04.2018}"
     * @return - command without arguments. Example:
     *                "filter since {} until {}"
     */
    public String removeArgs(String command) {
        Pattern pattern = Pattern.compile(ARGUMENT_PATTERN);
        Matcher matcher = pattern.matcher(command);
        return matcher.replaceAll(ARGUMENT_REPLACE);
    }
}
