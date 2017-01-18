package sl.interview;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sl.interview.communication.Messenger;
import sl.interview.communication.SMTPMessenger;

import static org.mockito.Mockito.*;
import static org.testng.Assert.fail;

public class ContentComparatorTest {

    //{content file name, previous content length, should an email be expected, should exception be expected}
    @DataProvider(name = "contentProvider")
    public static Object[][] propFiles() {
        return new Object[][] {
                {"", 0, false, true},
                {"file:///non-existent.file", 0, false, true},
                {"/content/a.html", 0, false, false}, //no previous content
                {"/content/a.html", 10, true, false}, //previous content was smaller
                {"/content/a.html", 400, true, false}, //previous content was larger
                {"/content/a.html", 143, false, false} //previous content was equal
        };
    }

    /**
     * Uses the named data provider to test the run method with various content and previous content sizes.
     *
     * @param contentFile Name of the content file to use in this test
     * @param previousContentLength Value to set the previousContentLength field to simulate a previous call having set it
     * @param expectEmail Whether or not to expect an email message to be sent
     * @param expectException Whether or not to expect an exception to be thrown (i.e. when using bad data)
     * @throws Exception
     */
    @Test(dataProvider = "contentProvider")
    public void testRun(String contentFile, int previousContentLength, boolean expectEmail, boolean expectException) throws Exception {
        Messenger mockMessenger = mock(SMTPMessenger.class);

        String filePath = "";

        if (contentFile.isEmpty() || !contentFile.startsWith("/")) {
            filePath = contentFile;
        } else {
            filePath = getClass().getResource(contentFile).toString();
        }
        ContentComparator cc = new ContentComparator(filePath, mockMessenger);
        cc.previousContentLength = previousContentLength;

        try {
            cc.run();

            if (!expectException) {
                if (expectEmail) {
                    verify(mockMessenger).send();
                } else {
                    verify(mockMessenger, never()).send();
                }
            } else {
                fail("Expected an exception but didn't get one.");
            }
        } catch (Exception ex) {
            if (!expectException) {
                fail("Got an exception when one was not expected: " + ex.getMessage());
            }
        }
    }
}