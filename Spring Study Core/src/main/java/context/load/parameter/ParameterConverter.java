package context.load.parameter;

import java.lang.reflect.Method;

public class ParameterConverter {

    public static Object convertParameter(Method m, String parameter) throws ContextParameterException {
	Class<?>[] parameterTypes = m.getParameterTypes();
	if (parameterTypes.length == 0 || parameterTypes.length > 1) {
	    throw new IllegalArgumentException("Method can't be with more than one parameter");
	}
	Class<?> class1 = parameterTypes[0];
	String name = class1.getName();
	try {
	    if (name.equals(String.class.getName())) {
		return parameter;
	    } else if (name.equals(Integer.class.getName())) {
		return Integer.parseInt(parameter);
	    } else if (name.equals(Long.class.getName())) {
		return Long.parseLong(parameter);
	    }
	} catch (Exception e) {
	    throw new ContextParameterException("Parameter cannot be cast to method required parameter type.", e);
	}

	throw new IllegalArgumentException("Unknown type of the parameter");
    }
}
