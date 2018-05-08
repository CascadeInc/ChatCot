package by.cascade.chatcot.connector;

import by.cascade.chatcot.jsonmodel.EditPhraseJson;
import by.cascade.chatcot.jsonmodel.PhraseDeleteJson;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseAdapter;
import by.cascade.chatcot.storage.databaseprocessing.phrases.PhraseModel;
import by.cascade.chatcot.storage.databaseprocessing.phrases.mysql.PhrasesMySqlAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(EditServlet.class);

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
        ObjectMapper mapper = new ObjectMapper();
        PhraseDeleteJson phraseJson = mapper.readValue(request.getInputStream(), PhraseDeleteJson.class);

        String phraseItem = phraseJson.getId();
        int id  = Integer.valueOf(phraseItem);
        try {
            phraseAdapter = new PhrasesMySqlAdapter();
            PhraseModel phraseModel = phraseAdapter.findPhraseById(id);
            if (phraseModel != null) {
                EditPhraseJson editJson = new EditPhraseJson();
                editJson.setNewPhraseText(phraseModel.getPhrase());
                editJson.setNewType(phraseModel.getType());
                editJson.setId(Integer.toString(id));

                MainServlet.writeJson(response, editJson);
                response.setStatus(200);
            }
            else {
                response.setStatus(401);
            }
        } catch (DataBaseException e) {
            LOGGER.catching(e);
            throw new RuntimeException(e);
        }
        finally {
            if (phraseAdapter != null) {
                phraseAdapter.shutdown();
            }
        }
    }
}
