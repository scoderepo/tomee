package org.apache.openejb.tomcat.catalina;

import org.apache.openejb.BeanContext;
import org.apache.openejb.core.ThreadContext;

import javax.naming.LinkRef;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author rmannibucau
 */
public class ContextValue extends LinkRef {
    public static final String MODULES_PREFIX = "openejb/modules/";

    private final Collection<String> links = new ArrayList<String>();

    public ContextValue(String linkName) {
        super(linkName);
    }

    public synchronized String getLinkName() throws NamingException {
	    if (links.size() == 1) {
            return "java:" + links.iterator().next();
        }

        // else try to get BeanContextN to get linkname
        ThreadContext tc = ThreadContext.getThreadContext();
        if (tc != null && tc.getBeanContext() != null) {
            return "java:" + linkName(tc.getBeanContext().getModuleID(), super.getLinkName());
        }

        // TODO: should we parse a stacktrace to get the module?
        throw new NamingException("more than one module binding match this name " + super.getLinkName());
    }

    public void addValue(String link) {
        links.add(link);
    }

    public static String linkName(String moduleId, String name) {
        return MODULES_PREFIX + moduleId + "/" + name;
    }
}
