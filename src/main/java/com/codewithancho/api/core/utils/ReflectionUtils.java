package com.codewithancho.api.core.utils;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {

    /**
     *
     * @param packageName Which package to use to load the commands.
     * @return The classes that are loaded from the given package path.
     * @throws ClassNotFoundException When a class is not found, throw an error why and which one it is.
     */
    public static Set<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();

        String path = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL resource = loader.getResource(path);

        if(resource == null) return classes;
        File directory = new File(resource.getFile());
        if(!directory.exists()) return classes;

        for (String file : directory.list()) {
            if(file.endsWith(".class")) {
                String className = packageName + '.' + file.substring(0, file.length() - 6);
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }
}