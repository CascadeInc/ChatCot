package by.cascade.chatcot.command;

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
    ADDING_COMMAND("adding", 2);

    private final String command;
    private final int arguments;

    CommandConstants(String command, int arguments) {
        this.command = command;
        this.arguments = arguments;
    }


    public final String getCommand() {
        return command;
    }

    public final int getArguments() {
        return arguments;
    }
}
