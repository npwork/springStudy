package loader;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import context.ApplicationContextException;
import context.configuration.ClassPathContextLoaderConfig;
import context.configuration.ContextLoaderConfig;
import context.configuration.ContextSettingsLoadException;
import context.load.ContextInitializationException;
import context.load.ContextLoader;

public class ContextLoaderConfigTest {
    private static org.apache.log4j.Logger log = Logger.getLogger(ContextLoaderConfigTest.class);

    private ContextLoaderConfig beanLoaderConfig;

    @Before
    public void setUp() {
	beanLoaderConfig = new ClassPathContextLoaderConfig();
    }

    @Test(expected = ContextInitializationException.class)
    public void classpath_context_loader_cant_have_filename_eq_to_null() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(null);
	new ContextLoader(beanLoaderConfig);
    }

    @Test(expected = ContextInitializationException.class)
    public void classpath_context_loader_cant_have_empty_filename() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName("");
	new ContextLoader(beanLoaderConfig);
    }

    @Test(expected = ContextSettingsLoadException.class)
    public void classpath_context_loader_should_have_settings_file() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName("test123123123.xml");
	new ContextLoader(beanLoaderConfig);
    }
}
