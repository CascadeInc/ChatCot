package by.cascade.chatcot.storage.databaseprocessing.todolists.xml;

import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class XmlDomListParser implements ListParsable {
    private static final Logger LOGGER = LogManager.getLogger(XmlDomListParser.class);

    private static final String DATE_FORMAT = "EEE MMM dd hh:mm:ss z yyyy";

    public ListModel[] parseFromXML(String path) {
        try {
            File inputFile = new File(path);
            if (!inputFile.exists()) {
                LOGGER.info("file doesn't exist");
                return null;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("task");
            ListModel[] people;
            if (nList.getLength() > 0) {
                people = new ListModel[nList.getLength()];
            }
            else {
                people = null;
            }
            for (int temp = 0; temp < nList.getLength(); temp++) {
                if (people != null) {
                    people[temp] = parseFromXML(path, temp);
                }
            }
            return people;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return null;
    }


    public ListModel parseFromXML(String path, int index) {
        try {
            int id = 0;
            Date date = null;
            String text = null;
            String description = null;
            int owner = 0;
            boolean check = false;

            File inputFile = new File(path);
            if (!inputFile.exists()) {
                LOGGER.info("file doesn't exists");
                return null;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("task");
            if (index < 0 || index >= nList.getLength()) {
                return null;
            }
            Node nNode = nList.item(index);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                String idString = getXMLArgument(nNode, "id");
                if (idString != null) {
                    id = Integer.valueOf(idString);
                }
                else {
                    id = 0;
                }
                String dateString = getXMLArgument(nNode, "date");
                if (dateString != null) {
                    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                    date = format.parse(dateString);
                }
                else {
                    date = new Date();
                }
                text = getXMLArgument(nNode, "text");
                description = getXMLArgument(nNode, "description");

                String ownerString = getXMLArgument(nNode, "owner");
                if (ownerString != null) {
                    owner = Integer.valueOf(ownerString);
                }

                String checkString = getXMLArgument(nNode, "check");
                if (checkString != null) {
                    check = Boolean.valueOf(checkString);
                }
                else {
                    owner = 0;
                }
            }
            return new ListModel(id, date, text, description, owner, check);
        } catch (Exception e) {
            LOGGER.catching(e);
            return null;
        }
    }



    private static String getXMLArgument(Node nNode, String attribute) {
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;
            return eElement.getElementsByTagName(attribute).item(0).getTextContent();
        }
        return null;
    }
}
