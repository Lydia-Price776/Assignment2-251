import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MemAppender extends AppenderSkeleton {
    private Long discardedLogs = 0L;
    private List<LoggingEvent> logsList;
    private int maxSize;
    private static MemAppender instance;

    private MemAppender (int size) {
        super();
        logsList = new LinkedList<>();
        maxSize = size;
    }

    @Override
    protected void append (LoggingEvent loggingEvent) {
        if (logsList.size() <= maxSize - 1) {
            logsList.add(loggingEvent);
        } else {
            //The log in index position 0 will always be the oldest
            logsList.remove(0);
            increaseDiscardedLogCount();
            logsList.add(loggingEvent);
        }
    }

    @Override
    public Layout getLayout () {
        return super.getLayout();
    }

    @Override
    public void close () {
    }

    @Override
    public boolean requiresLayout () {
        return false;
    }

    @Override
    public void setLayout (Layout layout) {
        super.setLayout(layout);
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
        for (int i = 0; i < logsList.size(); i++) {
            logsList.remove(i);
            increaseDiscardedLogCount();
        }
    }

    private void adjustLoggingListSize () {
        if (logsList.size() == 0) {
            return;
        }
        while (logsList.size() > maxSize) {
            logsList.remove(0);
        }

    }

    //Tracks number of discarded logs
    private void increaseDiscardedLogCount () {
        discardedLogs++;
    }

    public void setMaxSize (int maxSize) {
        this.maxSize = maxSize;
        adjustLoggingListSize();
    }

    public long getDiscardedLogCount () {
        return discardedLogs;
    }

    public void discardedLogsReset () {
        discardedLogs = 0L;
    }

    public void clearMemory () {
        logsList.clear();
        discardedLogsReset();
    }

    public static MemAppender getInstance (int size) {
        if (instance == null) {
            return instance = new MemAppender(size);
        } else {
            instance.setMaxSize(size);
            return instance;
        }
    }

    public int getMaxSize () {
        return maxSize;
    }

    public void  setLinkedList(){
        logsList = new LinkedList<>();
    }
}
