import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton {
    private Long discardedLogs = 0L;
    private List<LoggingEvent> logsMemory;
    private final int maxSize;

    //Constructor to set maxSize
    public MemAppender (int size) {
        super();
        logsMemory = new ArrayList<>();
        maxSize = size;
    }

    @Override
    protected void append (LoggingEvent loggingEvent) {
        if (logsMemory.size() < maxSize - 1) {
            logsMemory.add(loggingEvent);
        } else {
            Long temp = logsMemory.get(0).getTimeStamp();
            int j = 0;
            for (int i = 0; i < logsMemory.size(); i++) {
                if (temp < logsMemory.get(i).getTimeStamp()) {
                    temp = logsMemory.get(i).getTimeStamp();
                    j = i;
                }
                logsMemory.remove(j);
                increaseDiscardedLogCount();
                logsMemory.add(loggingEvent);
            }
        }
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
        //Collections.unmodifiableList(); returns an unmodifiable list
        return null;
    }

    //Returns a list of unmodifiable strings. Generated using a layout stored in the MemAppender
    public List<String> getEventStrings () {
        return null;
    }

    //Prints Logging Events to the console using the layout and then clear the logs from its memory
    public void printLogs () {

    }

    private void increaseDiscardedLogCount () {
        discardedLogs++;
    }

    public long getDiscardedLogCount () {
        return discardedLogs;
    }
}
