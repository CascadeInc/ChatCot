package by.cascade.chatcot.connector;

import by.cascade.chatcot.actor.ActionProcessor;
import by.cascade.chatcot.actor.CommandParser;
import by.cascade.chatcot.phrases.BotProcessor;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.MySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.xml.XmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.xml.XmlDomParser;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.ListModelXmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlDomListParser;
import by.cascade.chatcot.storage.databaseprocessing.user.UserAdapter;
import by.cascade.chatcot.storage.databaseprocessing.user.mysql.UserMySqlAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

public class MainServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(MainServlet.class);
    private static final String XML_PATH = "WEB-INF/classes/phrases.xml";
    private static final String LIST_XML_PATH = "lists.xml";
    private static final String UNKNOWN_PHRASE = "NEW";
    private PhraseAdapter adapter;
    private ListAdapter listAdapter;
    private BotProcessor botProcessor;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOperation(request, response);
    }

    private void doOperation(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.catching(e);
        }

        LOGGER.info("realPath = \"" + getServletContext().getRealPath("") + "\"");
        String oldPhrase = (String) getServletContext().getAttribute("oldPhrase");

        LOGGER.info("trying to saving phrase = \"" + oldPhrase + "\"");
        if (oldPhrase != null) {
            String type = request.getParameter("choiceType");
            LOGGER.info("save phrase = \"" + oldPhrase + "\" with type = \"" + type +"\"");
            botProcessor.save(type, oldPhrase);
            getServletContext().setAttribute("oldPhrase", null);
        }
        else {
            String quote = request.getParameter("quote");
            if (quote != null) {
                LOGGER.info("action");
                ActionProcessor processor = new ActionProcessor(botProcessor, listAdapter);
                String answer = processor.doAction(quote);
                LOGGER.info("answer = \"" + answer + "\"");
                if (answer == null) {
                    LOGGER.info("null answer");
                    request.setAttribute("resp", "unknown phrase");
                    request.setAttribute("answer", UNKNOWN_PHRASE);
                    getServletContext().setAttribute("oldPhrase", new CommandParser().removeArgs(quote));
                } else {
                    request.setAttribute("answer", answer);
                    getServletContext().setAttribute("oldPhrase", null);
                }
            }
        }
        goTo("/index.jsp", request, response);
    }

    public void goTo(String dispatch, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(dispatch).forward(request, response);
        } catch (ServletException | IOException e) {
            LOGGER.catching(e);
        }
    }

    @Override
    public void init() throws ServletException {
        adapter = new MySqlAdapter();
        botProcessor = BotProcessor.getInstance(adapter);
        listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());
        UserAdapter userAdapter = new UserMySqlAdapter();
        userAdapter.create();
        userAdapter.shutdown();
    }

    @Override
    public void destroy() {
        adapter.shutdown();
        listAdapter.shutdown();
        botProcessor.shutdown();
    }
}
