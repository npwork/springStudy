package context.configuration;

public interface ContextLoaderConfig {

    /**
     * @return Context settings file name
     */
    String getContextFileName();

    /**
     * @param contextFileName
     *            Context setiings file name
     */
    void setContextFileName(String contextFileName);
}
