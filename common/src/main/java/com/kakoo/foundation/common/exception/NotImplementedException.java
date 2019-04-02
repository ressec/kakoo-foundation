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
package com.kakoo.foundation.common.exception;

import com.kakoo.foundation.common.resource.bundle.IBundle;

/**
 * Exception thrown to indicate a service is not yet implemented.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Hemajoo</a>
 * @version 1.0.0
 */
public class NotImplementedException extends AbstractUncheckedException
{
    /**
     * Serialization identifier.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Exception thrown to indicate a service is not yet implemented.
     */
    public NotImplementedException()
    {
        super();
    }

    /**
     * Exception thrown to indicate a service is not yet implemented.
     * <p>
     * @param key Resource bundle key (enumerated value coming from an
     * enumeration implementing the {@link IBundle} interface).
     */
    public NotImplementedException(final Enum<? extends IBundle> key)
    {
        super(key);
    }

    /**
     * Exception thrown to indicate a service is not yet implemented.
     * <p>
     * @param key Exception key (enumerated value coming from an enumeration
     * implementing the {@link IException} interface).
     * @param parameters List of parameters used to populate the exception
     * message.
     */
    public NotImplementedException(final Enum<?> key, final Object... parameters)
    {
        super(key, parameters);
    }

    /**
     * Exception thrown to indicate a service is not yet implemented.
     * <p>
     * @param exception Parent exception.
     */
    public NotImplementedException(final Exception exception)
    {
        super(exception);
    }

    /**
     * Exception thrown to indicate a service is not yet implemented.
     * <p>
     * @param message Message describing the error being the cause of the raised
     * exception.
     */
    public NotImplementedException(final String message)
    {
        super(message);
    }

    /**
     * Exception thrown to indicate a service is not yet implemented.
     * <p>
     * @param message Message describing the error being the cause of the raised
     * exception.
     * @param exception Parent exception.
     */
    public NotImplementedException(final String message, final Exception exception)
    {
        super(message, exception);
    }
}
