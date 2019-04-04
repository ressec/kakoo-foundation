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
package com.kakoo.foundation.common.resource.bundle.test;

import com.kakoo.foundation.common.resource.bundle.KakooFoundationCommonBundle;
import com.kakoo.foundation.common.resource.bundle.ResourceBundleException;
import com.kakoo.foundation.common.resource.bundle.ResourceBundleManager;
import lombok.extern.log4j.Log4j;
import org.junit.*;

import java.util.Locale;
import java.util.MissingResourceException;

import static org.junit.Assert.fail;

/**
 * A test case for the resource bundle manager.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Kakoo</a>
 * @version 1.0.0
 * see {@link ResourceBundleManager}
 */
@Log4j
public final class TestResourceBundleManager
{
    /**
     * Initialization of the test cases.
     * <p>
     * @throws Exception In case an error occurs during the initialization.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception
    {
        // Empty
    }

    /**
     * Finalization of the test cases.
     * <p>
     * @throws Exception In case an error occurs during the finalization.
     */
    @AfterClass
    public static final void tearDownAfterClass() throws Exception
    {
        // Empty
    }

    /**
     * Sets up the fixture.
     * <p>
     * @throws Exception In case an error occurs during the setup phase.
     */
    @Before
    public final void setUp() throws Exception
    {
        // Empty
    }

    /**
     * Tears down the fixture.
     * <p>
     * @throws Exception In case an error occurs during the tear down phase.
     */
    @After
    public final void tearDown() throws Exception
    {
        // Empty
    }

    /**
     * Test the registration of an existing bundle file for the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRegisterExistingBundleCurrentLocale()
    {
        try
        {
            ResourceBundleManager.clear();
            ResourceBundleManager.register("i18n/kakoo-foundation-common");
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    /**
     * Test the registration of an existing bundle file for a given locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRegisterExistingBundleOtherLocale()
    {
        try
        {
            ResourceBundleManager.clear();
            ResourceBundleManager.register("i18n/kakoo-foundation-common", Locale.FRENCH);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    /**
     * Test the cleaning of the resource bundle manager cache.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = ResourceBundleException.class)
    public final void testClearCache()
    {
        ResourceBundleManager.register("i18n/fruits");
        ResourceBundleManager.get("fruit.apple.name");
        ResourceBundleManager.clear();
        ResourceBundleManager.get("fruit.apple.name");
    }

    /**
     * Test the retrieving of the number of resource bundles for the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveBundleCountCurrentLocale()
    {
        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/kakoo-foundation-common");
        ResourceBundleManager.register("i18n/fruits");

        Assert.assertEquals(2, ResourceBundleManager.getCount());
    }

    /**
     * Test the retrieving of the list of registered resource bundle base name for the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveBundleListCurrentLocale()
    {
        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/kakoo-foundation-common");
        ResourceBundleManager.register("i18n/fruits");

        Assert.assertEquals(true, ResourceBundleManager.getList().size() == 2);
    }

    /**
     * Test the registration of a non-existing bundle file for the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = MissingResourceException.class)
    public final void testRegisterNotExistingBundle()
    {
        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/non-existent-bundle");
    }

    /**
     * Test the retrieving of a resource bundle key for the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveKeyCurrentLocale()
    {
        ResourceBundleManager.register("i18n/kakoo-foundation-common");
        Assert.assertEquals(getExpectedDummyLanguageValue(), ResourceBundleManager.get("kakoo-foundation-common.test.dummy.language"));
    }

    /**
     * Test the retrieving of a resource bundle key in a given locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveKeyOtherLocale()
    {
        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/kakoo-foundation-common", Locale.FRENCH);
        Assert.assertEquals("Français", ResourceBundleManager.get("kakoo-foundation-common.test.dummy.language", Locale.FRENCH));
    }

    /**
     * Test the retrieving of a resource bundle non existing key in the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = ResourceBundleException.class)
    public final void testRetrieveNotExistingKeyCurrentLocale()
    {
        // Set the default locale of the JVM.
        ResourceBundleManager.setLocale(Locale.getDefault());

        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/kakoo-foundation-common");
        ResourceBundleManager.get("kakoo-foundation-common.test.dummy.value");
    }

    /**
     * Test the retrieving of a resource bundle non existing key in a given locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = ResourceBundleException.class)
    public final void testRetrieveNotExistingKeyOtherLocale()
    {
        ResourceBundleManager.clear();
        ResourceBundleManager.register("i18n/kakoo-foundation-common");
        ResourceBundleManager.get("kakoo-foundation-common.test.dummy.value", Locale.GERMAN);
    }

    /**
     * Returns the expected dummy language value according to the current locale.
     * <p>
     * @return Dummy language value.
     */
    private String getExpectedDummyLanguageValue()
    {
        String expected;

        switch (ResourceBundleManager.getLocale().getLanguage())
        {
            case "en":
                expected = "English";
                break;

            case "de":
                expected = "Deutsch";
                break;

            case "fr":
                expected = "Français";
                break;

            case "it":
                expected = "Italiano";
                break;

            case "es":
                expected = "Español";
                break;

            default:
                throw new ResourceBundleException(String.format("Unhandled: '%s' locale", ResourceBundleManager.getLocale()));
        }

        return expected;
    }

    /**
     * Test the retrieving of a resource bundle key through enumeration in the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveEnumKeyCurrentLocale()
    {
        ResourceBundleManager.clear();
        String value = ResourceBundleManager.get(KakooFoundationCommonBundle.TEST_DUMMY);
        String other = KakooFoundationCommonBundle.TEST_DUMMY.getValue();
    }

    /**
     * Test the retrieving of a resource bundle key through enumeration in a given locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveEnumKeyOtherLocale()
    {
        String expected = "Eine Nachricht von der Komponente: kakoo-foundation-common";

        ResourceBundleManager.clear();
        Assert.assertEquals(expected, ResourceBundleManager.get(KakooFoundationCommonBundle.TEST_DUMMY, Locale.GERMAN));
    }

    /**
     * Test the retrieving of a resource bundle key through enumeration in a given locale for which the resource
     * bundle file in the given locale does not exist.
     * <p>
     * It should retrieve the value from the default bundle file and should not fail.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveEnumKeyOtherLocaleNotExistingBundle()
    {
        if (ResourceBundleManager.get(KakooFoundationCommonBundle.TEST_DUMMY, Locale.CHINESE).isEmpty())
        {
            fail("Should not be empty!");
        }
    }

    /**
     * Test the retrieving of a resource bundle key through enumeration with message formatting.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveEnumKeyCurrentLocaleFormatted()
    {
        String expected = "Die gewählte Farbe ist: 'gelb' und die ausgewählte Frucht ist: 'Erdbeere'";

        String message = ResourceBundleManager.get(KakooFoundationCommonBundle.TEST_DUMMY_MESSAGE_FORMATTED, Locale.GERMAN, "gelb", "Erdbeere");
        Assert.assertEquals(expected, message);
    }
}
