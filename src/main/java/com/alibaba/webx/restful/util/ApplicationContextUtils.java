package com.alibaba.webx.restful.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

public class ApplicationContextUtils {

    private final static ThreadLocal<ApplicationContext> applicationContextLcal = new ThreadLocal<ApplicationContext>();

    public final static void setApplicationContext(ApplicationContext context) {
        applicationContextLcal.set(context);
    }

    public final static ApplicationContext getApplicationContext() {
        return applicationContextLcal.get();
    }
    
    public static ApplicationContext getApplicationContext(ServletContext sc) {
        return getApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

    public static void setApplicationContext(ServletContext servletContext, ApplicationContext applicationContext) {
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }

    /**
     * Find a custom WebApplicationContext for this web application.
     * 
     * @param sc ServletContext to find the web application context for
     * @param attrName the name of the ServletContext attribute to look for
     * @return the desired WebApplicationContext for this web app, or <code>null</code> if none
     */
    public static ApplicationContext getApplicationContext(ServletContext sc, String attrName) {
        Assert.notNull(sc, "ServletContext must not be null");
        Object attr = sc.getAttribute(attrName);
        if (attr == null) {
            return null;
        }
        if (attr instanceof RuntimeException) {
            throw (RuntimeException) attr;
        }
        if (attr instanceof Error) {
            throw (Error) attr;
        }
        if (attr instanceof Exception) {
            IllegalStateException ex = new IllegalStateException();
            ex.initCause((Exception) attr);
            throw ex;
        }
        if (!(attr instanceof ApplicationContext)) {
            throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
        }
        return (ApplicationContext) attr;
    }
}
