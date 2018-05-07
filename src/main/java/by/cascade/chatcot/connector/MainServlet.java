package by.cascade.chatcot.connector;

import by.cascade.chatcot.actor.ActionProcessor;
import by.cascade.chatcot.actor.CommandParser;
import by.cascade.chatcot.jsonmodel.CommandJson;
import by.cascade.chatcot.jsonmodel.PhraseInputJson;
import by.cascade.chatcot.jsonmodel.PhraseInputTypeJson;
import by.cascade.chatcot.jsonmodel.PhraseOutJson;
import by.cascade.chatcot.phrases.BotProcessor;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.ListAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.ListModelXmlAdapter;
import by.cascade.chatcot.storage.databaseprocessing.todolists.xml.XmlDomListParser;
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
 * Servlet of main page
 */
public class MainServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(MainServlet.class);
    //private static final String XML_PATH = "WEB-INF/classes/phrases.xml";
    private static final String LIST_XML_PATH = "lists.xml";
    private static final String UNKNOWN_PHRASE = "NEW";
    private PhraseAdapter adapter;
    private ListAdapter listAdapter;
    private BotProcessor botProcessor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doOperation(request, response);
    }

    /**
     * perform operations on main page
     * @param request - request from browser
     * @param response - response to browser
     */
    private void doOperation(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType( "application/json");

            LOGGER.info("realPath = \"" + getServletContext().getRealPath("") + "\"");
            String oldPhrase = (String)getServletContext().getAttribute("oldPhrase");
            String userName = (String)request.getSession().getAttribute("userName");
            int owner = -1;
            if (userName != null) {
                UserAdapter userAdapter = new UserMySqlAdapter();
                UserModel user = userAdapter.getUser(userName);
                if (user != null) {
                    owner = user.getId();
                }
                userAdapter.shutdown();
            }
            botProcessor.open();

            LOGGER.info("trying to saving phrase = \"" + oldPhrase + "\"");
            if (oldPhrase != null) {
                ObjectMapper mapper = new ObjectMapper();
                PhraseInputJson typeJson = mapper.readValue(request.getInputStream(), PhraseInputJson.class);
                String type = typeJson.getQuote();
                LOGGER.info("save phrase = \"" + oldPhrase + "\" with type = \"" + type + "\"");
                botProcessor.save(type, oldPhrase, owner);
                response.setStatus(200);
                LinkedList<PhraseModel> phrases = adapter.findByOwner(userName);
                if (phrases != null && !phrases.isEmpty()) {
                    response.addCookie(new Cookie("userScore", Integer.toString(phrases.size())));
                }
                else {
                    response.addCookie(new Cookie("userScore", Integer.toString(0)));
                }
                getServletContext().setAttribute("oldPhrase", null);
            } else {

                ObjectMapper mapper = new ObjectMapper();
                PhraseInputJson inputJson = mapper.readValue(request.getInputStream(), PhraseInputJson.class);

                String quote = inputJson.getQuote();

                if (quote != null) {
                    LOGGER.info("action");
                    ActionProcessor processor = new ActionProcessor(botProcessor, listAdapter, owner);
                    PhraseOutJson answer = processor.doAction(quote);
                    LOGGER.info("answer = \"" + answer + "\"");
                    if (answer.getAnswer() == null) {
                        LOGGER.info("null answer");
                        answer.setType(0);
                        writeJson(response, answer);
                        getServletContext().setAttribute("oldPhrase", new CommandParser().removeArgs(quote));
                    } else {
                        writeJson(response, answer);
                        getServletContext().setAttribute("oldPhrase", null);
                    }
                }
            }
            botProcessor.close();
        }
        catch (Exception e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * forward to other page
     * @param dispatch - url of forwarding page
     * @param request - request form browser
     * @param response - response to browser
     */
    private void goTo(String dispatch, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(dispatch).forward(request, response);
        } catch (ServletException | IOException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        LOGGER.info("INIT SERVLET------------------------------");
        try {
            adapter = new PhrasesMySqlAdapter();
            botProcessor = BotProcessor.getInstance(adapter);
            adapter.shutdown();
            botProcessor.open();
            listAdapter = new ListModelXmlAdapter(getServletContext().getRealPath("") + LIST_XML_PATH, new XmlDomListParser());
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("DESTROY SERVLET---------------------");
        try {
            adapter.shutdown();
            listAdapter.shutdown();
            botProcessor.shutdown();
        }
        catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
    }

    public static void writeJson(HttpServletResponse response, Object object) throws IOException {
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        out.write(mapper.writeValueAsString(object));
        out.flush();
    }
}
