package org.yflyud.projects.websearch.client;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.impl.SimpleLog;

public class SimpleLog2 extends SimpleLog {
    private static final long serialVersionUID = -6962805506480157505L;
    static protected String simpleLogPropsFile = "simplelog.properties";

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            ; // Ignore
        }
        return (prop == null) ? simpleLogProps.getProperty(name) : prop;
    }

    private static String getStringProperty(String name, String dephault) {
        String prop = getStringProperty(name);
        return (prop == null) ? dephault : prop;
    }

    private static boolean getBooleanProperty(String name, boolean dephault) {
        String prop = getStringProperty(name);
        return (prop == null) ? dephault : "true".equalsIgnoreCase(prop);
    }

    // Initialize class attributes.
    // Load properties file, if found.
    // Override with system properties.
    static {
        // Add props from the resource simplelog.properties
        simpleLogPropsFile = getStringProperty(systemPrefix + "simpleLogPropsFile",
                simpleLogPropsFile);
        InputStream in = getResourceAsStream(simpleLogPropsFile);
        if (null != in) {
            try {
                simpleLogProps.load(in);
                in.close();
            } catch (java.io.IOException e) {
                // ignored
            }
        }

        showLogName = getBooleanProperty(systemPrefix + "showlogname", showLogName);
        showShortName = getBooleanProperty(systemPrefix + "showShortLogname", showShortName);
        showDateTime = getBooleanProperty(systemPrefix + "showdatetime", showDateTime);

        if (showDateTime) {
            dateTimeFormat = getStringProperty(systemPrefix + "dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (IllegalArgumentException e) {
                // If the format pattern is invalid - use the default format
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }

    public SimpleLog2(String name) {
        super(name);
    }

    private static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;

        if (classLoader == null) {
            try {
                // Are we running on a JDK 1.2 or later system?
                Method method = Thread.class.getMethod("getContextClassLoader", (Class[]) null);

                // Get the thread context class loader (if there is one)
                try {
                    classLoader = (ClassLoader) method.invoke(Thread.currentThread(),
                            (Object[]) null);
                } catch (IllegalAccessException e) {
                    ; // ignore
                } catch (InvocationTargetException e) {
                    /**
                     * InvocationTargetException is thrown by 'invoke' when the
                     * method being invoked (getContextClassLoader) throws an
                     * exception.
                     * 
                     * getContextClassLoader() throws SecurityException when the
                     * context class loader isn't an ancestor of the calling
                     * class's class loader, or if security permissions are
                     * restricted.
                     * 
                     * In the first case (not related), we want to ignore and
                     * keep going. We cannot help but also ignore the second
                     * with the logic below, but other calls elsewhere (to
                     * obtain a class loader) will trigger this exception where
                     * we can make a distinction.
                     */
                    if (e.getTargetException() instanceof SecurityException) {
                        ; // ignore
                    } else {
                        // Capture 'e.getTargetException()' exception for
                        // details
                        // alternate: log 'e.getTargetException()', and pass
                        // back 'e'.
                        throw new LogConfigurationException("Unexpected InvocationTargetException",
                                e.getTargetException());
                    }
                }
            } catch (NoSuchMethodException e) {
                // Assume we are running on JDK 1.1
                ; // ignore
            }
        }

        if (classLoader == null) {
            classLoader = SimpleLog.class.getClassLoader();
        }

        // Return the selected class loader
        return classLoader;
    }

    //Copied from class SimpleLog
    private static InputStream getResourceAsStream(final String name) {
        return AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
            public InputStream run() {
                ClassLoader threadCL = getContextClassLoader();

                if (threadCL != null) {
                    return threadCL.getResourceAsStream(name);
                } else {
                    return ClassLoader.getSystemResourceAsStream(name);
                }
            }
        });
    }

}
