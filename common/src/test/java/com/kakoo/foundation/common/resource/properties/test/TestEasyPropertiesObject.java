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

import com.kakoo.foundation.common.resource.bundle.ResourceBundleManager;
import lombok.Getter;
import lombok.Setter;
import org.jeasy.props.annotations.I18NProperty;
import org.jeasy.props.annotations.Property;
import org.jeasy.props.annotations.SystemProperty;

public class TestEasyPropertiesObject
{
    /**
     * Color name.
     */
    @Getter
    @Setter
    @I18NProperty(bundle = "i18n/colors", key = "color.yellow.name")
    private String colorName;

    /**
     * Operating System version.
     */
    @Getter
    @Setter
    @SystemProperty(value = "os.version")
    private String operatingSystemVersion;

    /**
     * Operating System name.
     */
    @Getter
    @Setter
    @SystemProperty(value = "os.name")
    private String operatingSystemName;

    @Getter
    @Setter
    @Property(source = "properties/test.properties", key = "simple.property.version")
    private String version;

    /**
     * Creates a new {@link TestEasyPropertiesObject} object.
     */
    public TestEasyPropertiesObject()
    {
        // Make the resource bundle manager inject into the object instance the properties declared through annotations.
        ResourceBundleManager.injectProperties(this);
    }
}
