package by.cascade.chatcot.init;

import by.cascade.chatcot.phrases.BotProcessor;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Initiator {
    private static final Logger LOGGER = LogManager.getLogger(Initiator.class);

    public static void main(String[] args) {
        LOGGER.info("INIT MAIN------------------------------");
        try {
            BotProcessor processor = BotProcessor.getInstance(new PhrasesMySqlAdapter());
            processor.create();
            processor.close();
            UserAdapter userAdapter = new UserMySqlAdapter();
            userAdapter.create();
            userAdapter.shutdown();
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }
}
