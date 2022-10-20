import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

public class SystemStatus implements SystemStatusMBean {

    private Integer cacheSize;
    private List logMessages;
    private long discardedLogs;
    SystemStatus () {
        this.logMessages = null;
        this.cacheSize = 0;
        this.discardedLogs = 0;
    }

    public void updateValues(){
        setDiscardedLogs();
        setLogMessages();
        setSizeCachedLogs();
    }

    @Override
    public Integer getSizeCachedLogs () {
        return this.cacheSize;
    }

    @Override
    public List getLogMessages () {
        return this.logMessages;
    }

    @Override
    public Long getDiscardedLogs () {
        return this.discardedLogs;
    }

    @Override
    public void setSizeCachedLogs () {
        List list = MemAppender.getInstance(new ArrayList<>()).getEventStrings();
        for (int i = 0; i < list.size(); i++ ){
            this.cacheSize += list.get(i).toString().length();
        }
    }

    @Override
    public void setLogMessages () {
        this.logMessages =MemAppender.getInstance(new ArrayList<>()).getEventStrings();
    }

    @Override
    public void setDiscardedLogs () {
        this.discardedLogs = MemAppender.getInstance(new ArrayList<>()).getDiscardedLogCount();

    }

}
