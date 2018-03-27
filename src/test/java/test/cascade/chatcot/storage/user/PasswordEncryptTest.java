package test.cascade.chatcot.storage.user;

import by.cascade.chatcot.storage.databaseprocessing.user.PasswordEncrypt;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PasswordEncryptTest {

    @Test
    public void testEncrypt() throws Exception {
        String expected = "A54riuhf3iyoiw4ynfoiw4ynouwocoic3y487cy oas yoiueyno46n6f87nicyenoskatvo i304n";
        PasswordEncrypt encrypt = new PasswordEncrypt();
        String actual = encrypt.encrypt(expected);
        Assert.assertEquals(expected, encrypt.encrypt(actual));
    }
}