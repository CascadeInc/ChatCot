package by.cascade.chatcot.storage.databaseprocessing.todolists.xml;

import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;

import static by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlListConstants.*;

public class ListXmlAdapter {
    public static String parseToXML(ListModel model, int mask) {
        StringBuilder result = new StringBuilder();
        if ((mask & XMLBEGIN) == XMLBEGIN) {
            result.append(XMLBegin());
        }
        if (model != null) {
            result.append("    <task>\n");
            result.append("        <id>").append(model.getId()).append("</id>\n");
            result.append("        <date>").append(model.getDate()).append("</date>\n");
            result.append("        <text>").append(model.getText()).append("</text>\n");
            result.append("        <description>").append(model.getDescription()).append("</description>\n");
            result.append("        <owner>").append(model.getOwner()).append("</owner>\n");
            result.append("        <check>").append(model.getCheck()).append("</check>\n");
            result.append("    </task>\n");
        }
        if ((mask & XMLEND) == XMLEND){
            result.append(XMLEnd());
        }
        return result.toString();
    }


    public static String parseToXML(ListModel[] models, int mask) {
        StringBuilder result = new StringBuilder();
        if ((mask & XMLBEGIN) == XMLBEGIN) {
            result.append(XMLBegin());
        }
        if (models != null) {
            for (ListModel model : models) {
                if (model != null) {
                    result.append(parseToXML(model, XMLNONE));
                }
            }
        }
        if ((mask & XMLEND) == XMLEND) {
            result.append(XMLEnd());
        }
        return result.toString();
    }
}
