package sl.interview;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mwaldtha on 1/17/17.
 */
public class PropertyLoader {
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
