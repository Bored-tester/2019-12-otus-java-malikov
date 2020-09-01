package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.model.AppComponentItem;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    //    a map of initialized app components with their names mapped by the class of the interface they implement
    private final Map<Class<?>, AppComponentItem> appComponentItemsMapByClass = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final List<Method> appComponentInitializers = Arrays.stream(configClass.getMethods())
                .filter(method ->
                        Arrays.stream(method.getDeclaredAnnotations())
                                .map(Annotation::annotationType)
                                .anyMatch(annotationClass -> annotationClass.equals(AppComponent.class)))
                .collect(Collectors.toList());

//      going to prepare map for initialization where key is load priority and value is a list of methods to initialize
        final Map<Integer, List<Method>> appComponentInitializationMap = prepareInitializationMap(appComponentInitializers);

        initAppComponents(appComponentInitializationMap, configClass);
    }

    private Map<Integer, List<Method>> prepareInitializationMap(List<Method> appComponentInitializers) {
        final Map<Integer, List<Method>> appComponentInitializationMap = new HashMap<>();
        appComponentInitializers.forEach(appComponentInitializer -> {
            int priority = appComponentInitializer.getAnnotation(AppComponent.class).order();
            if (appComponentInitializationMap.containsKey(priority))
                appComponentInitializationMap.get(priority).add(appComponentInitializer);
            else {
                List<Method> initQue = new ArrayList<>();
                initQue.add(appComponentInitializer);
                appComponentInitializationMap.put(priority, initQue);
            }
        });
        return appComponentInitializationMap;
    }

    private void initAppComponents(Map<Integer, List<Method>> appComponentInitializationMap, Class<?> appConfigClass) {
        final Set<Integer> sortedPriorities = new TreeSet<>(appComponentInitializationMap.keySet());
        Object appConfig;
        try {
            appConfig = appConfigClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Failed to initialize class %s. It should have default public constructor without arguments", appConfigClass));
        }

        sortedPriorities.forEach(priority -> {
            appComponentInitializationMap.get(priority).forEach(appComponentInitializer -> {
                try {
                    Class<?> appComponentClass = appComponentInitializer.getReturnType();
                    if (appComponentItemsMapByClass.containsKey(appComponentClass))
                        throw new IllegalArgumentException(String.format("Failed to initialize app component in method %s. " +
                                "Duplicate component class %s.", appComponentInitializer.getName(), appComponentClass));

                    AppComponentItem appComponent = new AppComponentItem(
                            appComponentInitializer.getAnnotation(AppComponent.class).name(),
                            appComponentInitializer.invoke(appConfig, getArguments(appComponentInitializer)));
                    appComponentItemsMapByClass.put(appComponentInitializer.getReturnType(), appComponent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException(String.format("Failed to initialize app component in method %s. " +
                            "It should be public and working.", appComponentInitializer.getName()));
                }
            });
        });
    }

    private Object[] getArguments(Method appComponentInitializer) {
        Class<?>[] parameterTypes = appComponentInitializer.getParameterTypes();
        Object[] appComponentInitializationArguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!appComponentItemsMapByClass.containsKey(parameterTypes[i]))
                throw new IllegalArgumentException(String.format("Failed to initialize app component in method %s. " +
                        "Argument of class %s is not initialized.", appComponentInitializer.getName(), parameterTypes[i].getName()));
            appComponentInitializationArguments[i] = appComponentItemsMapByClass.get(parameterTypes[i]).getItemValue();
        }
        return appComponentInitializationArguments;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponentItemsMapByClass.get(componentClass).getItemValue();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return appComponentItemsMapByClass.values().stream()
                .filter(appComponent -> appComponent.getName().equals(componentName))
                .map(appComponent -> (C) appComponent.getItemValue())
                .findFirst()
                .orElse(null);
    }
}
