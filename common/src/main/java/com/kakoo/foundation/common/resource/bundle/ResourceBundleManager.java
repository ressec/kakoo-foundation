/*
 * Copyright (c) 2019 by Kakoo Ltd.
 * ---------------------------------------------------------------------------
 * This file is part of the Kakoo Foundation Software (KFS) project which is
 * licensed under the Apache license version 2 and use is subject to license
 * terms. You should have received a copy of the license with the project's
 * artifact binaries and/or sources.
 *
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * ---------------------------------------------------------------------------
 */
package com.kakoo.foundation.common.resource.bundle;

import com.kakoo.foundation.common.resource.bundle.annotation.Bundle;
import com.kakoo.foundation.common.resource.bundle.annotation.BundleAnnotationTypeVisitor;
import eu.infomas.annotation.AnnotationDetector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.jeasy.props.api.PropertiesInjector;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;

/**
 * The Resource Bundle Manager is used to discover, register and access resource bundle entries.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
@UtilityClass
@Log4j
public class ResourceBundleManager
{
    /**
     * Locale of manager (set to the JVM default).
     */
    @Getter
    private static Locale locale = Locale.getDefault();

    /**
     * Thread-safe collection of resource bundles grouped by locale.
     */
    private static final Map<Locale, List<ResourceBundle>> BUNDLES = new ConcurrentHashMap<>();

    /**
     * Thread-safe collection of annotated classes.
     */
    private static final Map<Class<? extends Annotation>, Map<Class<?>, String>> ANNOTATION_CLASSES = new ConcurrentHashMap<>();

    /**
     * Resource bundle status type.
     */
    @Getter
    private static StatusType status = StatusType.UNKNOWN;

    /**
     * Properties injector.
     */
    private PropertiesInjector propertiesInjector;

    /**
     * Initializes the resource bundle manager.
     *
     * @throws ResourceBundleManagerException Thrown if the initialization of the resource bundle manager has failed.
     */
    @Synchronized
    public static final void initialize()
    {
        if (ResourceBundleManager.status == StatusType.UNKNOWN)
        {
            ResourceBundleManager.status = StatusType.INITIALIZING;

            // Instantiates a properties injector for direct injection of properties.
            propertiesInjector = aNewPropertiesInjector();

            // Automatically detect annotated elements.
            autoDetectAnnotated();

            ResourceBundleManager.status = StatusType.INITIALIZED;
        }
    }

    /**
     * Sets the default locale of the resource bundle manager.
     *
     * @param locale Locale to set.
     * @return Previous locale.
     */
    @Synchronized
    public static final Locale setLocale(final @NonNull Locale locale)
    {
        initialize();

        if (ResourceBundleManager.locale != locale)
        {
            ResourceBundleManager.locale = locale;
        }

        return ResourceBundleManager.locale;
    }

    /**
     * Checks if the given bundle file name for the given locale exist.
     *
     * @param baseBundleName Bundle base file name.
     * @return {@code True} if the given bundle file exist, {@code false} otherwise.
     */
    public static final boolean exist(final @NonNull String baseBundleName, final @NonNull Locale locale)
    {
        if (BUNDLES.get(locale) == null)
        {
            return false;
        }

        for (ResourceBundle bundle : BUNDLES.get(locale))
        {
            if (bundle.getBaseBundleName().equals(baseBundleName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a resource bundle value given its key.
     *
     * @param key Key to retrieve.
     * @return Resource bundle value.
     */
    public static final String get(final @NonNull String key)
    {
        return get(key, ResourceBundleManager.locale);
    }

    /**
     * Gets the resource bundle value of the given key.
     *
     * @param key Key to retrieve.
     * @param locale Locale to use.
     * @return The resource bundle value if found.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    public static final String get(final @NonNull String key, final @NonNull Locale locale)
    {
        return retrieve(lookup(key, locale), key, locale);
    }

    /**
     * Gets a resource bundle value given its key using an enumerated value.
     *
     * @param key Key to retrieve.
     * @return Resource bundle value.
     */
    public static final String get(final Enum<? extends IBundle> key)
    {
        return get(key, locale);
    }

    /**
     * Gets a resource bundle value given its key using an enumerated value.
     *
     * @param key Key to retrieve.
     * @param locale Locale to use.
     * @return Resource bundle value.
     */
    public static final String get(final Enum<? extends IBundle> key, final @NonNull Locale locale)
    {
        return get(key, locale, null);
    }

    /**
     * Gets a resource bundle value given its key using an enumerated value.
     *
     * @param key Key to retrieve.
     * @param locale Locale to use.
     * @return Resource bundle value.
     */
    public static final String get(final Enum<? extends IBundle> key, final @NonNull Locale locale, final Object... parameters)
    {
        initialize();

        return extract(key, locale, parameters);
    }

    /**
     * Gets the resource bundle value of the given key.
     *
     * @param key Key to retrieve.
     * @param locale Locale to use.
     * @return The resource bundle value if found.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    public static final String get(final @NonNull String key, final @NonNull Locale locale, final Object... parameters)
    {
        initialize();

        return retrieve(lookup(key), key, locale, parameters);
    }

    /**
     * Lookup the given key to determine in which bundle it is located.
     *
     * @param key Key to lookup.
     * @return If found, the resource bundle base name.
     * @throws ResourceBundleManagerException Thrown in case no resource bundle is registered.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String lookup(final @NonNull String key)
    {
        return lookup(key, locale);
    }

    /**
     * Lookup the given key to determine in which bundle it is located.
     *
     * @param key Key to lookup.
     * @return If found, the resource bundle base name.
     * @throws ResourceBundleManagerException Thrown in case no resource bundle is registered.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String lookup(final @NonNull String key, final @NonNull Locale locale)
    {
        String baseBundleName;

        if (BUNDLES.size() == 0)
        {
            throw new ResourceBundleManagerException("No resource bundle registered!");
        }

        // We are looking with the default locale.
        if (!BUNDLES.containsKey(locale))
        {
            // Do a lookup with the default locale.
            for (ResourceBundle bundle : BUNDLES.get(ResourceBundleManager.locale))
            {
                if (bundle.containsKey(key))
                {
                    baseBundleName =  bundle.getBaseBundleName();
                    if (baseBundleName != null)
                    {
                        return baseBundleName;
                    }
                }
            }
        }
        else
        {
            for (ResourceBundle bundle : BUNDLES.get(locale))
            {
                if (bundle.containsKey(key))
                {
                    return bundle.getBaseBundleName();
                }
            }
        }

        throw new ResourceBundleException(String.format("Can't register resource bundle find key: '%s'", key));
    }

    /**
     * Retrieves the given key.
     *
     * @param baseBundleName Base bundle file name.
     * @param key Key o retrieve.
     * @param locale Locale to use.
     * @return If found, the resource bundle value.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String retrieve(final @NonNull String baseBundleName, final @NonNull String key, final @NonNull Locale locale)
    {
        return retrieve(baseBundleName, key, locale, new Object[]{});
    }

    /**
     * Retrieves the given key.
     *
     * @param baseBundleName Base bundle file name.
     * @param key Key o retrieve.
     * @param locale Locale to use.
     * @return If found, the resource bundle value.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String retrieve(final @NonNull String baseBundleName,  final @NonNull String key, final @NonNull Locale locale, final Object... parameters)
    {
        if (BUNDLES.get(locale) == null)
        {
            // Try to register the resource bundle in the requested locale.
            register(baseBundleName, locale);
        }

        List<ResourceBundle> list = BUNDLES.get(locale);
        if (list == null)
        {
            // Try to load it from the default locale.
            list = BUNDLES.get(ResourceBundleManager.locale);
            if (list == null)
            {
                throw new ResourceBundleException(String.format("Can't find resource bundle file: '%s', locale: '%s'", baseBundleName, locale));
            }
        }

        for (ResourceBundle bundle : list)
        {
            if (bundle.getBaseBundleName().equals(baseBundleName))
            {
                return MessageFormat.format(bundle.getString(key), parameters);
            }
        }

        throw new ResourceBundleException(String.format("Can't find resource bundle key: '%s'", key));
    }

    /**
     * Registers a resource bundle file for the current locale.
     *
     * @param baseBundleName Base bundle file name.
     */
    @Synchronized
    public static final void register(final @NonNull String baseBundleName)
    {
        register(baseBundleName, locale);
    }

    /**
     * Registers a resource bundle file for the given locale.
     * <p>
     * If the resource bundle in the given locale cannot be found, the resource bundle in the default locale is
     * registered.
     *
     * @param baseBundleName Base bundle file name.
     * @param locale Locale.
     */
    @Synchronized
    public static final void register(final @NonNull String baseBundleName, final @NonNull Locale locale)
    {
        initialize();

        ResourceBundle bundle;
        List<ResourceBundle> resources;

        // Register the bundle using the given locale if not the same as the current one.
        if (!exist(baseBundleName, locale))
        {
            resources = BUNDLES.get(locale);
            if (resources == null)
            {
                resources = new ArrayList<>();
            }

            bundle = ResourceBundle.getBundle(baseBundleName, locale);
            if (bundle != null)
            {
                resources.add(ResourceBundle.getBundle(baseBundleName, locale));
                BUNDLES.put(locale, resources);
                log.info(String.format("Registered resource bundle file: '%s', locale: '%s'", baseBundleName, locale));
            }
            else
            {
                log.warn(String.format("Can't find resource bundle file: '%s', locale: '%s'", baseBundleName, locale));
            }
        }
        else
        {
            log.info(String.format("Already registered resource bundle file: '%s', locale: '%s'", baseBundleName, locale));
        }

//        // Also register the bundle using the default locale.
//        if (!exist(baseBundleName, Locale.getDefault()))
//        {
//            resources = BUNDLES.get(Locale.getDefault());
//            if (resources == null)
//            {
//                resources = new ArrayList<>();
//            }
//
//            bundle = ResourceBundle.getBundle(baseBundleName);
//            resources.add(bundle);
//            BUNDLES.put(Locale.getDefault(), resources);
//            log.info(String.format("Registered resource bundle file: '%s', for default locale: '%s'", baseBundleName, Locale.getDefault()));
//        }
    }

    /**
     * Registers an annotated class.
     *
     * @param annotationClass Annotation class.
     * @param annotatedClass Annotated class.
     */
    public static final void register(final @NonNull Class<? extends Annotation> annotationClass, final @NonNull Class<?> annotatedClass)
    {
        initialize();

        if (annotationClass.isAssignableFrom(Bundle.class))
        {
            registerBundleAnnotatedClass(annotationClass, annotatedClass);
        }
    }

    /**
     * Registers a class annotated with the {@link Bundle} annotation.
     *
     * @param annotationClass Annotation class.
     * @param annotatedClass Annotated class.
     */
    private static void registerBundleAnnotatedClass(final @NonNull Class<? extends Annotation> annotationClass, final @NonNull Class<?> annotatedClass)
    {
        // Extract the resource bundle file name.
        Bundle annotation = annotatedClass.getAnnotation(Bundle.class);
        String baseBundleName = annotation.file();

        if (check(annotationClass, annotatedClass, baseBundleName))
        {
            throw new ResourceBundleException(
                    String.format("Already registered annotated class: '%s' for annotation: '%s' with resource bundle file: '%s'",
                            annotatedClass.getName(), annotationClass.getName(), baseBundleName));
        }

        // Keep a trace of this association between the annotation, the annotated class and the bundle file name.
        update(annotationClass, annotatedClass, baseBundleName);
    }

    /**
     * Checks if the given annotation class is already registered for the given annotated class.
     *
     * @param annotationClass Annotation class.
     * @param annotatedClass Annotated class.
     * @param baseBundleName Base bundle file name.
     * @return {@code True} if already registered, {@code false} otherwise.
     */
    private static boolean check(final @NonNull Class<? extends Annotation> annotationClass, final @NonNull Class<?> annotatedClass, final @NonNull String baseBundleName)
    {
        Map<Class<?>, String> classes = ANNOTATION_CLASSES.get(annotationClass);
        if (classes != null)
        {
            String filename = classes.get(annotatedClass);
            return (filename != null && filename.equals(baseBundleName));
        }

        return false;
    }

    /**
     * Updates the data structure keeping a trace of the association between the annotation class, the annotated class and the resource bundle file name.
     *
     * @param annotationClass Annotation class.
     * @param annotatedClass Annotated class.
     * @param baseBundleName Base bundle file name.
     */
    private static void update(final @NonNull Class<? extends Annotation> annotationClass, final @NonNull Class<?> annotatedClass, final @NonNull String baseBundleName)
    {
        String filename;

        Map<Class<?>, String> classes = ANNOTATION_CLASSES.get(annotationClass);
        if (classes == null)
        {
            classes = new HashMap<>();
        }

        filename = classes.get(annotatedClass);
        if (filename == null)
        {
            classes.put(annotatedClass, baseBundleName);
            ANNOTATION_CLASSES.put(annotationClass, classes);

            register(baseBundleName);
        }
    }

    /**
     * Clears all registered resource bundles.
     * <p>
     * Only the directly registered resource bundles are cleared, not the ones registered through annotations.
     */
    @Synchronized
    public static final void clear()
    {
        initialize();

        // Clear all the bundle files loaded directly.
        BUNDLES.clear();

        // Reload the annotated elements.
        reloadAnnotated();

        log.info("Cleared the resource bundle cache");
    }

    /**
     * Returns the number of resource bundle files registered for the current locale.
     *
     * @return Number of resource bundle registered.
     */
    public static final int getCount()
    {
        return getCount(locale);
    }

    /**
     * Returns the number of resource bundle files registered for a given locale.
     *
     * @param locale Locale.
     * @return Number of resource bundle registered.
     */
    public static final int getCount(final @NonNull Locale locale)
    {
        initialize();

        return BUNDLES.get(locale).size();

    }

    /**
     * Returns the list of bundle file base name registered for the current locale.
     * <p>
     * @return List of resource bundle base name registered.
     */
    public static final List<String> getList()
    {
        return getList(locale);
    }

    /**
     * Returns a list of the bundle file names registered for a given locale.
     *
     * @param locale Locale.
     * @return List of resource bundle base name registered.
     */
    public static final List<String> getList(final @NonNull Locale locale)
    {
        initialize();

        List<String> result = new ArrayList<>();
        List<ResourceBundle> resources = BUNDLES.get(locale);

        if (resources != null)
        {
            for (ResourceBundle bundle : resources)
            {
                result.add(bundle.getBaseBundleName());
            }
        }

        return result;
    }

    /**
     * Automatically detect annotated elements on the classpath.
     *
     * @see BundleAnnotationTypeVisitor
     * @see AnnotationDetector
     */
    private static void autoDetectAnnotated()
    {
        try
        {
            final BundleAnnotationTypeVisitor visitor = new BundleAnnotationTypeVisitor();
            final AnnotationDetector detector = new AnnotationDetector(visitor);
            detector.detect();

            visitor.delegateRegistration();
        }
        catch (Exception e)
        {
            ResourceBundleManager.status = StatusType.ERROR;
            throw new ResourceBundleException(e.getMessage(), e);
        }
    }

    /**
     * Extracts the resource bundle message.
     *
     * @param key Key of the resource bundle.
     * @param parameters Parameters for message formatting.
     * @return Formatted message.
     */
    private static String extract(final @NonNull Enum<? extends IBundle> key, final Object... parameters)
    {
        return extract(key, locale, parameters);
    }

    /**
     * Extracts the resource bundle message.
     *
     * @param key Key of the resource bundle.
     * @param locale Locale to use.
     * @param parameters Parameters for message formatting.
     * @return Formatted message.
     */
    private static String extract(final @NonNull Enum<? extends IBundle> key, final @NonNull Locale locale, final Object... parameters)
    {
        Bundle annotation = key.getDeclaringClass().getAnnotation(Bundle.class);
        String aKey = ((IBundle) key).getKey();

        aKey = annotation.root().endsWith(".") ? annotation.root() + aKey : annotation.root() + "." + aKey;

        return get(aKey, locale, parameters);
    }

    /**
     * Reload the resource bundles associated with annotated classes.
     */
    private static void reloadAnnotated()
    {
        for (Map<Class<?>, String> annotationClass : ANNOTATION_CLASSES.values())
        {
            for (String baseBundleName : annotationClass.values())
            {
                register(baseBundleName);
            }
        }
    }

    /**
     * Injects the properties according to annotations declared on the given object.
     *
     * @param o Object for which to inject properties.
     */
    public static final void injectProperties(final @NonNull Object o)
    {
        initialize();

        propertiesInjector.injectProperties(o);
    }
}
