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

import com.kakoo.foundation.common.exception.NotImplementedException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.jeasy.props.api.PropertiesInjector;

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
     * <p>
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

            // TODO Detect annotated classes

            ResourceBundleManager.status = StatusType.INITIALIZED;
        }
    }

    /**
     * Sets the locale.
     * <p>
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
     * <p>
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
     * <p>
     * @param key Key to retrieve.
     * @return Resource bundle value.
     */
    public static final String get(final @NonNull String key)
    {
        return get(key, ResourceBundleManager.locale);
    }

    /**
     * Gets the resource bundle value of the given key.
     * <p>
     * @param key Key to retrieve.
     * @param locale Locale to use.
     * @return The resource bundle value if found.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    public static final String get(final @NonNull String key, final @NonNull Locale locale)
    {
        return  retrieve(lookup(key), key, locale);
    }

    /**
     * Lookup the given key to determine in which bundle it is located.
     * <p>
     * @param key Key to lookup.
     * @return If found, the resource bundle base name.
     * @throws ResourceBundleManagerException Thrown in case no resource bundle is registered.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String lookup(final @NonNull String key)
    {
        if (BUNDLES.size() == 0)
        {
            throw new ResourceBundleManagerException("No resource bundle registered!");
        }

        // We are looking with the default locale.
        for (ResourceBundle bundle : BUNDLES.get(Locale.getDefault()))
        {
            if (bundle.containsKey(key))
            {
                return bundle.getBaseBundleName();
            }
        }

        throw new ResourceBundleException(String.format("Can't find key: '%s'", key));
    }

    /**
     * Retrieves the given key.
     * <p>
     * @param baseBundleName Base bundle file name.
     * @param key Key o retrieve.
     * @param locale Locale to use.
     * @return If found, the resource bundle value.
     * @throws ResourceBundleException Thrown in case the given key cannot be found.
     */
    private static String retrieve(final @NonNull String baseBundleName,  final @NonNull String key, final @NonNull Locale locale)
    {
        List<ResourceBundle> list = BUNDLES.get(locale);
        if (list == null)
        {
            throw new ResourceBundleException(String.format("Can't find bundle base name: '%s' for locale: '%s'", baseBundleName, locale));
        }

        for (ResourceBundle bundle : list)
        {
            if (bundle.getBaseBundleName().equals(baseBundleName))
            {
                return bundle.getString(key);
            }
        }

        throw new ResourceBundleException(String.format("Can't find key: '%s'", key));
    }

    /**
     * Registers a resource bundle file for the current locale.
     * <p>
     * @param baseBundleName Base bundle file name.
     */
    @Synchronized
    public static final void register(final @NonNull String baseBundleName)
    {
        initialize();

        register(baseBundleName, locale);
    }

    /**
     * Registers a resource bundle file for the given locale.
     * <p>
     * @param baseBundleName Base bundle file name.
     * @param locale Locale.
     */
    @Synchronized
    public static final void register(final @NonNull String baseBundleName, final @NonNull Locale locale)
    {
        initialize();

        List<ResourceBundle> resources;

        // Register the bundle using the default locale.
        if (!exist(baseBundleName, Locale.getDefault()))
        {
            resources = BUNDLES.get(Locale.getDefault());
            if (resources == null)
            {
                resources = new ArrayList<>();
            }

            resources.add(ResourceBundle.getBundle(baseBundleName, Locale.getDefault()));
            BUNDLES.put(Locale.getDefault(), resources);
        }

        // Register the bundle using the given locale.
        if (!exist(baseBundleName, locale))
        {
            resources = BUNDLES.get(locale);
            if (resources == null)
            {
                resources = new ArrayList<>();
            }

            resources.add(ResourceBundle.getBundle(baseBundleName, locale));
            BUNDLES.put(locale, resources);
        }
        else
        {
            log.info(String.format("Already registered bundle base file name: '%s', locale: '%s'", baseBundleName, locale));
        }
    }

    /**
     * Clears all registered resource bundle.
     */
    @Synchronized
    public static final void clear()
    {
        initialize();

        BUNDLES.clear();
    }

    @Synchronized
    public static final String getMessage(final @NonNull Enum<? extends IBundle> key, Object... parameters)
    {
        initialize();

        throw new NotImplementedException();
    }

    /**
     * Returns the number of resource bundle files registered for the current locale.
     * <p>
     * @return Number of resource bundle registered.
     */
    public static final int getCount()
    {
        return getCount(locale);
    }

    /**
     * Returns the number of resource bundle files registered for a given locale.
     * <p>
     * @param locale Locale.
     * @return Number of resource bundle registered.
     */
    public static final int getCount(final @NonNull Locale locale)
    {
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
     * Returns the list of bundle file base name registered for a given locale.
     * <p>
     * @param locale Locale.
     * @return List of resource bundle base name registered.
     */
    public static final List<String> getList(final @NonNull Locale locale)
    {
        List<String> result = new ArrayList<>();
        List<ResourceBundle> resources = BUNDLES.get(locale);

        for (ResourceBundle bundle : resources)
        {
            result.add(bundle.getBaseBundleName());
        }

        return result;
    }
}
