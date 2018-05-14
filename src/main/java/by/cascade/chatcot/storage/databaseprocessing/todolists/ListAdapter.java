package by.cascade.chatcot.storage.databaseprocessing.todolists;

import by.cascade.chatcot.jsonmodel.TaskWithIdJson;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public interface ListAdapter {
    int addTask(String text, String description, Date date, int user);
    LinkedList<ListModel> listTaskes(int owner);
    LinkedList<ListModel> filterTaskes(Date since, Date until, int owner);
    LinkedList<ListModel> filterTaskesByCheck(Boolean check, int owner);
    void deleteTaskes(List<ListModel> list);
    void deleteId(ListModel model);
    void deleteIdOwner(int id, int owner);
    void deleteTask(ListModel model);
    LinkedList<ListModel> findTasks();
    void changeTask(TaskWithIdJson json, int owner);
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
                    boolean check = set.getBoolean(6);
                    list.add(new ListModel(id, date, text, description, user, check));
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
