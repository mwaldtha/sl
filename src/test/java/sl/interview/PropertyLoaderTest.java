package sl.interview;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Properties;

import static org.testng.Assert.*;

public class PropertyLoaderTest {

    //{property file name, expected property count}
    @DataProvider(name = "propsProvider")
    public static Object[][] propFiles() {
        return new Object[][] {
                {"", 0},
                {null, 0},
                {"/non-existent.properties", 0},
                {"/email-config.properties", 6},
                {"/monitor-config.properties", 2}
        };
    }

    /**
     * Uses the specified dataProvider to test various property files.
     *
     * @param propFile Name of the property file to use for the test
     * @param expectedPropCount Expected number of properties to be loaded
     * @throws Exception
     */
    @Test(dataProvider = "propsProvider")
    public void testLoad(String propFile, int expectedPropCount) throws Exception {
        Properties props = PropertyLoader.load(propFile);
        assertTrue(props.size() == expectedPropCount);
    }

}