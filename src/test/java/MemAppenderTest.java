import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemAppenderTest {
    private static MemAppender appender;
    private static Logger logger;
    @BeforeAll
    static void setUp() {
        appender = MemAppender.getInstance(10);
        BasicConfigurator.configure();
        logger = Logger.getLogger("Test Logger");
        logger.addAppender(appender);
        logger.setAdditivity(false);
    }

    @Test
    public void setLayoutTest() {
        Layout simple = new SimpleLayout();
        appender.setLayout(simple);
        assertEquals(simple, appender.getLayout());
    }
    @Test
    public void testDiscardedLogs0() {
        for (int i = 0; i < 10; i ++) {
            logger.warn("Warn" + i );
        }
        assertEquals(0, appender.getDiscardedLogCount());
    }
    @Test
    public void testDiscardedLogsNon0() {
        appender = MemAppender.getInstance(5);

        for (int i = 0; i < 10; i ++) {
            logger.warn("Warn" + i );
            logger.error("Error" + i );
            logger.info("Info" + i );
        }
       assertEquals(20, appender.getDiscardedLogCount());
    }


    @Test
    public void testAppendNoDiscardedLogs() {
        appender = MemAppender.getInstance(5);
    }

}