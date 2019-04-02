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

import com.kakoo.foundation.common.exception.InvalidArgumentException;
import com.kakoo.foundation.common.exception.NotImplementedException;
import com.kakoo.foundation.common.resource.bundle.annotation.Bundle;
import com.kakoo.foundation.common.resource.bundle.annotation.BundleAnnotationTypeVisitor;
import eu.infomas.annotation.AnnotationDetector;
import lombok.*;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j;
import org.jeasy.props.api.PropertiesInjector;
import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@Log4j
public class ResourceBundleManager
{
    /**
     * The dot character.
     */
    @SuppressWarnings("nls")
    private static final String CHARACTER_DOT = ".";

    /**
     * Default fall-back locale.
     */
    private static Locale defaultLocale = Locale.ENGLISH;

    /**
     * Locale of manager (set to english by default).
     */
    private static Locale locale = ResourceBundleManager.defaultLocale;

    /**
     * Thread-safe collection of all resource bundle entries.
     */
    private static final Map<String, String> ENTRIES = new ConcurrentHashMap<>(1000);

    /**
     * Collection of all resource bundle file names.
     */
    private static final List<String> BUNDLES = new ArrayList<>();

    /**
     * Bundle load strategy type.
     */
    private static BundleLoadStrategyType strategy = BundleLoadStrategyType.LENIENT;

    /**
     * Properties injector.
     */
    private PropertiesInjector propertiesInjector;

    /**
     * Is the manager initialized?
     */
    private static boolean isInitialized = false;

    /**
     * Is the manager initializing?
     */
    private static boolean isInitializing = false;

    // Static minimal initialization of the resource bundle manager.
    static
    {
        // Forces the JVM to have the same language default than the resource bundle manager.
        Locale.setDefault(defaultLocale);
    }

    /**
     * Initializes the resource bundle manager if not already done automatically.
     * <p>
     * If no locale has been set using the {@link #setLocale(Locale)}, then the default {@code Locale} used
     * is set to {@code en_US}.
     * @throws ResourceBundleManagerException Thrown if the initialization of the resource bundle manager has failed.
     */
    @Synchronized
    public static final void initialize()
    {
        if (!isInitialized && !isInitializing)
        {
            isInitializing = true;

            // Instantiates a properties injector for direct injection of properties.
            propertiesInjector = aNewPropertiesInjector();

            // Detect and register all annotated elements detected.
            detectAnnotated();

            isInitializing = false;
            isInitialized = true;
        }
    }

    /**
     * Sets the language used by the {@link ResourceBundleManager} and sets accordingly the JVM default language.
     * <p>
     * <b>Note:</b> Calling this service forces the
     * {@link ResourceBundleManager} to reload all the resource bundle files according to the new locale.
     * <p>
     * @param locale {@link Locale} corresponding to the new language to set.
     */
    @SuppressWarnings("nls")
    public static final void setLocale(final @NonNull Locale locale)
    {
        throw new NotImplementedException();
    }

    /**
     * Sets the default locale (for fall-back scenarios).
     * <p>
     * @param locale {@link Locale} corresponding to the new language to set.
     */
    public static final void setDefaultLocale(final @NonNull Locale locale)
    {
        ResourceBundleManager.defaultLocale = locale;
    }

    private static void reload()
    {
        throw new NotImplementedException();
    }

    private static void registerAnnotatedClasses()
    {
        throw new NotImplementedException();
    }

    private static void registerAnnotatedMethods()
    {
        throw new NotImplementedException();
    }

    private static void registerAnnotatedFields()
    {
        throw new NotImplementedException();
    }

    /**
     * Returns a resource bundle value given its key.
     * <p>
     * @param key Resource bundle key.
     * @return Resource bundle value.
     */
    public static final String getMessage(final @NonNull String key)
    {
        initialize();

        return retrieve(key);
    }

    /**
     * Returns a resource string from its key using an enumerated value.
     * <p>
     * @param key Resource string key (enumerated value from an enumeration
     * implementing the {@code IBundle} interface).
     * @return Resource string.
     */
    public static final String getMessage(final Enum<? extends IBundle> key)
    {
        initialize();

        return getMessage(key, (Object[]) null);
    }

    /**
     * Returns a message from a resource bundle file handled by the resource
     * bundle manager based on a given enumerated value representing the
     * resource key of the message to retrieve.
     * <p>
     * @param key Enumerated resource key.
     * @param parameters Parameters to inject in the message during message
     * formatting.
     * @return Message associated to the resource key or an exception message if
     * the corresponding resource string cannot be loaded.
     */
    public static final String getMessage(final @NonNull  Enum<? extends IBundle> key, final Object... parameters)
    {
        initialize();

        if (key == null)
        {
            throw new InvalidArgumentException(KakooFoundationCommonBundle.RESOURCE_BUNDLE_INVALIDKEY);
        }

        return retrieve(key, parameters);
    }

