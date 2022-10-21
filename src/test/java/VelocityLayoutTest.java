import org.apache.log4j.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VelocityLayoutTest {
    private static MemAppender memAppender;
    private static final Logger logger = Logger.getLogger("Test Logger");
    private static VelocityLayout velocityLayout;

    @BeforeAll
    public static void setup () {
        memAppender = MemAppender.getInstance(new ArrayList<>());
        BasicConfigurator.configure();
        logger.setLevel(Level.TRACE);
        logger.setAdditivity(false); // to avoid an excessive amount of logs printed to console
        velocityLayout = new VelocityLayout("[$p] ($t) $d: $m $n");
    }

    @BeforeEach
    public void removeAppenders () {
        logger.removeAllAppenders();
    }

    @AfterAll
    public static void removeTestData () throws IOException {
        Files.deleteIfExists(
                Paths.get("testLogs.txt"));
        logger.removeAllAppenders();
    }

    @Test
    public void layoutWorksWithMemAppender () {
        logger.addAppender(memAppender);
        memAppender.setLayout(velocityLayout);
        logger.trace("Testing Trace Log");
        assertEquals("[TRACE] (main) " + new Date() + ": Testing Trace Log \n"
                , memAppender.getEventStrings().get(0), "VelocityLayout not formatting correctly");
    }

    @Test
    public void layoutWorksWithFileAppender () throws IOException {
        FileAppender fileAppender = new FileAppender(velocityLayout, "testLogs.txt");
        logger.addAppender(fileAppender);
        logger.trace("Testing Trace Log");
        Date date = new Date();
        FileReader fileReader = new FileReader("testLogs.txt");
        BufferedReader buffer = new BufferedReader(fileReader);
        String line = buffer.readLine();
        assertEquals("[TRACE] (main) " + date + ": Testing Trace Log ", line,
                "VelocityLayout not formatting correctly");
        fileReader.close();
        buffer.close();
    }

    @Test
    public void layoutWorksWithConsoleAppender () {
        ConsoleAppender consoleAppender = new ConsoleAppender(velocityLayout);
        consoleAppender.activateOptions();
        logger.addAppender(consoleAppender);
        logger.trace("Test Console");
        consoleAppender.close();
    }
}