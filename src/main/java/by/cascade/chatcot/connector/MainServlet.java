package by.cascade.chatcot.connector;

import by.cascade.chatcot.actor.ActionProcessor;
import by.cascade.chatcot.phrases.BotProcessor;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.xml.XmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.xml.XmlDomParser;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.ListModelXmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlDomListParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(MainServlet.class);
    private static final String XML_PATH = "phrases.xml";
    private static final String LIST_XML_PATH = "lists.xml";
    private PhraseAdapter adapter;
    private ListAdapter listAdapter;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOperation(request, response);
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
        adapter = new XmlAdapter(getServletContext().getRealPath("") + XML_PATH, new XmlDomParser());
        BotProcessor botProcessor = BotProcessor.getInstance(adapter);
        listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());
        String quote = request.getParameter("quote");
        ActionProcessor processor = new ActionProcessor(botProcessor, listAdapter);
        String answer = processor.doAction(quote);
        LOGGER.info("answer = \"" + answer + "\"");
        if (answer == null) {
            LOGGER.info("null answer");
            request.setAttribute("resp", "unknown phrase");
        }
        request.setAttribute("answer", answer);
        try {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            LOGGER.catching(e);
        }
    }
}
