package sl.interview.exceptions;

/**
 * Created by mwaldtha on 1/17/17.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException(String message) {
        super(message);
    }
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
