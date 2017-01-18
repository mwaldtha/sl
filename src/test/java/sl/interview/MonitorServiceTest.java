package sl.interview;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.*;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class MonitorServiceTest {

    @AfterMethod
    public void cleanUp() throws Exception {
        System.clearProperty("monitor_props_file");
    }

    @DataProvider(name = "configProvider")
    public Object[][] getConfiguration() {
        return new Object[][] {
                {"", true},
                {"/monitor-config.properties", true},
                {"/test-no-url.properties", false}
        };
    }
    @Test(dataProvider = "configProvider")
    public void testStart(String propFile, boolean expectToBeScheduled) throws Exception {
        ScheduledExecutorService mockScheduledExecutorService = mock(ScheduledExecutorService.class);

        MonitorService monitor = new MonitorService();
        monitor.pollingService = mockScheduledExecutorService;

        if (!propFile.equals("")) {
            System.setProperty("monitor_props_file", propFile);
        }

        monitor.start();

        if (expectToBeScheduled) {
            verify(mockScheduledExecutorService).scheduleAtFixedRate(anyObject(), anyLong(), anyLong(), anyObject());
        } else {
            verify(mockScheduledExecutorService, never()).scheduleAtFixedRate(anyObject(), anyLong(), anyLong(), anyObject());
        }
    }
}