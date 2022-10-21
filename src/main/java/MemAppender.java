import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton {
    private Long discardedLogs = 0L;
    private List<LoggingEvent> logsList;
    private int maxSize = -1;
    private static MemAppender instance = null;
    private static SystemStatus systemStatus;


    //Private inorder to follow singleton pattern
    private MemAppender (List<LoggingEvent> list) {
        super();
        logsList = list;
    }

    @Override
    protected void append (LoggingEvent loggingEvent) {

        //if maxSize has not been supplied
        if (maxSize == -1) {
            logsList.add(loggingEvent);
        }
        if (logsList.size() <= maxSize - 1) {
            logsList.add(loggingEvent);
        } else {
            //The log in index position 0 will always be the oldest
            logsList.remove(0);
            increaseDiscardedLogCount();
            logsList.add(loggingEvent);
        }
        //systemStatus.updateValues(); commented out as is more useful in a 'main' program environment
        //this would automatically update the values in the mBeans object as opposed to calling the method
        // itself in a profiler
    }

    @Override
    public Layout getLayout () {
        return layout;
    }

    @Override
    public void close () {
        logsList.clear();
        discardedLogs = 0L;
        maxSize = -1;
        //systemStatus.updateValues(); see comment above
    }

    @Override
    public boolean requiresLayout () {
        return false;
    }

    @Override
    public void setLayout (Layout layout) {
        this.layout = layout;
    }

    //Returns a list of unmodifiable LoggingEvents
    public List<LoggingEvent> getCurrentLogs () {
        return Collections.unmodifiableList(logsList);
    }

    //Returns a list of unmodifiable strings. Generated using a layout stored in the MemAppender
    public List<String> getEventStrings () {
        List<String> logsListString = new ArrayList<>();
        for (LoggingEvent loggingEvent : logsList) {
            if (getLayout() != null) {
                logsListString.add(getLayout().format(loggingEvent));
            } else {
                logsListString.add(loggingEvent.getRenderedMessage());
            }
        }
        return Collections.unmodifiableList(logsListString);
    }

    //Prints Logging Events to the console using the layout and then clear the logs from its memory
    public void printLogs () {
        for (LoggingEvent loggingEvent : logsList) {
            if (getLayout() != null) {
                System.out.println(getLayout().format(loggingEvent));
            } else {
                System.out.println(loggingEvent.getRenderedMessage());
            }
        }
        logsList.clear();
        //printLogs() does not contribute to discarded logs
    }

    //If the maxSize is changed, then the logList size is adjusted accordingly
    private void adjustLogListSize () {
        if (logsList.size() != 0) {
            while (logsList.size() > maxSize) {
                logsList.remove(0);
            }
        }

    }

    //Tracks number of discarded logs when the maxSize is exceeded
    private void increaseDiscardedLogCount () {
        discardedLogs++;
    }

    public void setMaxSize (int maxSize) {
        this.maxSize = maxSize;
        adjustLogListSize();
    }

    public long getDiscardedLogCount () {
        return discardedLogs;
    }


    //To implement the singleton pattern and ensure only one instance is created
    public static MemAppender getInstance (List<LoggingEvent> list) {
        if (instance == null) {
            try {
                systemStatus = new SystemStatus();
                MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
                ObjectName objectName = new ObjectName("com.assignment2.MBeans:name=SystemStatus");
                platformMBeanServer.registerMBean(systemStatus, objectName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return instance = new MemAppender(list);

        } else {
            return instance;
        }

    }

    public int getMaxSize () {
        return maxSize;
    }

}
