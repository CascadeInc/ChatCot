package by.cascade.chatcot.command;

/**
 * enum with all commands
 */
public enum CommandConstants {

    GREETING_STANDARD_COMMAND("standard greetings", 0),
    GREETING_SPECIAL_COMMAND("special greetings", 0),
    GREETING_QUESTION_COMMAND("question greetings", 0),
    GREETING_ANSWER_COMMAND("greetings answer", 0),
    YES_COMMAND("yes", 0),
    NO_COMMAND("no", 0),
    DEFAULT_COMMAND("default", 0),
    INITIALIZE_COMMAND("initialize", 0),
    FILTER_COMMAND("filter", 2),
    FILTER_COMMAND_BY_CHECK("filter check", 1),
    ADDING_COMMAND("adding", 3);

    private final String command;
    private final int arguments;

    /**
     * creating command
     * @param command - command
     * @param arguments - count of arguments
     */
    CommandConstants(String command, int arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    /**
     * getting command
     * @return - command
     */
    public final String getCommand() {
        return command;
    }

    /**
     * getting count of arguments
     * @return - count of arguments
     */
    public final int getArguments() {
        return arguments;
    }
}
