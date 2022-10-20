import org.apache.log4j.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MemAppenderTest {
    private static MemAppender appender;
    private static Logger logger;

    @BeforeAll
    static void setUp () {
        appender = MemAppender.getInstance(new ArrayList<>());
        BasicConfigurator.configure();
        logger = Logger.getLogger("Test Logger");
        logger.addAppender(appender);
        logger.setAdditivity(false);
        logger.setLevel(Level.TRACE);
    }

    @BeforeEach
    void resetAppender () {
        appender.close();
        appender.setLayout(null);
        appender.setMaxSize(10);
    }

    @Test
    public void setLayoutTest () {
        Layout simple = new SimpleLayout();
        appender.setLayout(simple);
        assertEquals(simple, appender.getLayout(), "Appender layout not set correctly");
    }

    //Test that if there are no discarded logs, DiscardedLogs remains 0
    @Test
    public void testDiscardedLogs0 () {
        add10LogsToAppender();
        assertEquals(0, appender.getDiscardedLogCount(), "");
    }

    //Tests that the discarded logs are being recorded correctly
    @Test
    public void testDiscardedLogsNon0 () {
        add30LogsToAppender();
        assertEquals(20, appender.getDiscardedLogCount());
    }

    @Test
    public void testAppendNoDiscardedLogs () {
        add10LogsToAppender();
        assumeTrue(appender.getCurrentLogs().size() == 10);
        assertEquals("Trace 1", appender.getCurrentLogs().get(0).getMessage());
    }

    @Test
    public void testAppendWithDiscardedLogs () {
        add30LogsToAppender();
        assumeTrue(appender.getCurrentLogs().size() == 10);
        assertEquals("Info 6", appender.getCurrentLogs().get(0).getMessage());
    }

    @Test
    public void testGetCurrentLogsUnmodifiable () {
        add10LogsToAppender();
        assertTrue(appender.getCurrentLogs().getClass().getName().contains("Unmodifiable"));
    }

    @Test
    public void testGetEventStringUnmodifiable () {
        add10LogsToAppender();
        assumeTrue(appender.getEventStrings().size() > 0);
        assertTrue(appender.getEventStrings().getClass().getName().contains("Unmodifiable"));
    }

    @Test
    public void testGetEventStringGivenFormat () {
        logger.debug("Test Debug Log");
        appender.setLayout(new SimpleLayout());
        assertEquals("DEBUG - Test Debug Log\n", appender.getEventStrings().get(0));
    }

    @Test
    public void testGetEventStringNoGivenFormat () {
        appender.setLayout(null);
        logger.debug("Test Debug Log");
        assertEquals("Test Debug Log", appender.getEventStrings().get(0));
    }

    @Test
    public void testPrintLogsGivenFormat () {
        logger.debug("Test Debug Log");
        appender.setLayout(new SimpleLayout());
        String output = getOutputFromPrintLogs();
        assertTrue(output.contains("DEBUG - Test Debug Log"));
    }

    @Test
    public void testPrintLogsGivenNoFormat () {
        logger.debug("Test Debug Log");
        appender.setLayout(null);
        String output = getOutputFromPrintLogs();
        assertTrue(output.contains("Test Debug Log"));
    }

    @Test
    public void adjustMaxSizeLessThanOriginalSize () {
        add10LogsToAppender();
        appender.setMaxSize(5);
        assumeTrue(appender.getEventStrings().size() == 5);
        assertEquals("Trace 6", appender.getEventStrings().get(0));
    }

    @Test
    public void adjustMaxSizeGreaterThanOriginalSize () {
        add10LogsToAppender();
        appender.setMaxSize(20);
        assumeTrue(appender.getEventStrings().size() <= 20);
        assertEquals(20, appender.getMaxSize());
    }

    @Test
    public void addLogsAfterMaxSizeChanged () {
        add10LogsToAppender();
        appender.setMaxSize(20);
        add30LogsToAppender();
        assumeTrue(appender.getEventStrings().size() == 20);
        assertEquals("Error 3", appender.getEventStrings().get(0));
    }

    private static String getOutputFromPrintLogs () {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteOutputStream));
        appender.printLogs();
        return byteOutputStream.toString();
    }

    private static void add10LogsToAppender () {
        for (int i = 0; i < 10; i++) {
            logger.trace("Trace " + (i + 1));
        }
    }

    private static void add30LogsToAppender () {
        for (int i = 0; i < 10; i++) {
            logger.warn("Warn " + i);
            logger.error("Error " + i);
            logger.info("Info " + i);
        }
    }

}