import org.apache.log4j.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class StressTests {
    private static Logger logger;

    @BeforeAll
    public static void setup () {

        BasicConfigurator.configure();
        logger = Logger.getLogger("Test Logger");
        logger.setAdditivity(false);
        logger.setLevel(Level.TRACE);
        VelocityLayout velocityLayout = new VelocityLayout("[$p] ($t) $d: $m $n");

    }

    @AfterEach
    public void resetData () {
        logger.removeAllAppenders();
    }
    @AfterAll
    static void removeTestData () throws IOException {
        Files.deleteIfExists(
                Paths.get("testLogs.txt"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000})
    public void memAppenderAppendParameterizedTestArrayList (int param) {
        MemAppender memAppender = MemAppender.getInstance(1);
        logger.addAppender(memAppender);
        assertTimeout(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < param; i++) {
                logger.trace("Test Trace Log " + (i + 1));
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000})
    public void memAppenderAppendParameterizedTestLinkedList (int param) {
        MemAppender memAppender = MemAppender.getInstance(1);
        memAppender.setLinkedList();
        logger.addAppender(memAppender);
        assertTimeout(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < param; i++) {
                logger.trace("Test Trace Log " + (i + 1));
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000})
    public void ConsoleAppenderParameterizedTest (int param) {
        ConsoleAppender consoleAppender = new ConsoleAppender(new SimpleLayout());
        logger.addAppender(consoleAppender);
        logger.setAdditivity(false);
        assertTimeout(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < param; i++) {
                logger.trace("Test Trace Log " + (i + 1));
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000})
    public void FileAppenderParameterizedTest (int param) throws IOException {
        FileAppender fileAppender = new FileAppender(new SimpleLayout(), "testLogs.txt");
        logger.addAppender(fileAppender);
        logger.setAdditivity(false);
        assertTimeout(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < param; i++) {
                logger.trace("Test Trace Log " + (i + 1));
            }
        });
    }
}
