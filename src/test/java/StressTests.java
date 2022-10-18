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
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class StressTests {
    private static Logger logger;
    private static MemAppender memAppender;

    @BeforeAll
    public static void setup () {
        BasicConfigurator.configure();
        logger = Logger.getLogger("Tester");
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
    @ValueSource(ints = {1, 10, 1000, 10000, 100000,1000000})
    public void memAppenderAppendStressTestArrayList (int size) throws InterruptedException {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        memAppender.setMaxSize(size);
        logger.addAppender(memAppender);
      //  Thread.sleep(10000);
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestLinkedList (int size) throws InterruptedException {
        memAppender = MemAppender.getInstance(new LinkedList());
        memAppender.setMaxSize(size);
        logger.addAppender(memAppender);
        //Thread.sleep(10000);
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    public void FileAppenderStressTest (int logNum) throws IOException, InterruptedException {
        FileAppender fileAppender = new FileAppender(new SimpleLayout(), "testLogs.txt");
        logger.addAppender(fileAppender);
        Thread.sleep(10000);
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    public void ConsoleAppenderStressTest (int logNum) {
        ConsoleAppender consoleAppender = new ConsoleAppender(new SimpleLayout());
        logger.addAppender(consoleAppender);
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
    }
}


