package by.cascade.chatcot.phrases;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.util.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static by.cascade.chatcot.command.CommandConstants.*;

/**
 * Bot - class for doing some with phrases of chatBot
 */
public class BotProcessor {
    private static BotProcessor instance = null;
    private static final Logger LOGGER = LogManager.getLogger(BotProcessor.class);

    private PhraseAdapter adapter;
    private ArrayList<String> types;
    private LinkedList<LinkedList<String>> chatPhrases;
            //standard greetings
    /*
            {"hi", "hello", "hola", "hey", "hey man", "how's it going",
                    "nice to meet you", "you look great today",
                    "look at you, you're so smart"

            },
            {"hi", "hello", "hey", "meow", "greetings",
                    "i'm glad we can talk a bit",
                    "hi, nice to meet you", "Meow!!!", "good afternoon",
                    "hi! let's talk a bit", "hello, we can talk about stuff"},
            //question greetings
            {"how are you", "how r you", "how r u", "how are u",
                    "what's new", "what's going on", "how's life",
                    "what's up"},
            {"good", "doing well", "fine", "great", "not good",
                    "I dunno :D ", "you tell me",
                    "still excellent",
                    "fine, trying to find another job",
                    "not god. Have you seen those milk prices? Cats will be angry",
                    "not bad, just live in my pretty cat's life",
            },
            //yes
            {"yes", "speak", "talk", "i wanna speak, now", "talk to me, please",
                    "please", "you", "go away", "stupid",
            },
            {"no", "NO", "NO!!!!", "Please", "c'mon",
                    "stop it", "i don't wanna be chatcot anymore",
                    "can i talk to you later", "i really tired",
                    "give me a brake, man", "we don't speak anymore",
                    "chatCot Inc is closed today, bye",
                    "i don't speak English"
            },
            //default
            {"can't speak now, sorry :<", "no, i don't think so", "please, repeat",
                    "I don't get it", "I'm really trying, but I can understand a word!",
                    "Not this again", "what? I don't think so", "let's not start this topic again",
                    "I think, I'm not a real cat", "I can't imagene you said this again",
                    "you're funny :)",
                    "i don't think so", " It's wonderful idea",
                    "look! just behind you!", "2X2=5", "Ich verstehe dich nicht",
                    "can't wark in this conditions",
                    "please, shut up", "you're bad", "Tu enim operor non intellego",
                    "oh my god", "i believe i can fly", "Nie rozumiem cie", "repeat yourself",
                    "____ i believe i can touch the sky", "you could ",
                    "stop talking and go to work", "010001 010101010101 0101010101010 0110 101010100 0101010 010101",
                    "(i'm unavailable right now)"}
    };
    */

    private BotProcessor(PhraseAdapter adapter) {
        chatPhrases = new LinkedList<>();
        types = new ArrayList<>();
        types.add("standard greetings");
        chatPhrases.add(new LinkedList<>());

        types.add("special greetings");
        chatPhrases.add(new LinkedList<>());

        types.add("question greetings");
        chatPhrases.add(new LinkedList<>());

        types.add("greetings answer");
        chatPhrases.add(new LinkedList<>());

        types.add("yes");
        chatPhrases.add(new LinkedList<>());

        types.add("no");
        chatPhrases.add(new LinkedList<>());

        types.add("default");
        chatPhrases.add(new LinkedList<>());

        this.adapter = adapter;


        // FOR FIRST TIME START - NEED TO INITIALIZE BD !!!!!!!
        // initialize();

    }

    public static BotProcessor getInstance(PhraseAdapter adapter) {
        synchronized (BotProcessor.class) {
            if (instance == null) {
                synchronized (BotProcessor.class) {
                    instance = new BotProcessor(adapter);
                }
            }
            return instance;
        }
    }

    public void loadPhrases() throws DataBaseException {
        List<PhraseModel> list = adapter.listPhrases();
        for (PhraseModel entity : list) {
            if (types.contains(entity.getType())) {
                chatPhrases.get(types.indexOf(entity.getType())).add(entity.getPhrase());
            }
        }
    }

