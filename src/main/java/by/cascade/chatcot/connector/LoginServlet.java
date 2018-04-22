package by.cascade.chatcot.connector;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Servlet of login system
 */
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
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
     * perform login operation
     *
     * @param request  - request from browser
     * @param response - response to browser
     * @throws ServletException - exception of servlet
     * @throws IOException      - exception for writing/reading streams
     */
    private void doOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataBaseException {
        UserAdapter userAdapter = null;
        try {
            userAdapter = new UserMySqlAdapter();

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            UserModel userModel = userAdapter.checkUser(login, password);

            if (userModel != null) {
                LOGGER.debug("login is successfully: " + login);
                request.getSession().setAttribute("userName", userModel.getName());
                PhraseAdapter phraseAdapter = new PhrasesMySqlAdapter();
                LinkedList<PhraseModel> phrases = phraseAdapter.findByOwner(userModel.getName());
                if (phrases != null && !phrases.isEmpty()) {
                    request.getSession().setAttribute("userScore", Integer.toString(phrases.size()));
                }
                else {
                    request.getSession().setAttribute("userScore", Integer.toString(0));
                }
                request.setAttribute("loginResult", "Login is successfully");
            } else {
                LOGGER.debug("login is not successfully: " + login);
                request.setAttribute("loginResult", "Login is not successfully");
            }
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
        finally {
            if (userAdapter != null) {
                userAdapter.shutdown();
            }
        }
        request.getRequestDispatcher("/").forward(request, response);
    }
}
