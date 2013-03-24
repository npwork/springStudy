package context;

import java.util.HashMap;
import java.util.Map;

import context.configuration.model.xml.Root;

/**
 * Represents running application context
 * 
 * @author niko
 * 
 */
public class ApplicationContext {
    private Root inmemoryContext;
    private Map<String, Object> loadedObjects = new HashMap<String, Object>();

    /**
     * @return bean from context based on beanName
     */
    public Object getBean(String beanName) {
	return loadedObjects.get(beanName);
    }

    /**
     * @param name
     *            of the object in context
     * @param o
     *            to persist
     * @return true if no bean with name had been aded yet. false if bean
     *         already present
     */
    public boolean addObjectToContext(String name, Object o) {
	// TODO NOT THREAD SAFETY YET
	Object object = loadedObjects.get(name);
	if (object == null) {
	    return loadedObjects.put(name, o) != null;
	} else {
	    return false;
	}
    }

    public Root getInmemoryContext() {
	return inmemoryContext;
    }

    public void setInmemoryContext(Root inmemoryContext) {
	this.inmemoryContext = inmemoryContext;
    }

    public Map<String, Object> getLoadedObjects() {
	return loadedObjects;
    }

    public void setLoadedObjects(Map<String, Object> loadedObjects) {
	this.loadedObjects = loadedObjects;
    }

}
