package by.cascade.chatcot.storage.databaseprocessing.phrases;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for core transactions.
 * Can be realised by :
 * + MySQL
 * - SQLite
 * - XML
 * - file
 */
public interface PhraseAdapter {
    void addPhrase(String type, String phrase, int owner) throws DataBaseException;
    List<PhraseModel> listPhrases() throws DataBaseException;
    void deletePhrases(List<PhraseModel> list) throws DataBaseException;
    void deleteId(PhraseModel model) throws DataBaseException;
    void deletePhrase(PhraseModel model) throws DataBaseException;
    void deleteModel(PhraseModel model) throws DataBaseException;
    void shutdown() throws DataBaseException;
    void create() throws DataBaseException;
    String findPhrase(String quote) throws DataBaseException;
    LinkedList<PhraseModel> findType(String type) throws DataBaseException;
    void refresh();

    static LinkedList<PhraseModel> getPhraseModels(ResultSet set, Logger log) {
        try {
            if (set != null) {
                LinkedList<PhraseModel> list = new LinkedList<PhraseModel>();
                while (set.next()) {
                    int id = set.getInt(1);
                    String type = set.getString(2);
                    String phrase = set.getString(3);
                    int owner = set.getInt(4);
                    list.add(new PhraseModel(id, type, phrase, owner));
                }
                set.close();
                return list;
            }
        }
        catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
