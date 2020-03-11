package com.tt.net.cpool.util;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class PropertyElf {

    private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");

    private PropertyElf() {

    }

    public static Set<String> getPropertyName(final Class<?> targetClass) {
        HashSet<String> set = new HashSet<>();
        Matcher matcher = GETTER_PATTERN.matcher("");
        for ( Method method : targetClass.getMethods() ) {
            String name = method.getName();
        }

        return null;
    }

    public static Object getProperty(final String propName, final Object target) {
        try {
            String capitalized = "get" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
            Method method = target.getClass().getMethod(capitalized);
            return method.invoke(target);
        } catch ( Exception e ) {
            try {
                String capitalized = "is" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
                Method method = target.getClass().getMethod(capitalized);
                return method.invoke(target);
            } catch ( Exception e2 ) {
                return null;
            }
        }
    }

    public static Properties copyProperties(final Properties props) {
        Properties copy = new Properties();
        props.forEach((key, value) -> copy.setProperty(key.toString(), value.toString()));
        return copy;
    }

    private static void setProperty(final Object target, final String propName, final Object propValue, final List<Method> methods) {

        final Logger logger = LoggerFactory.getLogger(PropertyElf.class);

        String methodName = "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
        Method writeMethod = methods.stream().filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1).findFirst().orElse(null);

        if ( writeMethod == null ) {
            logger.error("Property {} does not exists on target {}", propName, target.getClass());
            throw new RuntimeException(String.format("Property %s does not exist on target %s", propName, target.getClass()));
        }

        try {
            Class<?> paramClass = writeMethod.getParameterTypes()[0];
            if (paramClass == int.class) {
                writeMethod.invoke(target, Integer.parseInt(propValue.toString()));
            }
            else if (paramClass == long.class) {
                writeMethod.invoke(target, Long.parseLong(propValue.toString()));
            }
            else if (paramClass == boolean.class || paramClass == Boolean.class) {
                writeMethod.invoke(target, Boolean.parseBoolean(propValue.toString()));
            }
            else if (paramClass == String.class) {
                writeMethod.invoke(target, propValue.toString());
            }
            else {
                try {
                    logger.debug("Try to create a new instance of \"{}\"", propValue.toString());
                    writeMethod.invoke(target, Class.forName(propValue.toString()).newInstance());
                }
                catch ( InstantiationException | ClassNotFoundException e ) {
                    logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", propValue.toString());
                    writeMethod.invoke(target, propValue);
                }
            }

        } catch ( Exception e ) {
            logger.error("Failed to set property {} on target {}", propName, target.getClass(), e);
            throw new RuntimeException(e);
        }
    }
}
