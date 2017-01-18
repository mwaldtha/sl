package sl.interview;

import sl.interview.communication.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Represents a task to compare content retrieved from a URL with the previously retrieved content
 */
public class ContentComparator implements Runnable {
    protected int previousContentLength = 0;  //exposed for testability
    private int allowedPercentChange = 10;
    private final String _monitorUrl;
    private final Messenger _messenger;

    public ContentComparator(String monitorUrl, Messenger messenger) {
        this._monitorUrl = monitorUrl;
        this._messenger = messenger;
    }

    /**
     * Implementation of the Runnable.run() method.
     * Gets content from the specified URL and compares it to the previous
     */
    public void run() {
        try {
            System.out.printf("Comparing content at %d%n", new Date().getTime());

            URL pageUrl = new URL(this._monitorUrl);
            URLConnection pageConnection = pageUrl.openConnection();

            //read all the bytes to determine the content length
            InputStream contentStream = pageConnection.getInputStream();

            if (contentStream != null) {
                ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
                int bytesRead;
                byte[] contentBytes = new byte[1024];

                while ((bytesRead = contentStream.read(contentBytes, 0, contentBytes.length)) != -1) {
                    contentBuffer.write(contentBytes, 0, bytesRead);
                }

                int contentLength = contentBuffer.size();

                if (previousContentLength != 0) {
                    int allowedChangeAmt = (int) (previousContentLength * (allowedPercentChange / 100.0));

                    //send email if contentLength is outside of the acceptable range
                    if (contentLength >= previousContentLength + allowedChangeAmt || contentLength <= previousContentLength - allowedChangeAmt) {
                        _messenger.send();
                    }
                }

                //set previous to current for next evaluation
                previousContentLength = contentLength;
            } else {
                throw new RuntimeException("Unable to retrieve content from " + _monitorUrl + ".");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
