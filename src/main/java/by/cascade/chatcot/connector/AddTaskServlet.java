package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.TaskDeleteJson;
import by.cascade.chatcot.jsonmodel.TaskJson;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.ListModelXmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlDomListParser;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddTaskServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AddTaskServlet.class);
    private static final String LIST_XML_PATH = "lists.xml";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doOperation(req, resp);
        }
        catch (DataBaseException e) {
            LOGGER.catching(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doOperation(req, resp);
        }
        catch (DataBaseException e) {
            LOGGER.catching(e);
        }
    }

    private void doOperation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, DataBaseException {
        UserAdapter userAdapter = null;
        ListAdapter listAdapter = null;

        try {
            userAdapter = new UserMySqlAdapter();
            listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());
            String userName = (String) req.getSession().getAttribute("userName");
            if (userName != null) {
                ObjectMapper mapper = new ObjectMapper();

                TaskJson task = mapper.readValue(req.getInputStream(), TaskJson.class);
                UserModel model = userAdapter.getUser(userName);
                int id = model.getId();

                int resId = listAdapter.addTask(task.getDescription(), task.getDescription(), task.getDate(), id);
                resp.setStatus(200);
                TaskDeleteJson taskDeleteJson = new TaskDeleteJson();
                taskDeleteJson.setId(resId);
                MainServlet.writeJson(resp, taskDeleteJson);
            }
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
        finally {
            if (userAdapter != null) {
                userAdapter.shutdown();
            }
            if (listAdapter != null) {
                listAdapter.shutdown();
            }
        }
    }
}
