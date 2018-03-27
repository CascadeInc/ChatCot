package by.cascade.chatcot.storage.databaseprocessing.user;

import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public interface UserAdapter {
    void addUser(String name, String password);
    boolean checkUser(String name, String password);
    boolean checkLogin(String name);
    void shutdown();
    void create();

    static LinkedList<UserModel> getPhraseModels(ResultSet set, Logger log) {
        try {
            if (set != null) {
                LinkedList<UserModel> list = new LinkedList<UserModel>();
                while (set.next()) {
                    int id = set.getInt(1);
                    String name = set.getString(2);
                    String password = set.getString(3);
                    list.add(new UserModel(id, name, password));
                }
                set.close();
                return list;
            }
        }
        catch (SQLException e) {
            log.catching(e);
        }
        return null;
    }
}
