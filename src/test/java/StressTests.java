import org.apache.log4j.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class StressTests {
    private static Logger logger;
    private static MemAppender memAppender;
    private static VelocityLayout velocityLayout;
    private static PatternLayout patternLayout;

    @BeforeAll
    public static void setup () {
        BasicConfigurator.configure();
        logger = Logger.getLogger("Tester");
        logger.setAdditivity(false);
        velocityLayout = new VelocityLayout("[$p] ($t) $d: $m $n");
        patternLayout = new PatternLayout("[%p] (%t) %d: %m %n");
    }

    @AfterEach
    public void resetData () {
        logger.removeAllAppenders();
    }

    @AfterAll
    public static void removeTestData () throws IOException {
        Files.deleteIfExists(
          Paths.get("testLogs.txt"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestArrayListVelocityLayout (int size) {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        memAppender.setMaxSize(size);
        memAppender.setLayout(velocityLayout);
        logger.addAppender(memAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        //Thread.sleep(1000); used for profiling purposes

        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestArrayListPatternLayout (int size) {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        memAppender.setMaxSize(size);
        memAppender.setLayout(patternLayout);
        logger.addAppender(memAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        //Thread.sleep(1000); used for profiling purposes

        memAppender.close();

    }


    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestLinkedListVelocityLayout (int size) {
        memAppender = MemAppender.getInstance(new LinkedList<>());
        memAppender.setMaxSize(size);
        memAppender.setLayout(velocityLayout);
        logger.addAppender(memAppender);
        //Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        // Thread.sleep(1000); used for profiling purposes
        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000, 1000000})
    public void memAppenderAppendStressTestLinkedListPatternLayout (int size) {
        memAppender = MemAppender.getInstance(new LinkedList<>());
        memAppender.setMaxSize(size);
        memAppender.setLayout(patternLayout);
        logger.addAppender(memAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < 100000; i++) {
            logger.error("Test number " + i, new Exception());
        }
        // Thread.sleep(1000); used for profiling purposes
        memAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    public void fileAppenderVelocityLayoutStressTest (int logNum) throws IOException {
        FileAppender fileAppender = new FileAppender(velocityLayout, "testLogs.txt");
        logger.addAppender(fileAppender);
        // Thread.sleep(10000); used for profiling purposes
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
        // Thread.sleep(1000);  used for profiling purposes
        fileAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    public void fileAppenderPatternLayoutStressTest (int logNum) throws IOException {
        FileAppender fileAppender = new FileAppender(patternLayout, "testLogs.txt");
        logger.addAppender(fileAppender);
        // Thread.sleep(10000);  used for profiling purposes
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
        // Thread.sleep(1000);  used for profiling purposes
        fileAppender.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    @Tag("Console")
    public void consoleAppenderVelocityLayoutStressTest (int logNum) {
        ConsoleAppender consoleAppender = new ConsoleAppender(velocityLayout);
        logger.addAppender(consoleAppender);
        // Thread.sleep(10000);  used for profiling purposes
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
        //  Thread.sleep(1000);  used for profiling purposes
        consoleAppender.close();
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 1000, 10000, 100000})
    @Tag("Console")
    public void consoleAppenderPatternLayoutStressTest (int logNum) {
        ConsoleAppender consoleAppender = new ConsoleAppender(patternLayout);
        logger.addAppender(consoleAppender);
        // Thread.sleep(10000);  used for profiling purposes
        for (int i = 0; i < logNum; i++) {
            logger.error("Test number " + i, new Exception());
        }
        //  Thread.sleep(1000);  used for profiling purposes
        consoleAppender.close();
    }
}


