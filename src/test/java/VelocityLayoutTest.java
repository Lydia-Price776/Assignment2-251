import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VelocityLayoutTest {
    private static MemAppender memAppender;
    private static ConsoleAppender consoleAppender;
    private static FileAppender fileAppender;
    private static Logger logger;
    private static VelocityLayout velocityLayout;

    @BeforeAll
    static void setup () {
        memAppender = MemAppender.getInstance(5);
        BasicConfigurator.configure();
        logger = Logger.getLogger("Test Logger");
        logger.setLevel(Level.TRACE);
        logger.setAdditivity(false);
        velocityLayout = new VelocityLayout("[$p] ($t) $d: $m $n");
    }

    @BeforeEach
    void removeAppenders () {
        logger.removeAllAppenders();
    }

    @AfterEach
    void removeTestData () throws IOException {
        Files.deleteIfExists(
                Paths.get("testLogs.txt"));
    }

    @Test
    void formatsLoggingEventCorrectly () {
        LoggingEvent event = new LoggingEvent(null, logger, 0L, Level.DEBUG,
                "Test Logging Event", null);
        String testString = "[DEBUG] (main) Thu Jan 01 12:00:00 NZST 1970: Test Logging Event \n";
        assertEquals(testString, velocityLayout.format(event));
    }

    @Test
    void layoutWorksWithMemAppender () {
        logger.addAppender(memAppender);
        memAppender.setLayout(velocityLayout);
        logger.trace("Testing Trace Log");
        assertEquals("[TRACE] (main) " + new java.util.Date() + ": Testing Trace Log \n"
                , memAppender.getEventStrings().get(0));
    }

    @Test
    void layoutWorksWithFileAppender () throws IOException {
        fileAppender = new FileAppender(velocityLayout, "testLogs.txt");
        logger.addAppender(fileAppender);
        logger.trace("Testing Trace Log");
        Date date = new java.util.Date();
        FileReader fileReader = new FileReader("testLogs.txt");
        BufferedReader buffer = new BufferedReader(fileReader);
        String line = buffer.readLine();
        assertEquals("[TRACE] (main) " + date + ": Testing Trace Log ", line);
        fileReader.close();
    }

    @Test
    void layoutWorksWithConsoleAppender () {
        consoleAppender = new ConsoleAppender(velocityLayout);
        consoleAppender.activateOptions();
        logger.addAppender(consoleAppender);
        logger.trace("Test Console");
        consoleAppender.close();
    }
}