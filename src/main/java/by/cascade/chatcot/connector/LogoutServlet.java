package by.cascade.chatcot.connector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("userName", null);
        req.getSession().setAttribute("userScore", null);
        resp.addCookie(new Cookie("userName", ""));
        resp.addCookie(new Cookie("userScore", ""));
        resp.setStatus(200);
    }
}
