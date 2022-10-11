import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

public class VelocityLayout extends PatternLayout {


    private String pattern;
    public VelocityLayout(String newPattern){
        pattern = newPattern;
    }


    public void activateOptions() {
    }

    public String format(LoggingEvent event) {
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        StringReader reader = new StringReader(pattern);
        SimpleNode node = null;
        try {
            node = runtimeServices.parse(reader, "Event Information");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Template t = new Template();
        t.setRuntimeServices(runtimeServices);
        t.setData(node);
        t.initDocument();

        VelocityContext context = new VelocityContext();
        context.put("c", event.getLogger()); // What to use here???
        context.put("d", new Date(event.getTimeStamp()));
        context.put("m", event.getMessage());
        context.put("p", event.getLevel());
        context.put("t", event.getThreadName());
        context.put("n", "\n");

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }


    public boolean ignoresThrowable() {
        return true;
    }


    public void setPattern(String newPattern) {
        pattern = newPattern;
    }
}
