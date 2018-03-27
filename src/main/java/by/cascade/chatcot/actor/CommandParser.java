package by.cascade.chatcot.actor;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
    private static final String ARGUMENT_PATTERN = "\\{.+?}";
    private static final String ARGUMENT_REPLACE = "{}";

    public LinkedList<String> parse(String command) {
        LinkedList<String> arguments = new LinkedList<>();
        Pattern pattern = Pattern.compile(ARGUMENT_PATTERN);
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            arguments.add(command.substring(matcher.start() + 1, matcher.end() - 1));
        }
        return arguments;
    }

    public String removeArgs(String command) {
        Pattern pattern = Pattern.compile(ARGUMENT_PATTERN);
        Matcher matcher = pattern.matcher(command);
        return matcher.replaceAll(ARGUMENT_REPLACE);
    }
}
