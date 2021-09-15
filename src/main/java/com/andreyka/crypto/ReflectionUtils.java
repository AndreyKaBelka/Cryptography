package com.andreyka.crypto;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ReflectionUtils {
    private static Class<?>[] getService(Class<?> service) {
        Reflections reflections = new Reflections(ReflectionUtils.class.getPackageName());
        return reflections.getSubTypesOf(service).toArray(Class<?>[]::new);
    }

    public static <T> T getInstance(Class<T> clazz, String alg) throws NoSuchAlgorithmException {
        try {
            Class<?>[] classes = getService(clazz);
            for (Class<?> aClass : classes) {
                if (aClass.isAnnotationPresent(Algorithm.class)) {
                    Algorithm algorithm = aClass.getAnnotation(Algorithm.class);
                    if (algorithm.value().equals(alg)) {
                        return (T) aClass.getDeclaredConstructor().newInstance();
                    }
                }
            }
            throw new NoSuchAlgorithmException();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NoSuchAlgorithmException();
        }
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException();
        }
    }

    public static Object getMethodResult(Class<?> clazz, String methodName, Object... parameters) throws NoSuchMethodException {
        Class<?>[] types = Arrays.stream(parameters).map(Object::getClass).toArray(Class[]::new);
        Method method = getMethod(clazz, methodName, types);
        try {
            method.setAccessible(true);
            return method.invoke(null, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NoSuchMethodException();
        }
    }
}
