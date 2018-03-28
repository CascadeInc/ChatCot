package by.cascade.chatcot.storage.databaseprocessing.phrases.sqlite;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
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

    private static final String SCHEME_NAME = SQLiteUtil.getScheme();
    private static final String TABLE_NAME = "phrases";

    private static final String ID_COLUMN = "id";
    private static final String TYPE_COLUMN = "type";
    private static final String PHRASE_COLUMN = "phrase";
    private static final String OWNER_COLUMN = "owner";


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
    public void addPhrase(String type, String phrase, int owner) throws DataBaseException {
        util.execUpdate("INSERT INTO " + SCHEME_NAME + "." + TABLE_NAME + "(" + TYPE_COLUMN + ", " + PHRASE_COLUMN + ")" + " VALUES " + "(\"" + type + "\", \"" + phrase + "\", " + owner + ");");
        LOGGER.info("Adding new phrase : (type = " + type + ", phrase = " + phrase + ") into (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * getting all phrases from DataBase
     * @return - list of phrases
     */
    @Override
    public List<PhraseModel> listPhrases() throws DataBaseException {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + ";");
        LOGGER.info("Getting all rows from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null) return list;
        return null;
    }


    /**
     * deleting list of phrases from DataBase
     * @param delList - list with phrases for deleting
     */
    @Override
    public void deletePhrases(List<PhraseModel> delList) throws DataBaseException {
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
    public void deletePhrase(PhraseModel model) throws DataBaseException {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + PHRASE_COLUMN + " = \"" + model.getPhrase() + "\";");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * deleting only id (not type and not phrase check)
     * @param model - only id
     */
    @Override
    public void deleteId(PhraseModel model) throws DataBaseException {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + ID_COLUMN + " = " + Integer.toString(model.getId()) + " ;");
        LOGGER.info("Deleting (id = " + model.getId() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * deleting by type and phrase
     * @param model - type and phrase
     */
    @Override
    public void deleteModel(PhraseModel model) throws DataBaseException {
        util.execUpdate("DELETE FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + PHRASE_COLUMN + " = \"" + model.getPhrase() + "\" and " + TYPE_COLUMN + " = \"" + model.getType() + "\" ;");
        LOGGER.info("Deleting (type = " + model.getType() + ", phrase = " + model.getPhrase() + ") from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    @Override
    public void create() throws DataBaseException {
        util.execUpdate("CREATE TABLE " + TABLE_NAME + " ( " + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " + TYPE_COLUMN + " VARCHAR (100) NOT NULL, " + PHRASE_COLUMN + " VARCHAR (200) NOT NULL, " + OWNER_COLUMN + " INTEGER);");
    }

    @Override
    public String findPhrase(String quote) throws DataBaseException {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE PHRASE = \"" + quote + "\";");
        LOGGER.info("Getting all rows from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null) return list.get(0).getType();
        return null;
    }

    @Override
    public LinkedList<PhraseModel> findType(String type) throws DataBaseException {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE TYPE = \"" + type + "\";");
        LOGGER.info("Getting all rows from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        return PhraseAdapter.getPhraseModels(set, LOGGER);
    }

    @Override
    public void refresh() {

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
