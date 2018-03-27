package by.cascade.chatcot.storage.databaseprocessing.todolists;

import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public interface ListAdapter {
    void addTask(String text, String description, int user);
    LinkedList<ListModel> listTaskes();
    LinkedList<ListModel> filterTaskes(Date since, Date until);
    void deleteTaskes(List<ListModel> list);
    void deleteId(ListModel model);
    void deleteTask(ListModel model);
    LinkedList<ListModel> findTasks();
    void shutdown();
    void create();

    static LinkedList<ListModel> getTaskesModels(ResultSet set, Logger log) {
        try {
            if (set != null) {
                LinkedList<ListModel> list = new LinkedList<>();
                while (set.next()) {
                    int id = set.getInt(1);
                    Date date = set.getDate(2);
                    String text = set.getString(3);
                    String description = set.getString(4);
                    int user = set.getInt(5);
                    list.add(new ListModel(id, date, text, description, user));
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
