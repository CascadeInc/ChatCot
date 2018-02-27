package by.cascade.chatcot.storage.databaseprocessing.todolists;

import java.util.Date;

public class ListModel {
    private int id;
    private Date date;
    private String text;
    private String description;
    private int owner;

    public ListModel(int id, Date date, String text, String description, int owner) {
        this.date = date;
        this.text = text;
        this.description = description;
        this.owner = owner;
    }

    public ListModel(int id, Date date, String text, int owner) {
        this(id, date, text, "", owner);
    }

    public ListModel(int id, Date date, String text) {
        this(id, date, text, "", 0);
    }

    public ListModel(int id, Date date, String text, String description) {
        this(id, date, text, description, 0);
    }


    public int getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public String getDescription() {
        return description;
    }
    public int getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "(id = " + id + "; date = (" + date + "); text = \"" + text + "\"; description = \"" + description + "\"; owner = " + owner + ")";
    }
}
