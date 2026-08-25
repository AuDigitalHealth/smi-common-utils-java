package au.gov.nehta.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Singleton class that finds resources and provides access to them.
 */
public class ResourceLocator {

    public enum SearchOrderItem {

        CLASSPATH,

        FILE_SYSTEM,

        REMOTE_URL

    }

    private static final List<SearchOrderItem> DEFAULT_SEARCH_ORDER = List.of(
            SearchOrderItem.FILE_SYSTEM, SearchOrderItem.CLASSPATH);

    private static final ResourceLocator instance = new ResourceLocator();

    /**
     * Returns an instance of ResourceLocator.
     *
     * @return ResourceLocator instance.
     */
    public static ResourceLocator getInstance() {
        return instance;
    }

    private ResourceLocator() {
    }

    /**
     * Looks for a resource first file system, then the classpath. The system
     * class loader is used to look up the resource from the classpath.
     *
     * @param resourcePath resource to look up. Cannot be null nor an blank string.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream find(String resourcePath) {
        return find(resourcePath, null, DEFAULT_SEARCH_ORDER);
    }

    /**
     * Looks for a resource first file system, then the classpath.
     *
     * @param resourcePath   resource to look up. Cannot be null nor an blank string.
     * @param referenceClass the classloader for this reference class is used to look up the
     *                       resource. The system class loader is used if this parameter is not
     *                       provided.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream find(String resourcePath, Class<?> referenceClass) {
        return find(resourcePath, referenceClass, DEFAULT_SEARCH_ORDER);
    }

    /**
     * Looks for a resource from the classpath, file system or as a remote URL.
     * The 'searchOrder' parameter determines where to look and in what order.
     * If the classpath search is in the search order, the system class loader
     * is used to look up the resource.
     *
     * @param resourcePath resource to look up. Cannot be null nor an blank string.
     * @param searchOrder  where to look up the resource. If it is null or empty, the default
     *                     search order is used, which is to look in the file system, then
     *                     the classpath.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream find(String resourcePath, List<SearchOrderItem> searchOrder) {
        return find(resourcePath, null, searchOrder);
    }

    /**
     * Looks for a resource from the classpath, file system or as a remote URL.
     * The 'searchOrder' parameter determines where to look and in what order.
     *
     * @param resourcePath   resource to look up. Cannot be null nor an blank string.
     * @param referenceClass the classloader for this reference class is used to look up the
     *                       resource. The system class loader is used if this parameter is not
     *                       provided.
     * @param searchOrder    where to look up the resource. If it is null or empty, the default
     *                       search order is used, which is to look in the file system, then
     *                       the classpath.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream find(String resourcePath, Class<?> referenceClass,
                            List<SearchOrderItem> searchOrder) {
        assert (resourcePath != null) : "'resourcePath' is null.";
        assert (!resourcePath.trim().isEmpty()) : "'resourcePath' is a blank string.";

        List<SearchOrderItem> actualSearchOrder = searchOrder;
        if (ArgumentUtils.isNullOrEmpty(searchOrder)) {
            actualSearchOrder = DEFAULT_SEARCH_ORDER;
        }

        for (SearchOrderItem currSearchOrderItem : actualSearchOrder) {
            if (currSearchOrderItem == SearchOrderItem.FILE_SYSTEM) {
                try {
                    return new FileInputStream(resourcePath);
                } catch (FileNotFoundException e) {
                    // Resource can't be found in file system; keep looking
                }
            } else if (currSearchOrderItem == SearchOrderItem.CLASSPATH) {
                InputStream in = getStreamFromClasspath(resourcePath, referenceClass);
                if (in != null) {
                    return in;
                }
            } else if (currSearchOrderItem == SearchOrderItem.REMOTE_URL) {
                try {
                    URL url = new URL(resourcePath);
                    return url.openStream();
                } catch (MalformedURLException e) {
                    // The resource path isn't a URL; keep looking
                } catch (IOException e) {
                    // The URL can't be read; keep looking
                }
            }
        }
        return null;
    }

    /**
     * Returns the byte stream to a resource from the classpath using the system
     * class loader to find the resource.
     *
     * @param resourcePath resource to look up. Cannot be null nor an blank string.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream getStreamFromClasspath(String resourcePath) {
        return getStreamFromClasspath(resourcePath, null);
    }

    /**
     * Returns the byte stream to a resource from the classpath using a particular
     * class's class loader to find the resource.
     *
     * @param resourcePath   resource to look up. Cannot be null nor an blank string.
     * @param referenceClass the classloader for this reference class is used to look up the
     *                       resource. The system class loader is used if this parameter is not
     *                       provided.
     * @return byte stream to the resource or null if it cannot be found.
     */
    public InputStream getStreamFromClasspath(String resourcePath,
                                              Class<?> referenceClass) {
        assert (resourcePath != null) : "'resourcePath' is null.";
        assert (!resourcePath.trim().isEmpty()) : "'resourcePath' is a blank string.";

        ClassLoader classLoader = getClassLoader(referenceClass);
        return classLoader.getResourceAsStream(resourcePath);
    }

    /**
     * Returns the URL to a resource from the classpath using the system class
     * loader to find the resource.
     *
     * @param resourcePath resource to look up. Cannot be null nor an blank string.
     * @return URL to the resource or null if it cannot be found.
     */
    public URL getUrlFromClasspath(String resourcePath) {
        return getUrlFromClasspath(resourcePath, null);
    }

    /**
     * Returns the URL to a resource from the classpath using a particular class's
     * class loader to find the resource.
     *
     * @param resourcePath   resource to look up. Cannot be null nor an blank string.
     * @param referenceClass the classloader for this reference class is used to look up the
     *                       resource. The system class loader is used if this parameter is not
     *                       provided.
     * @return URL to the resource or null if it cannot be found.
     */
    public URL getUrlFromClasspath(String resourcePath, Class<?> referenceClass) {
        assert (resourcePath != null) : "'resourcePath' is null.";
        assert (!resourcePath.trim().isEmpty()) : "'resourcePath' is a blank string.";

        ClassLoader classLoader = getClassLoader(referenceClass);
        return classLoader.getResource(resourcePath);
    }

    private ClassLoader getClassLoader(Class<?> referenceClass) {
        ClassLoader classLoader = null;

        if (referenceClass != null) {
            classLoader = referenceClass.getClassLoader();
        }

        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        return classLoader;
    }
}
