package by.cascade.chatcot.storage.databaseprocessing.phrases.xml;

import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;

public interface PhraseParsable {
    PhraseModel[] parseFromXML(String path);
    PhraseModel parseFromXML(String path, int index);
}