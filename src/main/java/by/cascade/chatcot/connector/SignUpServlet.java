package by.cascade.chatcot.connector;

import by.cascade.chatcot.mail.MailException;
import by.cascade.chatcot.mail.RegistrantKeyMap;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class SignUpServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(SignUpServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doOperation(req, resp);
        } catch (DataBaseException | MailException e) {
            LOGGER.catching(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doOperation(req, resp);
        } catch (DataBaseException | MailException e) {
            LOGGER.catching(e);
        }
    }

    /**
     * performing registration
     *
     * @param request  - request from browser
     * @param response - response to browser
     * @throws ServletException - servlet exception
     * @throws IOException      - exception of write/read
     */
    private void doOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataBaseException, MailException {
        UserAdapter userAdapter;
        try {
            userAdapter = new UserMySqlAdapter();
            String uuid = request.getParameter("uuid");
            UserModel model = RegistrantKeyMap.getInstance().continueRegister(uuid);
            if (model != null) {
                userAdapter.addUser(model.getName(), model.getEmail(), model.getPassword(), "user");
                request.getSession().setAttribute("userName", model.getName());
                PhraseAdapter phraseAdapter = new PhrasesMySqlAdapter();
                LinkedList<PhraseModel> phrases = phraseAdapter.findByOwner(model.getName());
                if (phrases != null && !phrases.isEmpty()) {
                    response.addCookie(new Cookie("userScore", Integer.toString(phrases.size())));
                    response.setStatus(200);
                } else {
                    response.addCookie(new Cookie("userScore", Integer.toString(0)));
                    response.setStatus(200);
                }
            }
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
    }
}
