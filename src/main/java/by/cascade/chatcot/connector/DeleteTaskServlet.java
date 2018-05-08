package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.TaskDeleteJson;
import by.cascade.chatcot.jsonmodel.TaskWithIdJson;
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

public class DeleteTaskServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteTaskServlet.class);
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

    /**
     * @param request  - request from browser
     * @param response - response to browser
     * @throws ServletException - exception of servlet
     * @throws IOException      - exception for writing/reading streams
     */
    private void doOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataBaseException {
        UserAdapter userAdapter = null;
        ListAdapter listAdapter = null;

        try {
            userAdapter = new UserMySqlAdapter();
            listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());
            String userName = (String) request.getSession().getAttribute("userName");
            if (userName != null) {
                ObjectMapper mapper = new ObjectMapper();

                TaskDeleteJson taskJson = mapper.readValue(request.getInputStream(), TaskDeleteJson.class);

                UserModel model = userAdapter.getUser(userName);
                int id = model.getId();
                listAdapter.deleteIdOwner(taskJson.getId(), id);
                response.setStatus(200);
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
