package context.load;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import context.ApplicationContext;
import context.ApplicationContextException;
import context.configuration.ContextLoaderConfig;
import context.configuration.ClassPathContextLoaderConfig;
import context.configuration.ContextSettingsLoadException;
import context.configuration.model.xml.Bean;
import context.configuration.model.xml.Property;
import context.configuration.model.xml.Root;
import context.load.parameter.ContextParameterException;
import context.load.parameter.ParameterConverter;

import loader.ContextLoaderTest;

/**
 * Responsible for application context preparing and loading
 * 
 * @author niko
 * 
 */
public class ContextLoader {

    private static org.apache.log4j.Logger log = Logger.getLogger(ContextLoaderTest.class);

    private ContextLoaderConfig beanLoaderConfig;

    /**
     * Current System Classloader
     */
    private ClassLoader systemClassLoader;

    /**
     * Context loaded into memory from config file
     */
    private Root loadedContext;

    public ContextLoader(ContextLoaderConfig beanLoaderConfig) throws ApplicationContextException {
	this.beanLoaderConfig = beanLoaderConfig;
	systemClassLoader = ContextLoader.class.getClassLoader();
	validateInput();
	loadedContext = readContextSettingsFile();
    }

    /**
     * Validates that all parameters are present until loading
     */
    private void validateInput() throws ContextInitializationException {
	if (beanLoaderConfig == null)
	    throw new ContextInitializationException("No bean config found.");
	if (beanLoaderConfig.getContextFileName() == null)
	    throw new ContextInitializationException("No context file name found.");
	if (beanLoaderConfig.getContextFileName().isEmpty())
	    throw new ContextInitializationException("Context file name cann't be null.");
    }

    private Root readContextSettingsFile() throws ContextSettingsLoadException {
	String contextFileName = beanLoaderConfig.getContextFileName();
	InputStream resourceAsStream = systemClassLoader.getResourceAsStream(contextFileName);
	if (resourceAsStream == null) {
	    throw new ContextSettingsLoadException(String.format("Can't find resource: %s", contextFileName));
	}
	JAXBContext jaxbContext;
	Root root;
	try {
	    jaxbContext = JAXBContext.newInstance(Root.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    root = (Root) jaxbUnmarshaller.unmarshal(resourceAsStream);
	} catch (JAXBException e) {
	    throw new ContextSettingsLoadException("Could not unmarshal properties file.", e);
	}
	return root;
    }

    /**
     * Creates context from loaded into memory context properties
     */
    public ApplicationContext loadContext() throws ContextInitializationException {
	ApplicationContext appContextResult = new ApplicationContext();
	appContextResult.setInmemoryContext(loadedContext);

	ArrayList<Bean> beans = loadedContext.getBeans();
	if (beans == null || beans.isEmpty()) {
	    return appContextResult;
	}

	for (Bean b : beans) {
	    String className = b.getClassName();
	    String beanId = b.getId();
	    ArrayList<Property> myProperties = b.getMyProperties();

	    if (beanId == null || beanId.isEmpty()) {
		throw new ContextInitializationException("Can not instanciate an instance of of the bean without id.");
	    }

	    if (className == null || className.isEmpty()) {
		throw new ContextInitializationException(String.format("Class name for the bean id: %s, can not be empty.", beanId));
	    }

	    // avoid duplicate beans
	    if (appContextResult.getBean(beanId) != null) {
		throw new ContextInitializationException(String.format("Can't create duplicate bean with id = %s. ", beanId));
	    }

	    Object tryToAddToContext = tryToAddToContext(className, beanId, myProperties);
	    appContextResult.addObjectToContext(beanId, tryToAddToContext);
	    log.info(String.format("Loaded bean with id: %s  [%s]", beanId, className));

	}

	return appContextResult;
    }

    // Tries to generate an instance or throws an exception
    private Object tryToAddToContext(String className, String beanId, List<Property> properties) throws ContextInitializationException {
	Object newInstance;
	try {
	    Class<?> loadClass = systemClassLoader.loadClass(className);
	    newInstance = loadClass.newInstance();
	} catch (Exception e) {
	    throw new ContextInitializationException(String.format("Couldn't find class with the name: %s", className), e);
	}
	if (properties != null && properties.size() != 0) {
	    try {
		tryToAddProperties(newInstance, className, properties);
	    } catch (ContextParameterException e) {
		throw new ContextInitializationException(String.format("Can't convert params for beanId: %s", beanId), e);
	    }
	}
	return newInstance;
    }

    private void tryToAddProperties(Object instance, String className, List<Property> properties) throws ContextInitializationException,
	    ContextParameterException {
	Class<? extends Object> class1 = instance.getClass();
	for (Property p : properties) {
	    if (p.getName() == null && p.getName().isEmpty())
		continue;

	    List<Method> methodsByName = getMethodsByName(class1, new String("set" + p.getName()));
	    int methodsLength = methodsByName.size();
	    if (methodsLength == 0) {
		throw new ContextInitializationException(String.format(
			"Can not initialize class instance %s with parameter %s, because there is no set method for a property.", className, p.getName()));
	    } else if (methodsLength > 1) {
		throw new ContextInitializationException(String.format(
			"Can not initialize class instance %s with parameter %s, because of the method ambiguity.", className, p.getName()));
	    } else {
		Method method = methodsByName.get(0);
		Object convertParameter = ParameterConverter.convertParameter(method, p.getValue());
		try {
		    // TODO ref?
		    method.invoke(instance, convertParameter);
		} catch (Exception e) {
		    throw new ContextInitializationException(String.format("Couldn't call method %s with parameters %s", method.getName(), convertParameter), e);
		}
		log.info(String.format("Set parameter %s, of the %s instance, to the value %s ", p.getName(), className, p.getValue()));
	    }
	}
    }

    // Returns all overloaded methods with specified method name
    // TODO Optimize iteration over array
    private List<Method> getMethodsByName(Class classVar, String methodName) {
	List<Method> resultList = new ArrayList<Method>();
	Method[] methods = classVar.getMethods();
	if (methods == null || methods.length == 0)
	    return resultList;

	for (Method m : methods) {
	    if (m.getName().equalsIgnoreCase(methodName)) {
		resultList.add(m);
	    }
	}

	return resultList;
    }

    public ContextLoaderConfig getBeanLoaderConfig() {
	return beanLoaderConfig;
    }

    public void setBeanLoaderConfig(ContextLoaderConfig beanLoaderConfig) {
	this.beanLoaderConfig = beanLoaderConfig;
    }

    public ClassLoader getSystemClassLoader() {
	return systemClassLoader;
    }

    public void setSystemClassLoader(ClassLoader systemClassLoader) {
	this.systemClassLoader = systemClassLoader;
    }

    public Root getLoadedContext() {
	return loadedContext;
    }

    public void setLoadedContext(Root loadedContext) {
	this.loadedContext = loadedContext;
    }

}
