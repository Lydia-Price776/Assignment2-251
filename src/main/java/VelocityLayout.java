import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Date;

public class VelocityLayout extends Layout {
    private String pattern;

    public VelocityLayout(String newPattern) {
        pattern = newPattern;
    }

    public void activateOptions() {
    }


    public String format(LoggingEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLogger()); // Not sure about the category output
        context.put("d", new Date(event.getTimeStamp()));
        context.put("m", event.getMessage());
        context.put("p", event.getLevel());
        context.put("t", event.getThreadName());
        context.put("n", "\n");
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", pattern);
        return writer.toString();
    }

    public boolean ignoresThrowable() {
        return true;
    }

    public void setPattern(String newPattern) {
        pattern = newPattern;
    }
}
