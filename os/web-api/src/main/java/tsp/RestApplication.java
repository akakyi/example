package tsp;

import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationPath("/tsp/rest")
public class RestApplication extends Application {

    //    public RestApplication() {
////        BeanConfig beanConfig = new BeanConfig();
////        beanConfig.setVersion("1.0.2");
////        beanConfig.setSchemes(new String[]{"http"});
////        beanConfig.setHost("localhost:8080/web-api/");
////        beanConfig.setBasePath("tsp.rest");
////        beanConfig.setResourcePackage("tsp.rest.resource");
////        beanConfig.setScan(true);
//    }
//
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        try {
            classes.addAll(getClasses("tsp.rest.resource"));
            classes.addAll(getClasses("tsp.utils"));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("In Application startup", e);
        }

        return classes;
    }

    private static Set<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();

        while (resources.hasMoreElements()) {
            final URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            final List<Class<?>> packClasses = findClasses(directory, packageName);
            for (Class clazz : packClasses) {
                if (
                    clazz.isAnnotationPresent(Path.class)
                        || clazz.isAnnotationPresent(Provider.class)
                        || clazz.isAnnotationPresent(Singleton.class)
                    ) {
                    classes.add(clazz);
                }
            }
        }

        return classes;

    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }

        return classes;

    }
}
