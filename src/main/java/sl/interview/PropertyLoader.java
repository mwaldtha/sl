package sl.interview;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading values from property files.
 */
public class PropertyLoader {

    /**
     * Method for creating a Properties object from a file.
     *
     * @param propertyFile The property file to load
     * @return A Properties object containing all the properties found in the supplied file
     */
    public static Properties load(String propertyFile) {
        Properties loadedProps = new Properties();

        if (propertyFile != null && !propertyFile.isEmpty()) {
            try (InputStream propertyStream = PropertyLoader.class.getResourceAsStream(propertyFile)) {
                if (propertyStream != null) {
                    loadedProps.load(propertyStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return loadedProps;
    }
}
