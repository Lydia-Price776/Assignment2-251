import java.util.List;

public interface SystemStatusMBean {
    Integer getSizeCachedLogs();
    List<String> getLogMessages();
    Long getDiscardedLogs();
     void setSizeCachedLogs();
     void setLogMessages();
     void setDiscardedLogs();

}
