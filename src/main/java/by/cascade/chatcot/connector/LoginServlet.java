package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.UserLoginJson;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

            ObjectMapper mapper = new ObjectMapper();
            UserLoginJson userLoginJson = mapper.readValue(request.getInputStream(), UserLoginJson.class);

            String login = userLoginJson.getName();
            String password = userLoginJson.getPassword();

            UserModel userModel = userAdapter.checkUser(login, password);

            if (userModel != null) {
                LOGGER.debug("login is successfully: " + login);
                request.getSession().setAttribute("userName", userModel.getName());

                PhraseAdapter phraseAdapter = new PhrasesMySqlAdapter();
                LinkedList<PhraseModel> phrases = phraseAdapter.findByOwner(userModel.getName());
                if (phrases != null && !phrases.isEmpty()) {
                    response.addCookie(new Cookie("userScore", Integer.toString(phrases.size())));
                } else {
                    response.addCookie(new Cookie("userScore", Integer.toString(0)));
                }
                response.addCookie(new Cookie("userName", userModel.getName()));
                response.addCookie(new Cookie("userRole", userModel.getRole()));
                response.setStatus(200);
            } else {
                LOGGER.debug("login is not successfully: " + login);
                response.setStatus(401);
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
    }
}
