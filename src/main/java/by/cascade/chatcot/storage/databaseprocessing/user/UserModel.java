package by.cascade.chatcot.storage.databaseprocessing.user;

public class UserModel {
    private int id;
    private String name;
    private String password;

    public UserModel(int id, String name, String password) {
        this.id = id;
        this.name = name;
        PasswordEncrypt encrypt = new PasswordEncrypt();
        this.password = encrypt.encrypt(password);
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean checkPassword(String password) {
        PasswordEncrypt encrypt = new PasswordEncrypt();
        return (encrypt.encrypt(password).equals(this.password));
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "(id = " + id + "; name = \"" + name + "\")";
    }
}
