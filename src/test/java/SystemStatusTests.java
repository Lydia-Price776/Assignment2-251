import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemStatusTests {
    private static Logger logger;
    private static MemAppender memAppender;

    private static SystemStatus systemStatus;

    @BeforeAll
    public static void setUp(){
        BasicConfigurator.configure();
        logger = Logger.getLogger("Test Logger");
        logger.setAdditivity(false);
        logger.setLevel(Level.TRACE);
        memAppender = MemAppender.getInstance( new ArrayList<>());
        memAppender.setLayout(new SimpleLayout());
        logger.addAppender(memAppender);
    }
    @BeforeEach
    public void setMaxSize(){
        memAppender.setMaxSize(1);
        systemStatus = memAppender.getSystemStatus();
    }
    @AfterEach
    public void resetAppender(){
        memAppender.close();
    }

    @Test
    public void testSetSizeDiscardedLogs0 () {
        logger.error("Test Error");
        systemStatus.setDiscardedLogs();
        assertEquals(0,systemStatus.getDiscardedLogs());
    }

    @Test
    public void testSetSizeDiscardedLogsNon0 () {
        for(int i = 0; i < 5; i ++) {
            logger.error("Test Error " + i);
        }
        systemStatus.setDiscardedLogs();
        assertEquals(4,systemStatus.getDiscardedLogs());
    }

    @Test
    public void testSetLogMessages () {
        logger.error("Test Error");
        systemStatus.setLogMessages();
        assertEquals("ERROR - Test Error\n",systemStatus.getLogMessages().get(0));

    }

    @Test
    public void testSetCachedLogsNonZero () {
        logger.error("Test Error");
        systemStatus.setSizeCachedLogs();
        assertEquals(19,systemStatus.getSizeCachedLogs());
    }
}
