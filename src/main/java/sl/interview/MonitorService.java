package sl.interview;

import sl.interview.communication.Messenger;
import sl.interview.communication.SMTPMessenger;
import sl.interview.exceptions.ConfigurationException;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * Configures and starts a task, then executes it at the configured interval.
 */
public class MonitorService {

    protected ScheduledExecutorService pollingService = Executors.newSingleThreadScheduledExecutor();
    protected String monitorUrl = "";
    protected long pollingInterval = TimeUnit.MINUTES.toMillis(1);
    private Messenger messenger = new SMTPMessenger();

    /**
     * Configures and starts the service.
     */
    public void start() {
        try {
            configure();

            System.out.printf("Monitoring %s every %d ms.%n", monitorUrl, pollingInterval);

            ScheduledFuture<?> pollingFuture = pollingService.scheduleAtFixedRate(new ContentComparator(monitorUrl, messenger), 0, pollingInterval, TimeUnit.MILLISECONDS);
            if (pollingFuture != null) {
                pollingFuture.get();
            }
        } catch (ConfigurationException ce) {
            System.out.println(ce.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            stop("An exception occured while executing a scheduled task: %s%n", ex.getMessage());
        }
    }

    /**
     * Loads configuration properties for the service using the default property file or one specified in the 'monitor_props_file' system property.
     * @throws ConfigurationException
     */
    private void configure() throws ConfigurationException {
        Properties monitorProps = PropertyLoader.load(System.getProperty("monitor_props_file","/monitor-config.properties"));

        //get url and polling interval parameters
        monitorUrl = monitorProps.getProperty("monitor.url");
        if (monitorUrl == null || monitorUrl.isEmpty()) {
            throw new ConfigurationException("No URL to be monitored was found. Please ensure the 'monitor.url' property is set.");
        }

        String pollingIntervalVal = monitorProps.getProperty("monitor.polling.interval.ms");
        if (pollingIntervalVal != null && !pollingIntervalVal.equals("0")) {
            pollingInterval = Long.parseLong(pollingIntervalVal);
        }
    }

    /**
     * Prints an informational message and then stops the service.
     * @param msgFormat printf style message and format
     * @param arguments values to be replaced in the msgFormat string
     */
    private void stop(String msgFormat, Object... arguments) {
        System.out.printf(msgFormat, arguments);

        try {
            pollingService.shutdown();
            pollingService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException iex) {
            //do nothing, will attempt a more blunt shutdown in finally block, if necessary
        } finally {
            if (!pollingService.isTerminated()) {
                pollingService.shutdownNow();
            }
        }
    }
}
