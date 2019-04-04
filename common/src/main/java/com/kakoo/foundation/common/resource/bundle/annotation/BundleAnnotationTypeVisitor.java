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
package com.kakoo.foundation.common.resource.bundle.annotation;

import com.kakoo.foundation.common.annotation.visitor.IAnnotationTypeVisitor;
import com.kakoo.foundation.common.resource.bundle.IBundle;
import com.kakoo.foundation.common.resource.bundle.ResourceBundleManager;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Class used to automatically detect classes annotated with the {@link Bundle} annotation.
 * <hr>
 * @author  <a href="mailto:christophe.resse@gmail.com">Resse Christophe - Kakoo</a>
 * @version 1.0.0
 */
@Log4j
public final class BundleAnnotationTypeVisitor implements IAnnotationTypeVisitor
{
    /**
     * Collection of detected annotated types with the @Bundle annotation (grouped by priority).
     */
    private Map<Integer, List<String>> bundleAnnotated = new TreeMap<>();

    /**
     * Collection of detected annotated types with the @Bundle annotation (grouped by priority).
     */
    private Map<Class<? extends Annotation>, Map<Integer,  List<String>>> annotated = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] annotations()
    {
        return new Class[] { Bundle.class };
    }

    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotationClass, String annotatedClassName)
    {
        if (annotationClass == Bundle.class)
        {
            reportTypeAnnotationBundle(annotatedClassName);
        }
    }

    @Override
    public void delegateRegistration()
    {
        Map<Integer, List<String>> ordered;
        List<String> classes;

        for (Class<? extends Annotation> annotationClass : annotated.keySet())
        {
            ordered = annotated.get(annotationClass);
            for (Integer priority : ordered.keySet() )
            {
                classes = ordered.get(priority);
                for (String annotatedClass : classes)
                {
                    callForRegistration(annotationClass, annotatedClass);
                }
            }
        }
    }

    /**
     * Call the resource bundle manager to register the given annotated class for the given annotation.
     * <hr>
     * @param annotationClass Annotation class.
     * @param annotatedClassName Name of the annotated class.
     */
    private void callForRegistration(final @NonNull Class<? extends Annotation> annotationClass, final @NonNull String annotatedClassName)
    {
        try
        {
            ResourceBundleManager.register(annotationClass, Class.forName(annotatedClassName));
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Reports annotated types with the @Bundle annotation.
     * <p>
     * @param annotatedClassName Annotated class name.
     */
    @SuppressWarnings("unchecked")
    private void reportTypeAnnotationBundle(final @NonNull  String annotatedClassName)
    {
        List<String> classes;
        Map<Integer, List<String>> ordered;

        try
        {
            Class<? extends IBundle> bundleAnnotatedClass = (Class<? extends IBundle>) Class.forName(annotatedClassName);
            Bundle bundle = bundleAnnotatedClass.getAnnotation(Bundle.class);

            ordered = annotated.get(Bundle.class);
            if (ordered == null)
            {
                ordered = new TreeMap<>();
            }

            classes = ordered.get(bundle.priority());
            if (classes == null)
            {
                classes = new ArrayList<>();
            }

            classes.add(annotatedClassName);
            ordered.put(bundle.priority(), classes);
            annotated.put(Bundle.class, ordered);
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage());
        }
    }

}


