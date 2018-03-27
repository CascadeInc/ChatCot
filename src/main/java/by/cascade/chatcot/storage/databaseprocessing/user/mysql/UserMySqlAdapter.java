package by.cascade.chatcot.storage.databaseprocessing.user.mysql;

import by.cascade.chatcot.storage.databaseprocessing.user.PasswordEncrypt;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import by.cascade.chatcot.storage.databaseprocessing.util.MySqlUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;

public class UserMySqlAdapter implements UserAdapter {
    private static final Logger LOGGER = LogManager.getLogger(UserMySqlAdapter.class);

    private MySqlUtil util;

    private final String SCHEME_NAME = "usr";
    private final String TABLE_NAME = "users";

    private final String ID_COLUMN = "id";
    private final String NAME_COLUMN = "name";
    private final String PASSWORD_COLUMN = "password";


    public UserMySqlAdapter() {
        util = new MySqlUtil();
        LOGGER.info("creating MySQL Adapter");
    }

    /**
     * closing connection from DataBase
     */
    @Override
    public void shutdown() {
        util.shutdown();
        LOGGER.info("Shutdown connection");
    }

    @Override
    public void create() {
        util.execUpdate("CREATE SCHEMA IF NOT EXISTS " + SCHEME_NAME + " DEFAULT CHARACTER SET utf8;");
        util.execUpdate("CREATE TABLE IF NOT EXISTS " + SCHEME_NAME + "." + TABLE_NAME + " (" + ID_COLUMN + " INT NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " VARCHAR(100) NOT NULL, " + PASSWORD_COLUMN + " VARCHAR(100) NOT NULL, PRIMARY KEY(id), UNIQUE INDEX " + ID_COLUMN + "_UNIQUE (" + ID_COLUMN + " ASC)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;");
    }

    @Override
    public void addUser(String name, String password) {
        PasswordEncrypt encrypt = new PasswordEncrypt();
        util.execUpdate("INSERT INTO " + SCHEME_NAME + "." + TABLE_NAME + "(" + NAME_COLUMN + ", " + PASSWORD_COLUMN + ")" + " VALUES " + "(\"" + name + "\", \"" + encrypt.encrypt(password) + "\");");
        LOGGER.info("Adding new user : (name = " + name + ") into (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME + ")");
    }

    @Override
    public boolean checkUser(String name, String password) {
        PasswordEncrypt encrypt = new PasswordEncrypt();
        ResultSet set = util.execPrepare("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + NAME_COLUMN + " = ? AND " + PASSWORD_COLUMN + " = ?;", name, encrypt.encrypt(password));
        LOGGER.info("Find user by login and password from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME+")");
        LinkedList<UserModel> list = UserAdapter.getPhraseModels(set, LOGGER);
        return (list != null && !list.isEmpty());
    }

    @Override
    public boolean checkLogin(String name) {
        ResultSet set = util.exec("SELECT * FROM " + SCHEME_NAME + "." + TABLE_NAME + " WHERE " + NAME_COLUMN + " = \"" + name + "\" ;");
        LOGGER.info("Find user by login from (scheme = " + SCHEME_NAME + ", table = " + TABLE_NAME);
        LinkedList<UserModel> list = UserAdapter.getPhraseModels(set, LOGGER);
        return (list != null && !list.isEmpty());
    }
}
