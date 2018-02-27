package by.cascade.chatcot.storage.databaseprocessing.phrases.sqlite;

import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.util.SQLiteUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class SQLiteAdapter implements PhraseAdapter {
    private static final Logger LOGGER = LogManager.getLogger(SQLiteAdapter.class);

    private SQLiteUtil util;

    private final String schemeName = SQLiteUtil.getScheme();
    private final String tableName = "phrases";

    private final String idColumn = "id";
    private final String typeColumn = "type";
    private final String phraseColumn = "phrase";


    public SQLiteAdapter() {
        util = SQLiteUtil.getInstance();
        LOGGER.info("creating MySQL Adapter");
    }

    /**
     * adding new phrase in DataBase
     * @param type - type of phrase
     * @param phrase - phrase
     */
    @Override
    public void addPhrase(String type, String phrase) {
        util.execUpdate("INSERT INTO " + schemeName + "." + tableName + "(" + typeColumn + ", " + phraseColumn + ")" + " VALUES " + "(\"" + type + "\", \"" + phrase + "\");");
        LOGGER.info("Adding new phrase : (type = " + type + ", phrase = " + phrase + ") into (scheme = " + schemeName + ", table = " + tableName + ")");
    }

    /**
     * getting all phrases from DataBase
     * @return - list of phrases
     */
    @Override
    public List<PhraseModel> listPhrases() {
        ResultSet set = util.exec("SELECT * FROM " + schemeName + "." + tableName + ";");
        LOGGER.info("Getting all rows from (scheme = " + schemeName + ", table = " + tableName);
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null) return list;
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
        util.execUpdate("DELETE FROM " + schemeName + "." + tableName + " WHERE " + phraseColumn + " = \"" + model.getPhrase() + "\";");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + schemeName + ", table = " + tableName + ")");
    }

    /**
     * deleting only id (not type and not phrase check)
     * @param model - only id
     */
    @Override
    public void deleteId(PhraseModel model) {
        util.execUpdate("DELETE FROM " + schemeName + "." + tableName + " WHERE " + idColumn + " = " + Integer.toString(model.getId()) + " ;");
        LOGGER.info("Deleting (id = " + model.getId() + ") from (scheme = " + schemeName + ", table = " + tableName + ")");
    }

    /**
     * deleting by type and phrase
     * @param model - type and phrase
     */
    @Override
    public void deleteModel(PhraseModel model) {
        util.execUpdate("DELETE FROM " + schemeName + "." + tableName + " WHERE " + phraseColumn + " = \"" + model.getPhrase() + "\" and " + typeColumn + " = \"" + model.getType() + "\" ;");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + schemeName + ", table = " + tableName + ")");
    }

    @Override
    public void create() {
        util.execUpdate("CREATE TABLE " + tableName + " ( " + idColumn + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " + typeColumn + " VARCHAR (100) NOT NULL, " + phraseColumn + " VARCHAR (200) NOT NULL);");
    }

    @Override
    public String findPhrase(String quote) {
        ResultSet set = util.exec("SELECT * FROM " + schemeName + "." + tableName + " WHERE PHRASE = \"" + quote + "\";");
        LOGGER.info("Getting all rows from (scheme = " + schemeName + ", table = " + tableName);
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null) return list.get(0).getType();
        return null;
    }

    @Override
    public LinkedList<PhraseModel> findType(String type) {
        ResultSet set = util.exec("SELECT * FROM " + schemeName + "." + tableName + " WHERE TYPE = \"" + type + "\";");
        LOGGER.info("Getting all rows from (scheme = " + schemeName + ", table = " + tableName);
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