    /**
     * Retrieves a resource bundle value given its key.
     * <p>
     * @param key Resource bundle key to find.
     * @return Resource bundle value.
     */
    @SuppressWarnings("nls")
    private static String retrieve(final @NonNull String key)
    {
        throw new NotImplementedException();
    }

    /**
     * Retrieves a resource bundle value given its key.
     * <p>
     * @param key Resource bundle key to find.
     * @param parameters Parameters to inject while formatting the message.
     * @return Resource bundle value formatted.
     */
    @SuppressWarnings("nls")
    private static String retrieve(final Enum<? extends IBundle> key, final Object... parameters)
    {
        throw new NotImplementedException();
    }

    /**
     * Detects annotated elements.
     */
    private static void detectAnnotated()
    {
        try
        {
            BundleAnnotationTypeVisitor visitor = new BundleAnnotationTypeVisitor();
            final AnnotationDetector detector = new AnnotationDetector(visitor);
            detector.detect();

            visitor.delegateRegistration();
        }
        catch (Exception e)
        {
            isInitialized = false;
            throw new ResourceBundleException(e.getMessage(), e);
        }
    }

    /**
     * Registers directly a new resource bundle file.
     * <p>
     * @param filename Resource bundle file name to register.
     * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
     */
    @SuppressWarnings("nls")
    public static final void register(final String filename)
    {
        initialize();

        register(filename, "", ResourceBundleManager.locale);
    }

    /**
     * Registers directly a new resource bundle file.
     * <p>
     * @param filename Resource bundle file name to register.
     * @param root Root path to access the keys.
     * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
     */
    public static final void register(final @NonNull String filename, final @NonNull String root)
    {
        initialize();

        register(filename, root, ResourceBundleManager.locale);
    }

    /**
     * Registers directly a new resource bundle file.
     * <p>
     * @param filename Resource bundle file name to register.
     * @param locale Locale to use.
     * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
     */
    @SuppressWarnings({ "nls", "hiding" })
    public static final void register(final @NonNull String filename, final @NonNull Locale locale)
    {
        initialize();

        register(filename, "", locale);
    }

    /**
     * Registers directly a new resource bundle file.
     * <p>
     * @param filename Resource bundle file name to register.
     * @param root Root path to access the keys.
     * @param locale Locale to use.
     * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
     */
    public static final void register(final @NonNull String filename, final @NonNull String root, final @NonNull Locale locale)
    {
        String message;

        initialize();

        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(filename, locale);

            // Ensure the registered resource bundle locale matches the resource bundle manager current one.
            if (bundle.getLocale().getISO3Language().equals(ResourceBundleManager.locale.getISO3Language()))
            {
                loadResourceBundle(bundle);
            }
            else
            {
                if (ResourceBundleManager.strategy == BundleLoadStrategyType.LENIENT)
                {
                    // As the load strategy is set to LENIENT, let's try to load one with compatible locale.
                    bundle = ResourceBundle.getBundle(filename, ResourceBundleManager.locale);
                    if (bundle != null)
                    {
                        loadResourceBundle(bundle);
                    }
                }
                else
                {
                    // As the load strategy is set to STRICT, throw an exception.
                    message = String.format("Resource bundle: '%s' with language: '%s' ignored because it does not match resource bundle manager " +
                            "language set to: '%s'", filename, locale, ResourceBundleManager.locale);
                    log.error(message);
                    throw new ResourceBundleException(message);
                }
            }
        }
        catch (MissingResourceException e)
        {
            // The resource string file does not exist in the given language ... is it an error?
            log.error(e.getMessage(), e);
            throw new ResourceBundleException(e.getMessage(), e);
        }
    }

    /**
     * Registers a resource bundle file through an annotated class.
     * <p>
     * @param annotated Annotated class.
     * @throws ResourceBundleException Thrown if the resource bundle file cannot be found.
     */
    @SuppressWarnings("nls")
    public static final void register(final @NonNull Class<?> annotated)
    {
        initialize();

        // TODO Implement service body.
    }

    private static void loadResourceBundle(final @NonNull ResourceBundle bundle)
    {
        if (BUNDLES.contains(bundle.getBaseBundleName()))
        {
            log.info(String.format("Resource bundle: '%s' ignored because it is already registered", bundle.getBaseBundleName()));
        }
        else
        {
            BUNDLES.add(bundle.getBaseBundleName());
            mergeResourceBundleEntries(bundle);
            log.info(String.format("Resource bundle: '%s' registered with locale: '%s'", bundle.getBaseBundleName(), bundle.getLocale()));
        }
    }

    /**
     * Merge the entries of a resource bundle to the whole properties of the resource bundle manager.
     * <p>
     * @param bundle Resource bundle from which entries need to be merged.
     */
    private static void mergeResourceBundleEntries(final @NonNull ResourceBundle bundle)
    {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements())
        {
            String key = keys.nextElement();
            ENTRIES.putIfAbsent(key, bundle.getString(key));
        }
    }
}
