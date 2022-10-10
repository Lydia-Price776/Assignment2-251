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

public class VelocityLayout extends PatternLayout {


    StringBuffer sbuf = new StringBuffer(128);
    private String pattern;


    public void activateOptions() {
    }

    public String format(LoggingEvent event) {
        RuntimeServices rs = RuntimeSingleton.getRuntimeServices();
        StringReader sr = new StringReader(pattern);
        SimpleNode sn = null;
        try {
            sn = rs.parse(sr, "Event Information");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Template t = new Template();
        t.setRuntimeServices(rs);
        t.setData(sn);
        t.initDocument();

        VelocityContext vc = new VelocityContext();
        vc.put("c", event.fqnOfCategoryClass);
        vc.put("d", event.getTimeStamp());
        vc.put("m", event.getMessage());
        vc.put("p", event.getLevel());
        vc.put("t", event.getThreadName());
        vc.put("n", "\n");

        StringWriter sw = new StringWriter();
        t.merge(vc, sw);

        return sw.toString();
    }


    public boolean ignoresThrowable() {
        return true;
    }
    public VelocityLayout(String newPattern){
       pattern = newPattern;
    }

    public void setPattern(String newPattern) {
        pattern = newPattern;
    }
}
