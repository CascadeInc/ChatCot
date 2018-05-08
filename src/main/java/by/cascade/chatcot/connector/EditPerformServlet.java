package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.EditPhraseJson;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditPerformServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(EditPerformServlet.class);

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

        String userName = (String) request.getSession().getAttribute("userName");
        UserAdapter userAdapter = new UserMySqlAdapter();
        String userRole = userAdapter.getUser(userName).getRole();
        try {
            if (userName != null && userRole.equals("admin")) {
                phraseAdapter = new PhrasesMySqlAdapter();

                ObjectMapper mapper = new ObjectMapper();
                EditPhraseJson editJson = mapper.readValue(request.getInputStream(), EditPhraseJson.class);

                String newPhraseText = editJson.getNewPhraseText();
                String newType = editJson.getNewType();
                String id = editJson.getId();

                phraseAdapter.editPhrase(Integer.valueOf(id), newPhraseText, newType);
            } else response.setStatus(401);
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
        finally {
            if (phraseAdapter != null) {
                phraseAdapter.shutdown();
            }
        }
    }
}
