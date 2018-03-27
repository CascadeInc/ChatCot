package by.cascade.chatcot.phrases;

import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
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

    public void loadPhrases() {
        List<PhraseModel> list = adapter.listPhrases();
        for (PhraseModel entity : list) {
            if (types.contains(entity.getType())) {
                chatPhrases.get(types.indexOf(entity.getType())).add(entity.getPhrase());
            }
        }
    }

    public void initialize() {
        adapter.create();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hi");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hello");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hola");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hey");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "hey man");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "how's it going");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "nice to meet you");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "eou look great today");
        adapter.addPhrase(GREETING_STANDARD_COMMAND.getCommand(), "look at you, you're so smart");

        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hello");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hey");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "meow");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "greetings");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "i'm glad we can talk a bit");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi, nice to meet you");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "Meow!!!");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "good afternoon");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hi! Let's talk a bit");
        adapter.addPhrase(GREETING_SPECIAL_COMMAND.getCommand(), "hello, we can talk about stuff");

        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how are you?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how r you?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how r u?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how are u?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's new?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's going on?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "how's life?");
        adapter.addPhrase(GREETING_QUESTION_COMMAND.getCommand(), "what's up?");

        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "good");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "doing well");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "fine");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "great");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not good");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "I dunno :D");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "you tell me");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "still excellent");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "fine, trying to find another job");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not good. Have you seen those milk prices? Cats will be angry");
        adapter.addPhrase(GREETING_ANSWER_COMMAND.getCommand(), "not bad, just live in my pretty cat's life");

        adapter.addPhrase(YES_COMMAND.getCommand(), "yes");
        adapter.addPhrase(YES_COMMAND.getCommand(), "speak");
        adapter.addPhrase(YES_COMMAND.getCommand(), "talk");
        adapter.addPhrase(YES_COMMAND.getCommand(), "i wanna speak, now");
        adapter.addPhrase(YES_COMMAND.getCommand(), "talk to me, please");
        adapter.addPhrase(YES_COMMAND.getCommand(), "please");
        adapter.addPhrase(YES_COMMAND.getCommand(), "you");
        adapter.addPhrase(YES_COMMAND.getCommand(), "go away");
        adapter.addPhrase(YES_COMMAND.getCommand(), "stupid");

        adapter.addPhrase(NO_COMMAND.getCommand(), "no");
        adapter.addPhrase(NO_COMMAND.getCommand(), "NO");
        adapter.addPhrase(NO_COMMAND.getCommand(), "NO!!!!");
        adapter.addPhrase(NO_COMMAND.getCommand(), "Please");
        adapter.addPhrase(NO_COMMAND.getCommand(), "c'mon");
        adapter.addPhrase(NO_COMMAND.getCommand(), "stop it");
        adapter.addPhrase(NO_COMMAND.getCommand(), "i don't wanna be chatcot anymore");
        adapter.addPhrase(NO_COMMAND.getCommand(), "can i talk to you later");
        adapter.addPhrase(NO_COMMAND.getCommand(), "i really tired");
        adapter.addPhrase(NO_COMMAND.getCommand(), "give me a brake, man");
        adapter.addPhrase(NO_COMMAND.getCommand(), "we don't speak anymore");
        adapter.addPhrase(NO_COMMAND.getCommand(), "chatCot Inc is closed today, bye");
        adapter.addPhrase(NO_COMMAND.getCommand(), "i don't speak English");


        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "can't speak now, sorry :(");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "no, i don't think so");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "please, repeat");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I don't get it");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I'm really trying, but I can understand a word!");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Not this again");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "what? I don't think so");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "let's not start this topic again");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I think, I'm not a real cat");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "I can't imagene you said this again");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you're funny :)");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "i don't think so");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "It's wonderful idea");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "look! just behind you!");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "2X2=5");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Ich verstehe dich nicht");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "can't work in this conditions");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "please, shut up");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you're bad");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Tu enim operor non intellego");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "oh my god");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "i believe i can fly");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "Nie rozumiem cie");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "repeat yourself");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "____ i believe i can touch the sky");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "you could ");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "stop talking and go to work");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "010001 010101010101 0101010101010 0110 101010100 0101010 010101");
        adapter.addPhrase(DEFAULT_COMMAND.getCommand(), "(i'm unavailable right now)");

        adapter.addPhrase(INITIALIZE_COMMAND.getCommand(), "init");

        adapter.addPhrase(FILTER_COMMAND.getCommand(), "filter since {} until {}");

        adapter.addPhrase(ADDING_COMMAND.getCommand(), "add {} {}");

    }

    /**
     * checking contains string into phrases
     * @param in - input string for checking
     * @param str - storage of phrases
     * @return true - if str contains in
     *         false - if str don't contains in
     */
    public boolean contains(String in, String[] str){
        boolean match = false;
        for (String aStr : str) {
            if (aStr.equals(in)) {
                match = true;
            }
        }
        return match;
    }

    /**
     * getting result of checking
     * @param quote - string from chat window
     * @return answer of Bot
     */
    public String check(String quote) {
        LOGGER.info("checking in storage quote = \"" + quote + "\"");
        return adapter.findPhrase(quote);
    }

    public String save(String type, String quote) {
        LOGGER.info("adding in storage: (type = \"" + type + "\"; quote = \"" + quote + "\")");
        adapter.addPhrase(type, quote);
        return "phrase was successfully added: (type = \"" + type + "\"; phrase = \"" + quote + "\");";
    }

    public void shutdown() {
        this.adapter.shutdown();
    }

    public String getRandom(String type) {
        LinkedList<PhraseModel> list = this.adapter.findType(type);
        Random random = new Random();
        int index = random.nextInt(list.size() - 1);
        return list.get(index).getPhrase();
    }
}
