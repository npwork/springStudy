package loader;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import context.ApplicationContext;
import context.ApplicationContextException;
import context.configuration.ClassPathContextLoaderConfig;
import context.configuration.ContextLoaderConfig;
import context.load.ContextInitializationException;
import context.load.ContextLoader;

public class ParametersTest {
    private static final String CONTEXT_WITH_PARAMETERS = "test_context/parameters/context_with_parameters.xml";
    private static final String CONTEXT_WITH_WRONG_PARAMETERS_TYPE = "test_context/parameters/context_with_wrong_parameters_type.xml";
    private static final String CONTEXT_WITHOUT_ALL_PARAMETERS = "test_context/parameters/context_without_all_parameters.xml";

    private static org.apache.log4j.Logger log = Logger.getLogger(ParametersTest.class);

    private ContextLoaderConfig beanLoaderConfig;

    @Before
    public void setUp() {
	beanLoaderConfig = new ClassPathContextLoaderConfig();
    }
    
    @Test
    public void context_should_start_with_parameters() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITH_PARAMETERS);

	new ContextLoader(beanLoaderConfig).loadContext();
    }

    @Test(expected = ContextInitializationException.class)
    public void context_should_not_start_with_wrong_parameter_type() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITH_WRONG_PARAMETERS_TYPE);

	new ContextLoader(beanLoaderConfig).loadContext();
    }

    @Test
    public void context_should_start_without_all_params() throws ApplicationContextException {
	beanLoaderConfig.setContextFileName(CONTEXT_WITHOUT_ALL_PARAMETERS);

	ApplicationContext loadContext = new ContextLoader(beanLoaderConfig).loadContext();
	assertNotNull(loadContext.getBean("customer1"));
	assertNotNull(loadContext.getBean("customer2"));
	assertNotNull(loadContext.getBean("customer3"));
    }
}
