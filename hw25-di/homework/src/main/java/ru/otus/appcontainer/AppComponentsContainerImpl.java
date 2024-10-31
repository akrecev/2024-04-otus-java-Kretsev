package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object appConfigInstance = createConfigInstance(configClass);
        List<Method> methodList = getSortedConfigMethods(configClass);

        for (Method method : methodList) {
            Object component = createComponent(appConfigInstance, method);
            String componentName = method.getAnnotation(AppComponent.class).name();

            addComponent(componentName, component);
        }
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AppComponentsContainerException("App config class creation error.", e);
        }
    }

    private List<Method> getSortedConfigMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private Object createComponent(Object appConfigInstance, Method method) {
        try {
            Object[] parameters = getMethodParameters(method);
            return method.invoke(appConfigInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error creating component for method: " + method.getName(), e);
        }
    }

    private Object[] getMethodParameters(Method method) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> getAppComponent(parameter.getType()))
                .toArray();
    }

    private void addComponent(String componentName, Object component) {
        checkDuplicateComponent(componentName);
        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void checkDuplicateComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new AppComponentsContainerException(String.format("Component %s is duplicate.", componentName));
        }
    }
}
