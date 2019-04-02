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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
     * Collection of bundles to auto-register (by priority).
     */
    private Map<Integer, List<String>> files = new TreeMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Annotation>[] annotations()
    {
        return new Class[] { Bundle.class };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className)
    {
        List<String> classes = null;
        Integer key = null;

        Class<? extends IBundle> bClass;
        try
        {
            bClass = (Class<? extends IBundle>) Class.forName(className);
            Bundle a = bClass.getAnnotation(Bundle.class);
            key = a.priority();
            classes = files.containsKey(key) ? files.get(key) : new ArrayList<>();
            classes.add(className);
            files.put(Integer.valueOf(a.priority()), classes);
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage());
        }
    }

    @Override
    public void delegateRegistration()
    {
        files.entrySet()
                .stream()
                .forEach(e -> e.getValue()
                        .stream()
                        .forEach(this::callForRegistration));
    }

    /**
     * Call the resource bundle manager to register the given class annotated with
     * the {@link Bundle} annotation.
     * <hr>
     * @param className Name of the class annotated with the {@link Bundle} annotation.
     */
    private void callForRegistration(final @NonNull String className)
    {
        try
        {
            ResourceBundleManager.register(Class.forName(className));
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage(), e);
        }
    }
}


