import java.util.List;

public interface SystemStatusMBean {
    long getSizeCachedLogs();
    List<String> getLogMessages();
    long getDiscardedLogs();
     void setSizeCachedLogs();
     void setLogMessages();
     void setDiscardedLogs();

}
