package org.dianahep.mdproxy;

/*
 * Matlab Dynamic Proxy Handler
 * 
 * The root4j package dynamically creates classes in the RootClassLoader based on file contents
 * Coming from a different classloader, these classes are not available to the Matlab command line 
 * This class uses the java.lang.reflect.Proxy methodology to create a dynamic proxy
 * for these internally generated classes under the system classloader
 * These proxy classes are then available to the Matlab command line
 * 
 * Note this requires an interface definition for the class in org.dianahep.root4j.interfaces
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MDProxyHandler implements
java.lang.reflect.InvocationHandler
{
	Object obj;
	public MDProxyHandler(Object obj)
	{ this.obj = obj; }

	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
	{
		Object ret = null;
		try {
			ret = m.invoke(obj, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw e;
		}

		// Wrap return objects from internally generated classes
		String retClass = ret.getClass().getName();
		if (retClass.startsWith("org.dianahep.root4j.proxy")) {
			String interfaceClass = "org.dianahep.root4j.interfaces." + retClass.substring(retClass.lastIndexOf(".") + 1);
			try {
				ret = Proxy.newProxyInstance(Class.forName(interfaceClass).getClassLoader(), new Class[] { Class.forName(interfaceClass)}, new MDProxyHandler(ret));
				System.out.println("Returning class!");
			} catch (ClassNotFoundException e) {
				System.out.println("Class " + interfaceClass + " not found: " + e.getMessage());
			}
		}

		return ret;
	}
}