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
import lombok.Getter;

/**
 * Enumeration of the resource bundle keys of the <b>Kakoo Common</b> component. Each enumerated value maps to a key
 * in the corresponding resource bundle file.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Kakoo</a>
 * @version 1.0.0
 */
@SuppressWarnings("nls")
@Bundle(file="i18n/kakoo-foundation-common", root="kakoo-foundation-common.", priority = 1) // This one must be loaded first!
public enum KakooFoundationCommonBundle implements IBundle
{
    /*
     * ---------- Test.* ----------
     */

    /**
     * A dummy test message.
     */
    TEST_DUMMY("test.dummy"),

    /**
     * A dummy test message for the language.
     */
    TEST_DUMMY_LANGUAGE("test.dummy.language"),

    /**
     * A dummy test formatted message.
     */
    TEST_DUMMY_MESSAGE_FORMATTED("test.dummy.message.formatted"),

    /*
     * ---------- Resource.Bundle.* ----------
     */

    /**
     * The test resource bundle key that does not exists in the resource bundle file.
     */
    RESOURCE_BUNDLE_KEYDOESNOTEXIST("resource.bundle.keyDoesNotExist"),

    /**
     * The given resource bundle cannot be found.
     */
    RESOURCE_BUNDLE_NOTFOUND("resource.bundle.notFound"),

    /**
     * An error occurred with the given resource bundle.
     */
    RESOURCE_BUNDLE_ERROR("resource.bundle.error"),

    /**
     * The given resource bundle has been registered.
     */
    RESOURCE_BUNDLE_REGISTERED("resource.bundle.registered"),

    /**
     * The given resource bundle has been replaced.
     */
    RESOURCE_BUNDLE_REPLACED("resource.bundle.replaced"),

    /**
     * The given resource bundle is already registered.
     */
    RESOURCE_BUNDLE_ALREADYREGISTERED("resource.bundle.alreadyRegistered"),

    /**
     * The given resource bundle file name cannot be null or empty.
     */
    RESOURCE_BUNDLE_INVALIDNAME("resource.bundle.invalidName"),

    /**
     * The given resource bundle key cannot be null or empty.
     */
    RESOURCE_BUNDLE_INVALIDKEY("resource.bundle.invalidKey"),

    /**
     * The given resource bundle locale cannot be null.
     */
    RESOURCE_BUNDLE_INVALIDLOCALE("resource.bundle.invalidLocale"),

    /**
     * Resource bundle configuration file cannot be null or empty
     */
    RESOURCE_BUNDLE_INVALIDCONFIGURATION("resource.bundle.invalidConfiguration");

    /**
     * Resource bundle key.
     */
    @Getter
    private final String key;

    /**
     * Creates a new enumerated value based on the resource bundle key.
     * <p>
     * @param key Resource bundle key.
     */
    KakooFoundationCommonBundle(final String key)
    {
        this.key = key;
    }

    @Override
    public final String getValue()
    {
        return ResourceBundleManager.get(this);
    }
}

