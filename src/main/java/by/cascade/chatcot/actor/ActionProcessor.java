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

/**
 * class for performing operation
 */
public class ActionProcessor {
    private static final Logger LOGGER = LogManager.getLogger(ActionProcessor.class);

    private static final String INITIALIZE = "init";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private BotProcessor bot;
    private ListAdapter listAdapter;

    /**
     * creating processor for performing operations from main servlet
     * @param bot - chat bot, that used for performing operations
     * @param listAdapter - list adapter for performing operations with to-do list
     */
    public ActionProcessor(BotProcessor bot, ListAdapter listAdapter) {
        this.bot = bot;
        this.listAdapter = listAdapter;
    }

    /**
     * mapping and performing operation by String-name of the command.
     * Parsing input command for arguments and move it to mapping
     * @param action - command for performing
     * @return - result of performing
     */
    public String doAction(String action) {
        LOGGER.info("do action = " + action);
        if (INITIALIZE.equals(action)) {
            doInitialize();
        }
        CommandParser parser = new CommandParser();
        LinkedList<String> arguments = parser.parse(action);
        action = parser.removeArgs(action);
        return mapping(bot.check(action), arguments);
    }

    /**
     * mapping command and perform it
     * @param action - command
     * @param arguments - arguments of command
     * @return - result of performing
     */
    private String mapping(String action, LinkedList<String> arguments) {
               if (GREETING_STANDARD_COMMAND.getCommand().equals(action)) {
                   return doGreetingStandard();
        } else if (GREETING_SPECIAL_COMMAND.getCommand().equals(action)) {
                   return doGreetingSpecial(action);
        } else if (GREETING_QUESTION_COMMAND.getCommand().equals(action)) {
                   return doGreetingQuestion();
        } else if (GREETING_ANSWER_COMMAND.getCommand().equals(action)) {
                   return doGreetingAnswer();
        } else if (YES_COMMAND.getCommand().equals(action)) {
                   return doYes(action);
        } else if (NO_COMMAND.getCommand().equals(action)) {
                   return doNo(action);
        } else if (INITIALIZE_COMMAND.getCommand().equals(action)) {
                   return doInitialize();
        } else if (FILTER_COMMAND.getCommand().equals(action)) {
                   return doFilter(action, arguments);
        } else if (ADDING_COMMAND.getCommand().equals(action)) {
                   return doAdding(action, arguments);
        } else {
                   return doDefault(action);
        }
    }

    /**
     * performing standard greeting
     * @return - result of operation - greeting answer
     */
    private String doGreetingStandard() {
        LOGGER.info("do greeting standard");
        return bot.getRandom(GREETING_STANDARD_COMMAND.getCommand());
    }

    /**
     * performing special greeting
     * @param action - command
     * @return - result of operation - greeting answer
     */
    private String doGreetingSpecial(String action) {
        LOGGER.info("do greeting special");
        return action;
    }

    /**
     * performing question greeting
     * @return - result of operation - greeting answer
     */
    private String doGreetingQuestion() {
        LOGGER.info("do greeting question");
        return bot.getRandom(GREETING_ANSWER_COMMAND.getCommand());
    }

    /**
     * performing standard greeting
     * @return - result of operation - greeting answer
     */
    private String doGreetingAnswer() {
        LOGGER.info("do greeting answer");
        return bot.getRandom(YES_COMMAND.getCommand());
    }

    /**
     * performing standard agree
     * @param action - command
     * @return - result of operation - agree
     */
    private String doYes(String action) {
        LOGGER.info("do yes");
        return action;
    }

    /**
     * performing standard disagree
     * @param action - command
     * @return - result of operation - disagree
     */
    private String doNo(String action) {
        LOGGER.info("do no");
        return action;
    }

    /**
     * performing default
     * @param action - command
     * @return - result of operation - default
     */
    private String doDefault(String action) {
        LOGGER.info("do default");
        return action;
    }

    /**
     * performing initialize of DataBase or XML
     * @return - result of operation - message from initialization
     */
    private String doInitialize() {
        LOGGER.info("do initialize");
        bot.initialize();
        return "Initialize Successful";
    }

    /**
     * performing filter of to-do lists
     * @param action - command
     * @param arguments - arguments from command:
     *                  {since(DATE)} - begin date for filter
     *                  {until(DATE)} - end date for filter
     * @return - result of operation - valid to-do lists
     */
    private String doFilter(String action, LinkedList<String> arguments) {
        LOGGER.info("do filter");
        if (arguments.size() <= ADDING_COMMAND.getArguments()) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            try {
                LOGGER.info("since = (" + format.parse(arguments.get(0)).toString() + ")");
                LOGGER.info("until = (" + format.parse(arguments.get(1)).toString() + ")");
                LinkedList<ListModel> list = listAdapter.filterTaskes(format.parse(arguments.get(0)), format.parse(arguments.get(1)));
                StringBuilder builder = new StringBuilder();
                for (ListModel model : list) {
                    builder.append(model.toString()).append("<br>");
                }
                return builder.toString();
            } catch (ParseException e) {
                LOGGER.catching(e);
                return "INVALID ARGUMENT FOR FILTER";
            }
        }
        return action;
    }

    /**
     * performing adding new to-do list
     * @param action - command
     * @param arguments - arguments from command:
     *                  {text(STRING}         - text of task
     *                  {description(STRING)} - description of task
     * @return - result of operation - yes - successful operation
     *                                  no - not successful operation
     */
    private String doAdding(String action, LinkedList<String> arguments) {
        if (arguments.size() <= ADDING_COMMAND.getArguments()) {
            listAdapter.addTask(arguments.get(0), arguments.get(1), 0);
            return doYes(action);
        }
        else {
            return doNo(action);
        }
    }
}
