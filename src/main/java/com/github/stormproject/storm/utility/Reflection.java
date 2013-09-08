package com.github.stormproject.storm.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A helper class to make reflection more bearable
 *
 * @author Icyene
 */
public class Reflection {

    public static <T> FieldContainer<T> field(String name) {
        return new FieldContainer<T>(name);
    }

    public static <T> MethodContainer<T> declaredMethod(String name) {
        return new MethodContainer<T>(name, true);
    }

    public static <T> MethodContainer<T> method(String name) {
        return new MethodContainer<T>(name, false);
    }

    public static <T> ConstructorContainer<T> constructor() {
        return new ConstructorContainer<T>();
    }

    public static class MethodContainer<T> extends Container {

        private Class<?>[] param;
        private boolean declared;

        public MethodContainer(String name, boolean declared) {
            super(name);
            this.declared = declared;
        }

        public <T> MethodContainer<T> withReturnType(Class<T> clazz) {
            MethodContainer<T> clone = new MethodContainer<T>(name, declared);
            clone.param = param;
            clone.target = target;
            clone.in = in;
            return clone;
        }

        public MethodContainer<T> in(Object clazz) {
            if (!(clazz instanceof Class)) {
                in = clazz;
                target = clazz.getClass();
            } else {
                target = (Class<?>) clazz;
            }
            return this;
        }

        public MethodContainer<T> withParameters(Class<?>... args) {
            param = args;
            return this;
        }

        public T invoke(Object... args) {
            try {
                return (T) getRaw().invoke(in, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Method getRaw() {
            try {
                Method raw;
                if (param != null && param.length > 0) {
                    raw = declared ? target.getDeclaredMethod(name, param) : target.getMethod(name, param);
                } else {
                    raw = declared ? target.getDeclaredMethod(name) : target.getMethod(name);
                }
                raw.setAccessible(true);
                return raw;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class ConstructorContainer<T> extends Container {

        private Class<?>[] param;

        public ConstructorContainer<T> withParameters(Class<?>... args) {
            param = args;
            return this;
        }

        public <T> ConstructorContainer<T> in(Class<T> clazz) {
            ConstructorContainer<T> clone = new ConstructorContainer<T>();
            clone.name = name;
            clone.param = param;
            clone.target = clazz;
            return clone;
        }

        public T newInstance(Object... args) {
            try {
                return (T) getRaw().newInstance(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Constructor<?> getRaw() {
            try {
                Constructor<?> raw = (param != null && param.length > 0) ? target.getDeclaredConstructor(param) : target.getDeclaredConstructor();
                raw.setAccessible(true);
                return raw;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class FieldContainer<T> extends Container {

        protected static Field modifiersField;

        public FieldContainer(String name) {
            super(name);
            try {
                modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public <T> FieldContainer<T> ofType(Class<T> clazz) {
            FieldContainer<T> clone = new FieldContainer<T>(name);
            clone.target = target;
            clone.in = in;
            return clone;
        }

        public FieldContainer<T> in(Object clazz) {
            if (!(clazz instanceof Class)) {
                in = clazz;
                target = clazz.getClass();
            } else {
                target = (Class<?>) clazz;
            }
            return this;
        }

        public void set(Object object) {
            try {
                Field field = target.getDeclaredField(name);
                access(field);
                field.set(in, object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public T get() {
            try {
                return (T) getRaw().get(in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Field getRaw() {
            try {
                Field raw = target.getDeclaredField(name);
                access(raw);
                return raw;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void access(Field field) throws IllegalAccessException {
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);
        }
    }

    public static class Container {

        protected String name;
        protected Object in;
        protected Class<?> target;

        public Container(String name) {
            this.name = name;
        }

        public Container() {
        }
    }
}