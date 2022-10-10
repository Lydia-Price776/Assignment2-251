import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

public class test {
    public static void main (String[] args) {
        BasicConfigurator.configure();
        Logger l = Logger.getLogger("test");

      MemAppender app = MemAppender.getInstance();
       // app.setLayout(new SimpleLayout());


        l.addAppender(app);

        l.warn("first");
        l.warn("second");
        l.warn("third");

        l.trace("fourth shouldn't be printed");

        app.printLogs();


    }
}

