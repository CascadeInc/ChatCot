package by.cascade.chatcot.storage.databaseprocessing.phrases.xml;

public class XmlConstants {

    public static final int XMLNONE = 0;
    public static final int XMLBEGIN = 2;
    public static final int XMLEND = 1;

    /**
     * getting the String of Begin of the XML file
     * @return the String of Begin of the XML file
     */
    public static String XMLBegin(){
        return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<phrases>\n");
    }

    /**
     * getting the String of End of the XML file
     * @return the String of End of the XML file
     */
    public static String XMLEnd(){
        return ("</phrases>");
    }
}
