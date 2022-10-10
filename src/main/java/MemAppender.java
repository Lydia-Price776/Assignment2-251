import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MemAppender extends AppenderSkeleton {
    private Long discardedLogs = 0L;
    private List<LoggingEvent> logsList;
    private final int maxSize;
    private static MemAppender instance;

    private MemAppender (int size) {
        super();
        logsList = new ArrayList<>();
        maxSize = size;
    }

    @Override
    protected void append (LoggingEvent loggingEvent) {
        if (logsList.size() < maxSize - 1) {
            logsList.add(loggingEvent);
        } else {
            long temp = logsList.get(0).getTimeStamp();
            int j = 0;
            for (int i = 0; i < logsList.size(); i++) {
                if (temp < logsList.get(i).getTimeStamp()) {
                    temp = logsList.get(i).getTimeStamp();
                    j = i;
                }
                logsList.remove(j);
                increaseDiscardedLogCount();
                logsList.add(loggingEvent);
            }
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
                //TODO Implement default layout
            }
        }
        return logsListString;
    }

    //Prints Logging Events to the console using the layout and then clear the logs from its memory
    public void printLogs () {

        for (LoggingEvent loggingEvent : logsList) {
            if (getLayout() != null) {
                System.out.println(getLayout().format(loggingEvent));
            } else {
                printNoLayoutGiven(logsList);
            }
        }

        for (int i = 0; i < logsList.size(); i++) {
            logsList.remove(i);
            increaseDiscardedLogCount();
        }
    }

    private void increaseDiscardedLogCount () {
        discardedLogs++;
    }

    public long getDiscardedLogCount () {
        return discardedLogs;
    }

    private void printNoLayoutGiven (List<LoggingEvent> logsList) {
        //TODO Implement default layout
    }

    public static MemAppender getInstance () {
        if (instance == null) {
            return instance = new MemAppender(100);
        } else {
            return instance;
        }
    }
}
