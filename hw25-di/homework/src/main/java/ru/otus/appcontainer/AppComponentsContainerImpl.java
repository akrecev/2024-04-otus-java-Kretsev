package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.AppComponentsContainerException;

@SuppressWarnings({"squid:S1068", "java:S112"})
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        List<Class<?>> initialConfigClassList = Arrays.stream(initialConfigClasses)
                .filter(configClass -> configClass.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparing(configClass -> configClass
                        .getAnnotation(AppComponentsContainerConfig.class)
                        .order()))
                .toList();

        for (Class<?> initialConfigClass : initialConfigClassList) {
            processConfig(initialConfigClass);
        }
    }

    private void processConfig(Class<?> configClass) {

        checkConfigClass(configClass);
        Object appConfigInstance;
        try {
            appConfigInstance = configClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AppComponentsContainerException("App config class creation error.", e);
        }

        List<Method> methodList = Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        Object component;
        String componentName;

        for (Method method : methodList) {
            try {
                List<Object> list = new ArrayList<>();
                for (Parameter parameter : method.getParameters()) {
                    Class<?> type = parameter.getType();
                    Object appComponent = getAppComponent(type);
                    list.add(appComponent);
                }
                Object[] objects = list.toArray();

                component = method.invoke(appConfigInstance, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            componentName = method.getAnnotation(AppComponent.class).name();
            checkDuplicateComponent(componentName);

            appComponents.add(component);

            appComponentsByName.put(componentName, component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();

        if (components.isEmpty()) {
            throw new AppComponentsContainerException("Component is absent in a container");
        } else if (components.size() > 1) {
            throw new AppComponentsContainerException("Component is more than one in a container");
        }

        return (C) components.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new AppComponentsContainerException(String.format("Component %s is absent.", componentName));
        }
        return component;
    }

    private void checkDuplicateComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new AppComponentsContainerException(String.format("Component %s is duplicate.", componentName));
        }
    }
}