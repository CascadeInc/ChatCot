package by.cascade.chatcot.actor;

import by.cascade.chatcot.phrases.BotProcessor;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;

import static by.cascade.chatcot.command.CommandConstants.*;

public class ActionProcessor {
    private static final Logger LOGGER = LogManager.getLogger(ActionProcessor.class);

    static final String INITIALIZE = "init";
    static final String DATE_FORMAT = "dd.MM.yyyy";
    private BotProcessor bot;
    private ListAdapter listAdapter;

    public ActionProcessor(BotProcessor bot, ListAdapter listAdapter) {
        this.bot = bot;
        this.listAdapter = listAdapter;
    }

    public String doAction(String action) {
        if (INITIALIZE.equals(action)) {
            doInitialize(INITIALIZE_COMMAND.getCommand(), new LinkedList<>());
        }
        CommandParser parser = new CommandParser();
        LinkedList<String> arguments = parser.parse(action);
        action = parser.removeArgs(action);
        return mapping(bot.check(action), arguments);
    }

    public String mapping(String action, LinkedList<String> arguments) {
               if (GREETING_STANDARD_COMMAND.getCommand().equals(action)) {
                   return doGreetingStandard(action, arguments);
        } else if (GREETING_SPECIAL_COMMAND.getCommand().equals(action)) {
                   return doGreetingSpecial(action, arguments);
        } else if (GREETING_QUESTION_COMMAND.getCommand().equals(action)) {
                   return doGreetingQuestion(action, arguments);
        } else if (GREETING_ANSWER_COMMAND.getCommand().equals(action)) {
                   return doGreetingAnswer(action, arguments);
        } else if (YES_COMMAND.getCommand().equals(action)) {
                   return doYes(action, arguments);
        } else if (NO_COMMAND.getCommand().equals(action)) {
                   return doNo(action, arguments);
        } else if (INITIALIZE_COMMAND.getCommand().equals(action)) {
                   return doInitialize(action, arguments);
        } else if (FILTER_COMMAND.getCommand().equals(action)) {
                   return doFilter(action, arguments);
        } else if (ADDING_COMMAND.getCommand().equals(action)) {
                   return doAdding(action, arguments);
        } else {
                   return doDefault(action, arguments);
        }
    }

    public String doGreetingStandard(String action, LinkedList<String> arguments) {
        LOGGER.info("do greeting standard");
        return bot.getRandom(GREETING_STANDARD_COMMAND.getCommand());
    }
    public String doGreetingSpecial(String action, LinkedList<String> arguments) {
        LOGGER.info("do greeting special");
        return action;
    }
    public String doGreetingQuestion(String action, LinkedList<String> arguments) {
        LOGGER.info("do greeting question");
        return bot.getRandom(GREETING_ANSWER_COMMAND.getCommand());
    }
    public String doGreetingAnswer(String action, LinkedList<String> arguments) {
        LOGGER.info("do greeting answer");
        return bot.getRandom(YES_COMMAND.getCommand());
    }
    public String doYes(String action, LinkedList<String> arguments) {
        LOGGER.info("do yes");
        return action;
    }
    public String doNo(String action, LinkedList<String> arguments) {
        LOGGER.info("do no");
        return action;
    }
    public String doDefault(String action, LinkedList<String> arguments) {
        LOGGER.info("do default");
        return action;
    }
    public String doInitialize(String action, LinkedList<String> arguments) {
        LOGGER.info("do initialize");
        bot.initialize();
        return "Initialize Succesfull";
    }
    public String doFilter(String action, LinkedList<String> arguments) {
        LOGGER.info("do filter");
        if (arguments.size() <= ADDING_COMMAND.getArguments()) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            try {
                LOGGER.info("since = (" + format.parse(arguments.get(0)).toString() + ")");
                LOGGER.info("until = (" + format.parse(arguments.get(1)).toString() + ")");
                LinkedList<ListModel> list = listAdapter.filterTaskes(format.parse(arguments.get(0)), format.parse(arguments.get(1)));
                StringBuilder builder = new StringBuilder();
                for (ListModel model : list) {
                    builder.append(model.toString()).append("\n");
                }
                return builder.toString();
            } catch (ParseException e) {
                LOGGER.catching(e);
                return "INVALID ARGUMENT FOR FILTER";
            }
        }
        return action;
    }
    public String doAdding(String action, LinkedList<String> arguments) {
        if (arguments.size() <= ADDING_COMMAND.getArguments()) {
            listAdapter.addTask(arguments.get(0), arguments.get(1), 0);
            return doYes(action, arguments);
        }
        else {
            return doNo(action, arguments);
        }
    }
}
