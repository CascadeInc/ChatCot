package by.cascade.chatcot.connector;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import by.cascade.chatcot.view.PhraseView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AdminServlet.class);

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
     *
     *
     * @param request  - request from browser
     * @param response - response to browser
     * @throws ServletException - exception of servlet
     * @throws IOException      - exception for writing/reading streams
     */
    private void doOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataBaseException {
        UserAdapter userAdapter = null;
        PhraseAdapter phraseAdapter = null;
        try {
            userAdapter = new UserMySqlAdapter();
            phraseAdapter = new PhrasesMySqlAdapter();
            LinkedList<PhraseModel> phrases = phraseAdapter.listPhrases();
            LinkedList<PhraseView> result = new LinkedList<PhraseView>();
            if (phrases != null) {
                for (PhraseModel phrase : phrases) {
                    UserModel model = userAdapter.getUserById(phrase.getOwner());
                    if (model != null) {
                        result.add(new PhraseView(phrase.getId(), phrase.getPhrase(), phrase.getType(), phrase.getDate(), model.getName()));
                    }
                }
            }
            MainServlet.writeJson(response, result);
            response.setStatus(200);
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