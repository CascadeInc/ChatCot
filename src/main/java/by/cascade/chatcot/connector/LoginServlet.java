package by.cascade.chatcot.connector;

import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doOperation(req, resp);
    }



    private void doOperation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAdapter userAdapter = new UserMySqlAdapter();

        String login = request.getParameter("login");
        String password = request.getParameter("password");


        boolean check = userAdapter.checkUser(login, password);
        userAdapter.shutdown();

        if (check) {
            request.setAttribute("loginResult", "Login is successfully");
        }
        else {
            request.setAttribute("loginResult","Login is not successfully");
        }

        userAdapter.shutdown();
        request.getRequestDispatcher("/").forward(request, response);
    }
}
