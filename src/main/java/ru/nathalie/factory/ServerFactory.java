package ru.nathalie.factory;

import ru.nathalie.config.AppProperties;
import ru.nathalie.db.ConnectionPool;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class ServerFactory {
    private Map<String, Object> factory = new HashMap<>();
    private LinkedList<Class> classNeedsArgs = new LinkedList<>();

    public ServerFactory() {
        AppProperties properties = new AppProperties();
        factory.put(properties.getClass().getName(), properties);
        factory.put(DataSource.class.getName(), new ConnectionPool(properties).setPool());
    }

    public Object setClass(Class newClass) throws IOException {
        if (factory.containsKey(newClass.getName())) {
            return factory.get(newClass.getName());
        } else {
            parseParameters(newClass);
            parseDependents();
        }
        return factory.get(newClass.getName());
    }

    private void parseParameters(Class newClass) throws IOException {
        try {
            Constructor[] constructor = newClass.getConstructors();
            Class[] parameters = constructor[0].getParameterTypes();

            boolean isFull = true;
            List<Object> classesList = new ArrayList<>();
            for (Class param : parameters) {
                if (factory.containsKey(param.getName())) {
                    classesList.add(factory.get(param.getName()));
                } else {
                    classNeedsArgs.add(param);
                    isFull = false;
                }
            }

            if (isFull) {
                Object object = constructor[0].newInstance(classesList.toArray());
                factory.put(newClass.getName(), object);
            } else {
                classNeedsArgs.addLast(newClass);
            }
        } catch (Exception e) {
            throw new IOException("Error during injection of bean " + newClass.getName());
        }
    }

    private void parseDependents() throws IOException {
        String className = "";
        try {
            while (!classNeedsArgs.isEmpty()) {
                Class subClass = classNeedsArgs.getFirst();
                className = subClass.getName();
                Constructor[] constructor = subClass.getConstructors();
                Class[] parameters = constructor[0].getParameterTypes();

                List<Object> classesList = new ArrayList<>();
                boolean isFull = true;
                for (Class param : parameters) {
                    if (factory.containsKey(param.getName())) {
                        classesList.add(factory.get(param.getName()));
                    } else {
                        classNeedsArgs.addFirst(param);
                        isFull = false;
                    }
                }
                if (isFull) {
                    Object object = constructor[0].newInstance(classesList.toArray());
                    factory.put(object.getClass().getName(), object);
                    classNeedsArgs.remove(subClass);
                }
            }
        } catch (Exception e) {
            throw new IOException("Error during injection of bean " + className);
        }
    }
}