package test.cascade.chatcot.storage.user;

import by.cascade.chatcot.storage.databaseprocessing.todolists.ListModel;
import by.cascade.chatcot.storage.databaseprocessing.user.PasswordEncrypt;
import by.cascade.chatcot.storage.databaseprocessing.user.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Date;

import static org.testng.Assert.*;

public class PasswordEncryptTest {
    private final static String BASE_FILE = "todo.json";

    @Test
    public void testEncrypt() throws Exception {
        String expected = "A54riuhf3iyoiw4ynfoiw4ynouwocoic3y487cy oas yoiueyno46n6f87nicyenoskatvo i304n";
        PasswordEncrypt encrypt = new PasswordEncrypt();
        String actual = encrypt.encrypt(expected);
        Assert.assertEquals(expected, encrypt.encrypt(actual));
    }

    @Test
    public void testJson() throws Exception {
        ListModel listModel = new ListModel(1, new Date(23), "do sprint", "for Iskra N. A.", 2, false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(BASE_FILE), listModel);
        System.out.println("json created!");

        ListModel readModel = mapper.readValue(new File(BASE_FILE), ListModel.class);
        System.out.println(readModel);
    }
}