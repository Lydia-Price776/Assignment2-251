import java.util.List;

public interface SystemStatusMBean {
    Integer getSizeCachedLogs();
    List getLogMessages();
    Long getDiscardedLogs();
     void setSizeCachedLogs();
     void setLogMessages();
     void setDiscardedLogs();

}