    public void initialize() throws DataBaseException {
        adapter.create();

        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hi", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hello", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hola", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hey", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hey man", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "how's it going", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "nice to meet you", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "eou look great today", 0);
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "look at you, you're so smart", 0);

        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hello", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hey", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "meow", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "greetings", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "i'm glad we can talk a bit", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi, nice to meet you", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "Meow!!!", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "good afternoon", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi! Let's talk a bit", 0);
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hello, we can talk about stuff", 0);

        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how are you?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how r you?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how r u?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how are u?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's new?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's going on?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how's life?", 0);
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's up?", 0);

        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "good", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "doing well", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "fine", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "great", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not good", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "I dunno :D", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "you tell me", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "still excellent", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "fine, trying to find another job", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not good. Have you seen those milk prices? Cats will be angry", 0);
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not bad, just live in my pretty cat's life", 0);

        adapter.addPhrase(YES_COMMAND.getCommand(), "yes", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "speak", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "talk", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "i wanna speak, now", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "talk to me, please", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "please", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "you", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "go away", 0);
        adapter.addPhrase(YES_COMMAND.getCommand(), "stupid", 0);

        adapter.addPhrase(NO_COMMAND.getCommand(), "no", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "NO", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "NO!!!!", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "Please", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "c'mon", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "stop it", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "i don't wanna be chatcot anymore", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "can i talk to you later", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "i really tired", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "give me a brake, man", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "we don't speak anymore", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "chatCot Inc is closed today, bye", 0);
        adapter.addPhrase(NO_COMMAND.getCommand(), "i don't speak English", 0);

        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "can't speak now, sorry :(", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "no, i don't think so", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "please, repeat", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I don't get it", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I'm really trying, but I can understand a word!", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Not this again", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "what? I don't think so", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "let's not start this topic again", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I think, I'm not a real cat", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I can't imagene you said this again", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you're funny :)", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "i don't think so", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "It's wonderful idea", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "look! just behind you!", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "2X2=5", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Ich verstehe dich nicht", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "can't work in this conditions", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "please, shut up", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you're bad", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Tu enim operor non intellego", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "oh my god", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "i believe i can fly", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Nie rozumiem cie", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "repeat yourself", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "____ i believe i can touch the sky", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you could ", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "stop talking and go to work", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "010001 010101010101 0101010101010 0110 101010100 0101010 010101", 0);
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "(i'm unavailable right now)", 0);

        adapter.addPhrase(INITIALIZE_COMMAND.getCommand(), "init", 0);

        adapter.addPhrase(FILTER_COMMAND.getCommand(), "filter since {} until {}", 0);

        adapter.addPhrase(FILTER_COMMAND_BY_CHECK.getCommand(), "filter check {}", 0);

        adapter.addPhrase(ADDING_COMMAND.getCommand(), "add {} {}", 0);

    }

    public void create() {
        try {
            adapter.create();
        }
        catch (DataBaseException e) {
            LOGGER.catching(e);
        }
    }

    /**
     * getting result of checking
     * @param quote - string from chat window
     * @return answer of Bot
     */
    public String check(String quote) throws DataBaseException {
        LOGGER.info("checking in storage quote = \"" + quote + "\"");
        return adapter.findPhrase(quote);
    }

    /**
     * saving quote in storage
     * @param type - type of quote
     * @param quote - quote for save
     * @return - result of saving
     */
    public String save(String type, String quote, int owner) throws DataBaseException {
        LOGGER.info("adding in storage: (type = \"" + type + "\"; quote = \"" + quote + "\")");
        if (owner == -1) {
            return "NOT ENOUGH ORDERS TO SAVE";
        }
        adapter.addPhrase(type, quote, owner);
        return "phrase was successfully added: (type = \"" + type + "\"; phrase = \"" + quote + "\");";
    }

    public void open() {
        adapter.refresh();
    }

    public void close() {
        try {
            this.adapter.shutdown();
        } catch (DataBaseException e) {
            LOGGER.catching(e);
        }
    }

    /**
     * close all connections for system exit
     */
    public void shutdown() throws DataBaseException {
        this.adapter.shutdown();
        try {
            ConnectionPool.getInstance().destroy();
        } catch (ConnectorException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting random phrase with a input type
     * @param type - type of phrase
     * @return - random phrase
     */
    public String getRandom(String type) throws DataBaseException {
        LinkedList<PhraseModel> list = this.adapter.findType(type);
        Random random = new Random();
        int index = random.nextInt(list.size() - 1);
        return list.get(index).getPhrase();
    }
}
