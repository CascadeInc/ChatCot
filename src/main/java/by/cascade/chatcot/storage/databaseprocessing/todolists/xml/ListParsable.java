package by.cascade.chatcot.storage.databaseprocessing.todolists.xml;

import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;

public interface ListParsable {

    ListModel[] parseFromXML(String path);
    ListModel parseFromXML(String path, int index);
}
