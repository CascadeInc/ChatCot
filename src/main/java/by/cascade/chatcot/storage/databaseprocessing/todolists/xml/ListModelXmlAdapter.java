package by.cascade.chatcot.storage.databaseprocessing.todolists.xml;

import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.*;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlListConstants.*;

public class ListModelXmlAdapter implements ListAdapter {
    private static final Logger LOGGER = LogManager.getLogger(ListModelXmlAdapter.class);

    private ListParsable parser;
    private String path;

    private static final String prefix = "temp";


    /**
     * Create XmlAdapter for working with phrases by using XML file
     * @param path - path of the XML file
     * @param parser - used parser
     */
    public ListModelXmlAdapter(String path, ListParsable parser) {
        this.path = path;
        this.parser = parser;
    }

    /**
     * adding new phrase into XML file
     * @param list - phrase for inserting
     * @param index - index to insert
     */
    public void add(ListModel list, int index) {
        LOGGER.info("adding " + list.toString() + " to XML file to index = " + index);
        ListModel lists[] = parser.parseFromXML(path);
        if (lists != null) {
            if (index < 0 || index > lists.length) {
                LOGGER.info("invalid index");
                return;
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(path, false);
            LOGGER.info("adding begin of XML file");
            fileWriter.write(ListXmlAdapter.parseToXML((ListModel) null, XMLBEGIN));
            if (lists != null && index <= lists.length) {
                for (int i = 0; i < index; i++) {
                    fileWriter.append(ListXmlAdapter.parseToXML(lists[i], XMLNONE));
                }
            }
            if (lists != null) {
                LOGGER.info("setting list id = " + lists.length);
                list.setId(lists.length);
            }
            else {
                LOGGER.info("setting list id = 0");
                list.setId(0);
            }
            fileWriter.append(ListXmlAdapter.parseToXML(list, XMLNONE));
            if (lists != null && index < lists.length) {
                for (int i = index; i < lists.length; i++) {
                    fileWriter.append(ListXmlAdapter.parseToXML(lists[i], XMLNONE));
                }
            }
            LOGGER.info("adding end of XML file");
            fileWriter.append(ListXmlAdapter.parseToXML((ListModel) null, XMLEND));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * adding new phrase at begin of XML file
     * @param phrase - phrase for inserting
     */
    public void addInBegin(ListModel phrase) {
        LOGGER.info("adding in begin");
        add(phrase, 0);
    }

    /**
     * adding new phrase at end of XML file
     * @param phrase - phrase for inserting
     */
    public void addInEnd(ListModel phrase) {
        LOGGER.info("adding in end");
        if (parser.parseFromXML(path) != null) {
            this.add(phrase, parser.parseFromXML(path).length);
        } else {
            this.add(phrase, 0);
        }
    }

    /**
     * deleting phrase by index from XML file
     * @param index - index of deleting phrase in XML file
     * @return - true - if deleting was completed
     *         - false - if deleting wasn't completed
     */
    public boolean delete(int index) {
        try {
            LOGGER.info("deleting from index = " + index);
            ListModel lists[] = parser.parseFromXML(path);
            if (lists != null && index >= 0 && index < lists.length) {
                FileWriter fileWriter = new FileWriter(prefix + path, false);
                fileWriter.write(ListXmlAdapter.parseToXML((ListModel) null, XMLBEGIN));
                if (index >= lists.length) {
                    return false;
                }
                for (int i = 0; i < index; i++) {
                    fileWriter.append(ListXmlAdapter.parseToXML(lists[i], XMLNONE));
                }
                for (int i = index + 1; i < lists.length; i++) {
                    lists[i].setId(lists[i].getId() - 1);
                    fileWriter.append(ListXmlAdapter.parseToXML(lists[i], XMLNONE));
                }
                fileWriter.append(ListXmlAdapter.parseToXML((ListModel) null, XMLEND));
                fileWriter.flush();
                fileWriter.close();

                LOGGER.info("create temp file");
                File file = new File(prefix + path);
                File oldFile = new File(path);
                if (oldFile.delete()) {
                    if (!file.renameTo(oldFile)) {
                        LOGGER.error("Can't rename file");
                    }
                } else {
                    LOGGER.error("Can't delete file");
                }
                return true;
            } else {
                if (lists == null) {
                    File file = new File(path);
                    LOGGER.info("delete file");
                    file.delete();
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    /**
     * editing phrase in XML file
     * @param list - new phrase from editing
     * @param index - index to edit
     */
    public void edit(ListModel list, int index) {
        LOGGER.info("edit index = " + index);
        int old = get(index).getId();
        if (delete(index)) {
            list.setId(old);
            add(list, index);
        }
    }


    /**
     * getting phrase from index in XML file
     * @param index - index of phrase in XML file
     * @return - selected phrase
     */
    public ListModel get(int index) {
        return parser.parseFromXML(path, index);
    }


    /**
     * getting list of phrases
     * @return - list of all phrases from XML file
     */
    public LinkedList<ListModel> getList() {
        LOGGER.info("getting list from XML");
        LinkedList<ListModel> list = new LinkedList<>();
        ListModel task;
        int index = 0;
        do {
            task = parser.parseFromXML(path, index++);
            if (task != null) {
                list.add(task);
            }
        } while (task != null);
        return list;
    }

    /**
     * getting length of XML file
     * @return - count of phrases into XML file
     */
    public int length() {
        ListModel[] phrases = parser.parseFromXML(path);
        if (phrases != null) {
            return phrases.length;
        } else {
            return 0;
        }
    }

    /**
     * transform all phrases of XML file into String
     * @return - all phrases of XML file
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        ListModel[] tasks = parser.parseFromXML(path);
        if (tasks != null) {
            for (ListModel task : parser.parseFromXML(path)) {
                if (task != null) {
                    s.append(task.toString());
                }
            }
        }
        return s.toString();
    }

    /**
     * getting path of current XML file
     * @return - path of XML file
     */
    public String getPath() {
        return path;
    }

    /**
     * setting path of XML file
     * @param path - path of XML file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * setting parser for working with XML file
     * @param parser - Object, that implements Parserable
     */
    public void setParser(ListParsable parser) {
        this.parser = parser;
    }




    @Override
    public void addTask(String text, String description, int user) {
        LOGGER.info("adding new task: \"" + text + "\"");
        addInEnd(new ListModel(0, new Date(), text, description, user));
    }

    @Override
    public LinkedList<ListModel> listTaskes() {
        LOGGER.info("getting list of tasks");
        return getList();
    }

    @Override
    public LinkedList<ListModel> filterTaskes(Date since, Date until) {
        LOGGER.info("getting filtered tasks");
        LinkedList<ListModel> list = new LinkedList<>();
        ListModel task;
        int index = 0;
        do {
            task = parser.parseFromXML(path, index++);
            if (task != null && task.getDate().after(since) && task.getDate().before(until)) {
                list.add(task);
            }
        } while (task != null);
        return list;
    }

    @Override
    public void deleteTaskes(List<ListModel> list) {
        LOGGER.info("deleting tasks from list");
        list.sort((o1, o2) -> Integer.compare(o2.getId(), o1.getId()));
        for (int i = length() - 1; i >= 0; i--) {
            deleteId(list.get(i));
        }
    }

    @Override
    public void deleteId(ListModel model) {
        LOGGER.info("getting task by id");
        delete(model.getId());
    }

    @Override
    public void deleteTask(ListModel model) {
        LOGGER.info("getting task");
        deleteId(model);
    }

    @Override
    public LinkedList<ListModel> findTasks() {
        LOGGER.info("find tasks");
        return listTaskes();
    }

    /**
     * close application action
     */
    @Override
    public void shutdown() {
    }

    /**
     * creating XML file
     */
    @Override
    public void create() {
        LOGGER.info("creating XML file for path: \"" + path + "\"");
        try {
            FileWriter fileWriter = new FileWriter(path, false);
            fileWriter.write(ListXmlAdapter.parseToXML((ListModel) null, XMLBEGIN));
            fileWriter.write(ListXmlAdapter.parseToXML((ListModel) null, XMLEND));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
