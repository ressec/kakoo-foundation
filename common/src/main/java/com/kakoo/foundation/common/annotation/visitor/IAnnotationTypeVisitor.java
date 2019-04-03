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
package com.kakoo.foundation.common.annotation.visitor;

import eu.infomas.annotation.AnnotationDetector;

/**
 * Extend the {@link eu.infomas.annotation.AnnotationDetector.TypeReporter} interface to be able to detect by reflection
 * classes annotated with specific annotations.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Kakoo</a>
 * @version 1.0.0
 */
public interface IAnnotationTypeVisitor extends AnnotationDetector.TypeReporter
{
    /**
     * Delegates registration of annotated classes.
     * <hr>
     * @throws ClassNotFoundException Thrown in case an error occurred while trying to
     * delegate the registration of the annotated class.
     */
    void delegateRegistration() throws ClassNotFoundException;
}
