package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.CommandJson;
import by.cascade.chatcot.jsonmodel.UserLoginJson;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
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

public class AddPerformServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AddPerformServlet.class);

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

        try {
            phraseAdapter = new PhrasesMySqlAdapter();
            userAdapter = new UserMySqlAdapter();

            ObjectMapper mapper = new ObjectMapper();
            CommandJson commandJson = mapper.readValue(request.getInputStream(), CommandJson.class);

            String newPhraseText = commandJson.getNewPhraseText();
            String newType = commandJson.getNewType();


            phraseAdapter.addPhrase(newType, newPhraseText, userAdapter.getUser((String) request.getSession().getAttribute("userName")).getId());
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
        }
    }
}
