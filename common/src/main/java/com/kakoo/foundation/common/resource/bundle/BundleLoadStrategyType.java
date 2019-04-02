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

/**
 * Enumeration of the resource bundle load strategy types.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Kakoo</a>
 * @version 1.0.0
 */
public enum BundleLoadStrategyType
{
    /**
     * Strict bundle load strategy.
     * <p>
     * When set to STRICT, the resource bundle file must exist in the language set in the resource bundle manager. If
     * not, an exception will be raised.
     */
    STRICT,

    /**
     * Lenient bundle load strategy.
     * <p>
     * When set to LENIENT, if the resource bundle file does not exist in the language set in the resource bundle
     * manager, a default one will be loaded instead. If no default resource bundle file is found, an exception will
     * be raised.
     */
    LENIENT;
}
