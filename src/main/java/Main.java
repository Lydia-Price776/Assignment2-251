import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Main {
        public static void main(String[] args) throws InterruptedException {
          MemAppender memAppender = MemAppender.getInstance(new ArrayList<>());
            memAppender.setMaxSize(20);
            BasicConfigurator.configure();
            Logger logger = Logger.getLogger("test");
            logger.addAppender(memAppender);
            for (int i = 0; i < 100000; i++) {
                logger.error("Test number " + i, new Exception());
                Thread.sleep(1000);
            }

        }
}
