import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class StressTests {
    private static final Logger logger = Logger.getRootLogger();
    private static MemAppender memAppender;

    @BeforeAll
    public static void setup () {
        BasicConfigurator.configure();
        logger.setAdditivity(false);
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
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestArrayList (int size) {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        memAppender.setMaxSize(size);
        logger.addAppender(memAppender);
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i);
        }
        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestLinkedList (int size) {
        memAppender = MemAppender.getInstance(new LinkedList());
        memAppender.setMaxSize(size);
        logger.addAppender(memAppender);
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i);
        }
        memAppender.close();
    }

/*
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 100000, 1000000})
    void arrayListStressTestParameterized (int size) {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        memAppender.setLayout(new VelocityLayout("[$p] $c $d: $m"));
        logger.addAppender(memAppender);
        memAppender.setMaxSize(size);

        for (int i = 0; i < 100_000; i++) {
            logger.error("Testing" + i, new Exception());
        }

        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendParameterizedTestLinkedList (int param) {
        MemAppender memAppender = MemAppender.getInstance();
        memAppender.setLinkedList();
        logger.addAppender(memAppender);
        assertTimeout(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < param; i++) {
                logger.trace("Test Trace Log " + (i + 1));
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000, 1000000})
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
    @ValueSource(ints = {1, 10, 100, 1000, 10000, 100000, 1000000})
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
*/

}
