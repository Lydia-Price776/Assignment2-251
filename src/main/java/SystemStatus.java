import java.util.ArrayList;
import java.util.List;

public class SystemStatus implements SystemStatusMBean {

    private long cacheSize;
    private List logMessages;
    private long discardedLogs;
    public SystemStatus () {
        this.logMessages = null;
        this.cacheSize = 0L;
        this.discardedLogs = 0;
    }

    //Used for automatically updating the MBean Object
    public void updateValues(){
        setDiscardedLogs();
        setLogMessages();
        setSizeCachedLogs();
    }

    @Override
    public long getSizeCachedLogs () {
        return this.cacheSize;
    }

    @Override
    public List getLogMessages () {
        return this.logMessages;
    }

    @Override
    public long getDiscardedLogs () {
        return this.discardedLogs;
    }

    @Override
    public void setSizeCachedLogs () {
      List<String> eventStrings = MemAppender.getInstance(new ArrayList<>()).getEventStrings();
      long characters = 0L;
        for (String eventString : eventStrings) {
            characters += eventString.length();
        }
      this.cacheSize = characters;
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
