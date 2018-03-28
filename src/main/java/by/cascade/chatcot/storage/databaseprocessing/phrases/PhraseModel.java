package by.cascade.chatcot.storage.databaseprocessing.phrases;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhraseModel {
    private static final Logger LOGGER = LogManager.getLogger(PhraseModel.class);

    private int id;
    private String type;
    private String phrase;
    private int owner;

    public PhraseModel(int id, String type, String phrase, int owner) {
        this.id = id;
        this.type = type;
        this.phrase = phrase;
        this.owner = owner;
    }

    public PhraseModel(String type, String phrase, int owner) {
        this(0, type, phrase, owner);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhraseModel that = (PhraseModel) o;
        return id == that.id && (type != null ? type.equals(that.type) : that.type == null) && (phrase != null ? phrase.equals(that.phrase) : that.phrase == null) && (owner == that.owner);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (phrase != null ? phrase.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return id + " : " + type + " : " + phrase;
    }
}
