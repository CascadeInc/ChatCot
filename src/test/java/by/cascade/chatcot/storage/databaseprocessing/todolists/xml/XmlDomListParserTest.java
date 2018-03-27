package by.cascade.chatcot.storage.databaseprocessing.todolists.xml;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.testng.Assert.*;

public class XmlDomListParserTest {
    private static final String DATE_FORMAT = "EEE MMM dd hh:mm:ss z yyyy";

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseFromXML() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date date = new Date();
        System.out.println(date.toString());
        System.out.println(format.format(date));
        Assert.assertEquals(date.toString(),format.format(date));
    }
}