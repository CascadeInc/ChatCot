package by.cascade.chatcot.connector;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;
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
import java.util.LinkedList;

public class GetTasksServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(GetTasksServlet.class);
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
        PhraseAdapter phraseAdapter = null;
        UserAdapter userAdapter = null;
        ListAdapter listAdapter = null;

        try {
            phraseAdapter = new PhrasesMySqlAdapter();
            userAdapter = new UserMySqlAdapter();
            listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());

            String userName = (String) request.getSession().getAttribute("userName");
            if (userName != null) {
                UserModel model = userAdapter.getUser(userName);
                int id = model.getId();
                LinkedList<ListModel> list = listAdapter.listTaskes(id);
                MainServlet.writeJson(response, list);
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
            if (phraseAdapter != null) {
                phraseAdapter.shutdown();
            }
            if (listAdapter != null) {
                listAdapter.shutdown();
            }
        }
    }
}
