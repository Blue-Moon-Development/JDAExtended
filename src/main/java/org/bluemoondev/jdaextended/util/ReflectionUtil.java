/*
*	jdaextended
*	Copyright (C) 2019 Matt
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*	Contact: matt@bluemoondev.org
 */
package org.bluemoondev.jdaextended.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> ReflectionUtil.java
 *
 * <p>
 * Utility methods for ease of use with reflection
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class ReflectionUtil {

    public static Set<Method> getMethodsAnnotatedBy(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> result = new HashSet<Method>();
        for (Method m : methods) {
            if (m.getAnnotation(annotation) == null) {
                continue;
            }
            result.add(m);
        }

        return result;
    }

    public static Set<Class<?>> getClassesAnnotatedBy(String packageName, Class<? extends Annotation> annotation) {
        ClassPath cp = null;
        try {
            cp = ClassPath.from(ClassLoader.getSystemClassLoader());
        } catch (IOException e) {
            Debug.error("Failed to create ClassPath from default system class loader", e);
        }

        ImmutableSet<ClassInfo> info = cp.getTopLevelClassesRecursive(packageName);
        Set<Class<?>> result = new HashSet<Class<?>>();

        info.forEach(action -> {
            try {
                if (Class.forName(action.getName()).isAnnotationPresent(annotation)) {
                    result.add(Class.forName(action.getName()));
                }
            } catch (ClassNotFoundException e) {
                Debug.error("Attempted to load a class that could not be found. Class: " + action.getName(), e);
            }
        });

        return result;
    }
}
