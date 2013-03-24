package context.configuration;

/**
 * Application context configuration model placed in classpath
 * 
 * @author niko
 * 
 */
public class ClassPathContextLoaderConfig implements ContextLoaderConfig {

    /**
     * Context settings file name placed in the classpath
     */
    private String contextFileName = "context.xml";

    public String getContextFileName() {
	return contextFileName;
    }

    public void setContextFileName(String contextFileName) {
	this.contextFileName = contextFileName;
    }

}
