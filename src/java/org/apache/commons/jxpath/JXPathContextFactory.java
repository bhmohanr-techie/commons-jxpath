/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jxpath/src/java/org/apache/commons/jxpath/JXPathContextFactory.java,v 1.2 2002/04/24 03:29:33 dmitri Exp $
 * $Revision: 1.2 $
 * $Date: 2002/04/24 03:29:33 $
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2001, Plotnix, Inc,
 * <http://www.plotnix.com/>.
 * For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.commons.jxpath;

import java.util.*;
import java.io.*;
import javax.xml.transform.TransformerFactory;

/**
 * Defines a factory API that enables applications to obtain a
 * JXPathContext instance.  To acquire a JXPathContext, first call the
 * static <code>newInstance()</code> method of JXPathContextFactory.
 * This method returns a concrete JXPathContextFactory.
 * Then call <code>newContext()</code> on that instance.  You will rarely
 * need to perform these steps explicitly: usually you can call one of the
 * <code>JXPathContex.newContext</code> methods, which will perform these steps
 * for you.
 *
 * @see JXPathContext#newContext(Object)
 * @see JXPathContext#newContext(JXPathContext,Object)
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.2 $ $Date: 2002/04/24 03:29:33 $
 */
public abstract class JXPathContextFactory {

    /** The default property */
    public static final String FACTORY_NAME_PROPERTY =
        "org.apache.commons.jxpath.JXPathContextFactory";

    /** The default factory class */
    private static final String defaultClassName =
        "org.apache.commons.jxpath.ri.JXPathContextFactoryReferenceImpl";

    protected JXPathContextFactory () {

    }

    /**
     * Obtain a new instance of a <code>JXPathContextFactory</code>.
     * This static method creates a new factory instance.
     * This method uses the following ordered lookup procedure to determine
     * the <code>JXPathContextFactory</code> implementation class to load:
     * <ul>
     * <li>
     * Use the <code>org.apache.commons.jxpath.JXPathContextFactory</code> system
     * property.
     * </li>
     * <li>
     * Alternatively, use the JAVA_HOME (the parent directory where jdk is
     * installed)/lib/jxpath.properties for a property file that contains the
     * name of the implementation class keyed on
     * <code>org.apache.commons.jxpath.JXPathContextFactory</code>.
     * </li>
     * <li>
     * Use the Services API (as detailed in the JAR specification), if
     * available, to determine the classname. The Services API will look
     * for a classname in the file
     * <code>META-INF/services/<i>org.apache.commons.jxpath.JXPathContextFactory</i></code>
     * in jars available to the runtime.
     * </li>
     * <li>
     * Platform default <code>JXPathContextFactory</code> instance.
     * </li>
     * </ul>
     *
     * Once an application has obtained a reference to a
     * <code>JXPathContextFactory</code> it can use the factory to
     * obtain JXPathContext instances.
     *
     * @exception JXPathFactoryConfigurationError if the implementation is not
     * available or cannot be instantiated.
     */

    public static JXPathContextFactory newInstance() {
        String factoryImplName = findFactory(FACTORY_NAME_PROPERTY, defaultClassName);

        if (factoryImplName == null) {
            throw new JXPathContextFactoryConfigurationError(
                "No default implementation found");
        }

        JXPathContextFactory factoryImpl;
        try {
            Class clazz = Class.forName(factoryImplName);
            factoryImpl = (JXPathContextFactory)clazz.newInstance();
        } catch  (ClassNotFoundException cnfe) {
            throw new JXPathContextFactoryConfigurationError(cnfe);
        } catch (IllegalAccessException iae) {
            throw new JXPathContextFactoryConfigurationError(iae);
        } catch (InstantiationException ie) {
            throw new JXPathContextFactoryConfigurationError(ie);
        }
        return factoryImpl;
    }

    /**
     * Creates a new instance of a JXPathContext using the
     * currently configured parameters.
     *
     * @exception JXPathContextFactoryConfigurationError if a JXPathContext
     * cannot be created which satisfies the configuration requested
     */

    public abstract JXPathContext newContext(JXPathContext parentContext, Object contextBean)
        throws JXPathContextFactoryConfigurationError;

    // -------------------- private methods --------------------
    // This code is duplicated in all factories.
    // Keep it in sync or move it to a common place
    // Because it's small probably it's easier to keep it here
    /** Avoid reading all the files when the findFactory
        method is called the second time ( cache the result of
        finding the default impl )
    */
    private static String foundFactory = null;

    /** Temp debug code - this will be removed after we test everything
     */
    private static boolean debug = false;
    static {
        try {
            debug = System.getProperty("jxpath.debug") != null;
        }
        catch (SecurityException se) {
        }
    }

    /** Private implementation method - will find the implementation
        class in the specified order.
        @param property    Property name
        @param defaultFactory Default implementation, if nothing else is found
        
        @return class name of the JXPathContextFactory
    */
    private static String findFactory(String property, String defaultFactory) {
        // Use the system property first
        try {
            String systemProp = System.getProperty(property);
            if (systemProp != null) {
                if (debug){
                    System.err.println("JXPath: found system property" + systemProp);
                }
                return systemProp;
            }

        }
        catch (SecurityException se) {
        }

        if (foundFactory != null){
            return foundFactory;
        }

        // Use the factory ID system property first
        try {
            String systemProp = System.getProperty(property);
            if (systemProp != null) {
                if (debug){
                    System.err.println("JXPath: found system property" + systemProp);
                }
                return systemProp;
            }

        }
        catch (SecurityException se) {
        }

        // try to read from $java.home/lib/xml.properties
        try {
            String javah = System.getProperty("java.home");
            String configFile =
                javah + File.separator + "lib" + File.separator + "jxpath.properties";
            File f = new File(configFile);
            if (f.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(f));
                foundFactory = props.getProperty(property);
                if (debug){
                    System.err.println("JXPath: found java.home property " + foundFactory);
                }
                if (foundFactory != null){
                    return foundFactory;
                }
            }
        }
        catch (Exception ex) {
            if (debug){
                ex.printStackTrace();
            }
        }

        String serviceId = "META-INF/services/" + property;
        // try to find services in CLASSPATH
        try {
            ClassLoader cl = JXPathContextFactory.class.getClassLoader();
            InputStream is = null;
            if (cl == null) {
                is = ClassLoader.getSystemResourceAsStream(serviceId);
            }
            else {
                is = cl.getResourceAsStream(serviceId);
            }

            if (is != null) {
                if (debug){
                    System.err.println("JXPath: found  " + serviceId);
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                foundFactory = rd.readLine();
                rd.close();

                if (debug){
                    System.err.println("JXPath: loaded from services: " + foundFactory);
                }
                if (foundFactory != null && !"".equals(foundFactory)) {
                    return foundFactory;
                }
            }
        }
        catch (Exception ex) {
            if (debug){
                ex.printStackTrace();
            }
        }

        return defaultFactory;
    }
}