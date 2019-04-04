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
package com.kakoo.foundation.common.resource.properties.test;

import com.kakoo.foundation.common.resource.bundle.ResourceBundleException;
import com.kakoo.foundation.common.resource.bundle.ResourceBundleManager;
import lombok.extern.log4j.Log4j;
import org.junit.*;

import java.util.Locale;

import static org.junit.Assert.fail;

/**
 * A test case for some of the {@code easy-props} annotations.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Kakoo</a>
 * @version 1.0.0
 * see {@link ResourceBundleManager}
 */
@Log4j
public final class TestPropertiesAnnotations
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
     * Test the retrieving of a I18N property on an annotated field in the current locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveKeyI18NPropertyCurrentLocale()
    {
        TestEasyPropertiesObject object = new TestEasyPropertiesObject();
        Assert.assertEquals(getExpectedYellowColorName(), object.getColorName());
    }

    /**
     * Test the retrieving of a I18N property on an annotated field in specific locale.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testRetrieveKeyI18NPropertyOtherLocale()
    {
        Locale.setDefault(Locale.ITALIAN);

        // Property value is injected at object construction time! So it's important to change the locale before the
        // object is created.
        TestEasyPropertiesObject object = new TestEasyPropertiesObject();
        Assert.assertEquals(getExpectedYellowColorName(), object.getColorName());
    }

    /**
     * Test the retrieving of a system property.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testGetSystemProperty()
    {
        TestEasyPropertiesObject object = new TestEasyPropertiesObject();
        if (object.getOperatingSystemName().isEmpty())
        {
            fail("System property: os.name should not be empty!");
        }
    }

    /**
     * Test the retrieving of a simple property.
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testGetProperty()
    {
        TestEasyPropertiesObject object = new TestEasyPropertiesObject();
        Assert.assertEquals("1.2.3", object.getVersion());
    }

    /**
     * Returns the yellow color name according to the current locale.
     * <p>
     * @return Yellow color name in current language.
     */
    private String getExpectedYellowColorName()
    {
        String expected;

        switch (ResourceBundleManager.getLocale().getLanguage())
        {
            case "en":
                expected = "Yellow";
                break;

            case "de":
                expected = "Gelb";
                break;

            case "fr":
                expected = "Jaune";
                break;

            case "it":
                expected = "Giallo";
                break;

            case "es":
                expected = "Amarillo";
                break;

            default:
                throw new ResourceBundleException(String.format("Unhandled: '%s' locale", ResourceBundleManager.getLocale()));
        }

        return expected;
    }
}
