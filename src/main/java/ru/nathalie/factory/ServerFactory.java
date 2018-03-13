package ru.nathalie.factory;

import ru.nathalie.config.AppProperties;
import ru.nathalie.db.ConnectionPool;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.*;

public class ServerFactory {
    private Map<String, Object> factory = new HashMap<>();
    private LinkedList<Class> classNeedsArgs = new LinkedList<>();

    public ServerFactory() {
        factory.put(DataSource.class.getName(), new ConnectionPool(new AppProperties()).setPool());
    }

    public Object setClass(Class newClass) throws Exception {
        if (factory.containsKey(newClass.getName())) {
            return factory.get(newClass.getName());
        } else {
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

            while (!classNeedsArgs.isEmpty()) {
                Class subClass = classNeedsArgs.getFirst();
                constructor = subClass.getConstructors();
                parameters = constructor[0].getParameterTypes();

                classesList.clear();
                isFull = true;
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
        }
        return factory.get(newClass.getName());
    }
}