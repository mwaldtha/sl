package sl.interview;

import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class URLMonitorTest {

    /**
     * Tests that the main method actually invokes the start method of the MonitorService
     *
     * @throws Exception
     */
    @Test
    public void testURLMonitorMain() throws Exception {
        MonitorService mockMonitor = mock(MonitorService.class);
        URLMonitor.setMonitorService(mockMonitor);

        URLMonitor.main(null);

        verify(mockMonitor).start();
    }
}