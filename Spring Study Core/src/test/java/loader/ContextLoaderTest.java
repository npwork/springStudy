package loader;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import context.ApplicationContext;
import context.ApplicationContextException;
import context.configuration.ContextLoaderConfig;
import context.configuration.ClassPathContextLoaderConfig;
import context.configuration.ContextSettingsLoadException;
import context.load.ContextInitializationException;
import context.load.ContextLoader;
import entity.Customer;

public class ContextLoaderTest {

    private static final String CONTEXT_WITHOUT_BEANS = "test_context/context_loader/context_without_beans.xml";
    private static final String CONTEXT_WITH_3_BEANS = "test_context/context_loader/context_with_3_beans.xml";
    private static final String CONTEXT_WITHOUT_ID = "test_context/context_loader/context_without_id.xml";
    private static final String CONTEXT_WITHOUT_CLASS = "test_context/context_loader/context_without_class.xml";
    private static final String CONTEXT_WITH_ID_DUPLICATES = "test_context/context_loader/context_with_id_duplicates.xml";

    private static org.apache.log4j.Logger log = Logger.getLogger(ContextLoaderConfigTest.class);

    private ContextLoaderConfig beanLoaderConfig;

    @Before
    public void setUp() {
	beanLoaderConfig = new ClassPathContextLoaderConfig();
    }

    @Test
    public void context_should_start_without_beans() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITHOUT_BEANS);

	ContextLoader contextLoader = new ContextLoader(beanLoaderConfig);
	contextLoader.loadContext();
    }

    @Test
    public void context_should_start_and_load_all_beans() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITH_3_BEANS);

	ContextLoader contextLoader = new ContextLoader(beanLoaderConfig);
	ApplicationContext loadContext = contextLoader.loadContext();
	assertNotNull(loadContext.getBean("cusomerService1"));
	assertNotNull(loadContext.getBean("cusomerService2"));
	assertNotNull(loadContext.getBean("cusomerService3"));
    }

    @Test(expected = ContextInitializationException.class)
    public void context_should_not_start_without_bean_id() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITHOUT_ID);

	ContextLoader contextLoader = new ContextLoader(beanLoaderConfig);
	contextLoader.loadContext();
    }

    @Test(expected = ContextInitializationException.class)
    public void context_should_not_start_without_bean_class() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITHOUT_CLASS);

	ContextLoader contextLoader = new ContextLoader(beanLoaderConfig);
	contextLoader.loadContext();
    }

    @Test(expected = ContextInitializationException.class)
    public void context_should_not_start_with_id_duplicates() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITH_ID_DUPLICATES);

	ContextLoader contextLoader = new ContextLoader(beanLoaderConfig);
	contextLoader.loadContext();
    }
}
