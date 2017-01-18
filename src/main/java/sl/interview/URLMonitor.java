package sl.interview;

/**
 * Simple cass to start the MonitorService (i.e. from the command line)
 */
public class URLMonitor {

    private static MonitorService monitorService = new MonitorService();

    //added to ease testing, allows a mock MonitorService to be injected
    static void setMonitorService(MonitorService monitorService) {
        URLMonitor.monitorService = monitorService;
    }

    public static void main(String[] args) {
        monitorService.start();
    }
}
