package sl.interview.communication;

/**
 * A simple interface to define a generic messenger which could be extended to create different types of messengers.
 * (e.g. SMTPMessenger, JMSMessenger, etc.)
 */
public interface Messenger {

    /**
     * Sends a message according to the type of implementation.
     * @throws Exception
     */
    void send() throws Exception;
}
