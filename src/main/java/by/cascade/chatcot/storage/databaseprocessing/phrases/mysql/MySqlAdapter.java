package by.cascade.chatcot.storage.databaseprocessing.phrases.mysql;

import by.cascade.chatcot.storage.databaseprocessing.util.MySqlUtil;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


/**
 * class for doing transactions by using MySQL server
 */
public class MySqlAdapter implements PhraseAdapter {
    private static final Logger LOGGER = LogManager.getLogger(MySqlAdapter.class);

    private MySqlUtil util;

    private final String SCHEME_NAME = "phr";
    private final String TABLE_NAME = "phrases";

    private final String ID_COLUMN = "id";
    private final String TYPE_COLUMN = "type";
    private final String PHRASE_COLUMN = "phrase";


    public MySqlAdapter() {
        util = new MySqlUtil();
        LOGGER.info("creating MySQL Adapter");
    }

    /**
     * adding new phrase in DataBase
     * @param type - type of phrase
     * @param phrase - phrase
     */
    @Override
    public void addPhrase(String type, String phrase) {
        util.execUpdate("INSERT INTO " + SCHEME_NAME + "." + TABLE_NAME + "(" + TYPE_COLUMN + ", " + PHRASE_COLUMN + ")" + " VALUES " + "(\"" + type + "\", \"" + phrase + "\");");
        LOGGER.info("Adding new phrase : (type = " + type + ", phrase = " + phrase + ") into (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * getting all phrases from DataBase
     * @return - list of phrases
     */
    @Override
    public List<PhraseModel> listPhrases() {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + ";");
        LOGGER.info("Getting all rows from MuSQL (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }


    /**
     * deleting list of phrases from DataBase
     * @param delList - list with phrases for deleting
     */
    @Override
    public void deletePhrases(List<PhraseModel> delList) {
        for (PhraseModel model : delList) {
            deleteId(model);
        }
        LOGGER.info("Deleting list of models");
    }

    /**
     * deleting only phrase (not id and not type checking)
     * @param model - only phrase
     */
    @Override
    public void deletePhrase(PhraseModel model) {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + PHRASE_COLUMN + " = \"" + model.getPhrase() + "\";");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * deleting only id (not type and not phrase check)
     * @param model - only id
     */
    @Override
    public void deleteId(PhraseModel model) {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + ID_COLUMN + " = " + Integer.toString(model.getId()) + " ;");
        LOGGER.info("Deleting (id = " + model.getId() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * deleting by type and phrase
     * @param model - type and phrase
     */
    @Override
    public void deleteModel(PhraseModel model) {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + PHRASE_COLUMN + " = \"" + model.getPhrase() + "\" and " + TYPE_COLUMN + " = \"" + model.getType() + "\" ;");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    @Override
    public void create() {
        util.execUpdate("CREATE SCHEMA IF NOT EXISTS " + SCHEME_NAME + " DEFAULT CHARACTER SET utf8;");
        util.execUpdate("CREATE TABLE IF NOT EXISTS " + SCHEME_NAME + "." + TABLE_NAME + " (" + ID_COLUMN + " INT NOT NULL AUTO_INCREMENT, " + TYPE_COLUMN + " VARCHAR(100) NOT NULL, " + PHRASE_COLUMN + " VARCHAR(200) NOT NULL, PRIMARY KEY(id), UNIQUE INDEX " + ID_COLUMN + "_UNIQUE (" + ID_COLUMN + " ASC)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;");
    }

    @Override
    public String findPhrase(String quote) {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE PHRASE = \"" + quote + "\";");
        LOGGER.info("Finding phrase \"" + quote + "\" in (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null && !list.isEmpty())
            return list.get(0).getType();
        return null;
    }

    @Override
    public LinkedList<PhraseModel> findType(String type) {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE TYPE = \"" + type + "\";");
        LOGGER.info("Getting all rows from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        return PhraseAdapter.getPhraseModels(set, LOGGER);
    }


    /**
     * closing connection from DataBase
     */
    @Override
    public void shutdown() {
        util.shutdown();
        LOGGER.info("Shutdown connection");
    }
}
