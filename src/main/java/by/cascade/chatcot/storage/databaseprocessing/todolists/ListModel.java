package by.cascade.chatcot.storage.databaseprocessing.todolists;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ListModel {
    @JsonProperty("id")
    private int id;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("todo")
    private String text;
    @JsonProperty("description")
    private String description;
    @JsonProperty("check")
    private boolean check;
    @JsonIgnore
    private int owner;

    public ListModel() {
    }

    public ListModel(int id, Date date, String text, String description, int owner, boolean check) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.description = description;
        this.owner = owner;
        this.check = check;
    }

    public ListModel(int id, Date date, String text, int owner) {
        this(id, date, text, "", owner, false);
    }

    public ListModel(int id, Date date, String text) {
        this(id, date, text, "", 0, false);
    }

    public ListModel(int id, Date date, String text, String description) {
        this(id, date, text, description, 0, false);
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
    public boolean getCheck() {
        return check;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "(id = " + id + "; date = (" + date + "); text = \"" + text + "\"; description = \"" + description + "\"; owner = " + owner + "; check = " + check + ")";
    }

    public String toHTML() {
        return "<tr>" +
                "<th>" + getId() + "</th>" +
                "<th>" + getDate() +"</th>" +
                "<th>" + getText() +"</th>" +
                "<th>" + getDescription() + "</th>" +
                "<th>" + getCheck() + "</th>" +
                "</tr>";
    }

    public static String getHtmlTableHeader() {
        return  "<tr>" +
                "<th>" + "id" + "</th>" +
                "<th>" + "date" + "</th>" +
                "<th>" + "text" + "</th>" +
                "<th>" + "description" + "</th>" +
                "<th>" + "check" + "</th>" +
                "</tr>";
    }
}
