package by.cascade.chatcot.storage.databaseprocessing.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserModel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
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
