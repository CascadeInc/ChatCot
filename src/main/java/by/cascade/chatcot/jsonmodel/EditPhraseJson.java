package by.cascade.chatcot.jsonmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditPhraseJson {
    @JsonProperty("phraseTextNew")
    private String newPhraseText;
    @JsonProperty("choiceType")
    private String newType;
    @JsonProperty("phraseItemId")
    private String id;

    public String getNewPhraseText() {
        return newPhraseText;
    }

    public void setNewPhraseText(String newPhraseText) {
        this.newPhraseText = newPhraseText;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
