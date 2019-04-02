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

/**
 * Interface intended to be implemented by all exception enumeration classes.
 * <p>
 * This interface ease exceptions creation by using enumerated values when instantiating an exception such as
 * in the following example:
 * <p>
 * <b>Code sample:</b><p>
 * <code>
 * {
 *    ...
 *    throw new ApplicationException(ApplicationExceptionType.ALREADY_RUNNING);
 *    ...
 * }
 * </code>
 * <p>
 * The use of this syntax requires that the {@code ApplicationExceptionType} enumeration class implements
 * the {@code IException} interface.
 * <hr>
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse - Kakoo
 * @version 1.0.0
 */
public interface IException
{
    /**
     * Returns the message associated with the underlying enumerated value.
     * <hr>
     * @return Message.
     */
    String getMessage();
}