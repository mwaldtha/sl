package sl.interview.communication;

import com.dumbster.smtp.SimpleSmtpServer;
import org.testng.annotations.*;
import sl.interview.exceptions.ConfigurationException;
import javax.mail.MessagingException;

import static org.testng.Assert.*;

public class SMTPMessengerTest {

    private SimpleSmtpServer smtpServer;

    @BeforeClass
    public void setUp() throws Exception {
        smtpServer = SimpleSmtpServer.start(4823);
    }

    @AfterClass
    public void tearDown() throws Exception {
        smtpServer.stop();
    }

    @AfterMethod
    public void cleanUp() throws Exception {
        System.clearProperty("mail_props_file");
    }

    //{property file name, expectException}
    @DataProvider(name = "propsProvider")
    public static Object[][] propFiles() {
        return new Object[][] {
                {"", false},
                {"/non-existent.properties", true},
                {"/bad-from-address.properties", true},
                {"/bad-to-address.properties", true},
                {"/bad-server-value.properties", true},
                {"/incorrect-port.properties", true}
        };
    }

    /**
     * Uses the specified dataProvider to test various valid and invalid properties files.
     *
     * @param propFile Name of the properties file to use for the test
     * @param expectException Whether or not to expect an exception to be encountered
     * @throws Exception
     */
    @Test(dataProvider = "propsProvider")
    public void testSend(String propFile, boolean expectException) throws Exception {
        Messenger messenger = new SMTPMessenger();

        if (!propFile.equals("")) {
            System.setProperty("mail_props_file", propFile);
        }

        try {
            messenger.send();
            if (!expectException) {
                assertTrue(smtpServer.getReceivedEmailSize() == 1);
            } else {
                fail("Expected an exception but didn't get one.");
            }
        } catch (MessagingException | ConfigurationException ex) {
            if (!expectException) {
                fail("Got an exception when one was not expected: " + ex.getMessage());
            }
        }
    }
}