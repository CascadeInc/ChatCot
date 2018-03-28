package by.cascade.chatcot.storage.databaseprocessing.phrases.mysql;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
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
public class PhrasesMySqlAdapter implements PhraseAdapter {
    private static final Logger LOGGER = LogManager.getLogger(PhrasesMySqlAdapter.class);

    private MySqlUtil util;

    private static final String SCHEME_NAME = "phr";
    private static final String TABLE_NAME = "phrases";
    private static final String ID_COLUMN = "id";
    private static final String TYPE_COLUMN = "type";
    private static final String PHRASE_COLUMN = "phrase";
    private static final String OWNER_COLUMN = "owner";


    public PhrasesMySqlAdapter() throws DataBaseException {
        util = new MySqlUtil();
        LOGGER.info("creating MySQL Adapter");
    }

    public void refresh() {
        LOGGER.info("refresh: " + util);
        try {
            util.shutdown();
            util = new MySqlUtil();
            LOGGER.info("create new phrase connection = " + util);
        }
        catch (DataBaseException e) {
            LOGGER.catching(e);
        }
    }

    /**
     * adding new phrase in DataBase
     * @param type - type of phrase
     * @param phrase - phrase
     */
    @Override
    public void addPhrase(String type, String phrase, int owner) throws DataBaseException {
        util.execUpdate("INSERT INTO " + SCHEME_NAME + "." + TABLE_NAME + "(" + TYPE_COLUMN + ", " + PHRASE_COLUMN + ", " + OWNER_COLUMN + ")" + " VALUES " + "(\"" + type + "\", \"" + phrase + "\", " + owner + ");");
        LOGGER.info("Adding new phrase : (type = " + type + ", phrase = " + phrase + ") into (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    /**
     * getting all phrases from DataBase
     * @return - list of phrases
     */
    @Override
    public List<PhraseModel> listPhrases() throws DataBaseException {
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
        try {
            util.execUpdate("CREATE SCHEMA IF NOT EXISTS " + SCHEME_NAME + " DEFAULT CHARACTER SET utf8;");
        }
        catch (DataBaseException e) {
            LOGGER.info("scheme \"" + SCHEME_NAME + "\" is exist");
        }
        try {
            util.execUpdate("CREATE TABLE IF NOT EXISTS " + SCHEME_NAME + "." + TABLE_NAME + " (" + ID_COLUMN + " INT NOT NULL AUTO_INCREMENT, " + TYPE_COLUMN + " VARCHAR(100) NOT NULL, " + PHRASE_COLUMN + " VARCHAR(200) NOT NULL, " + OWNER_COLUMN + " INT, PRIMARY KEY(id), UNIQUE INDEX " + ID_COLUMN + "_UNIQUE (" + ID_COLUMN + " ASC)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;");
        }
        catch (DataBaseException e) {
            LOGGER.info("table \"" + SCHEME_NAME + "." + TABLE_NAME + "\" is exist");
        }
    }

    @Override
    public String findPhrase(String quote) throws DataBaseException {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE PHRASE = \"" + quote + "\";");
        LOGGER.info("Finding phrase \"" + quote + "\" in (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
        List<PhraseModel> list = PhraseAdapter.getPhraseModels(set, LOGGER);
        if (list != null && !list.isEmpty())
            return list.get(0).getType();
        return null;
    }

    @Override
    public LinkedList<PhraseModel> findType(String type) throws DataBaseException {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE TYPE = \"" + type + "\";");
        LOGGER.info("Getting all rows from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        return PhraseAdapter.getPhraseModels(set, LOGGER);
    }


    /**
     * closing connection from DataBase
     */
    @Override
    public void shutdown() throws DataBaseException {
        try {
            if (util.isOpen()) {
                LOGGER.info("Shutdown phrase connection " + util);
                util.shutdown();
            }
        }
        catch (ConnectorException e) {
            throw new DataBaseException("undefined connection status = " + util, e);
        }
    }

    @Override
    public String toString() {
        return util.toString();
    }
}
