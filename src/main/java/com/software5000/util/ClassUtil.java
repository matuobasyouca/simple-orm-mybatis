package com.software5000.util;

import com.github.pagehelper.util.StringUtil;
import com.software5000.util.bean.BasicType;
import com.software5000.util.bean.Singleton;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Java Class与反射相关的一些工具类
 */
public final class ClassUtil {

    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static final Map<String, String> reverseAbbreviationMap;

    static {
        final Map<String, String> m = new HashMap<String, String>();
        m.put("int", "I");
        m.put("boolean", "Z");
        m.put("float", "F");
        m.put("long", "J");
        m.put("short", "S");
        m.put("byte", "B");
        m.put("double", "D");
        m.put("char", "C");
        final Map<String, String> r = new HashMap<String, String>();
        for (final Map.Entry<String, String> e : m.entrySet()) {
            r.put(e.getValue(), e.getKey());
        }
        reverseAbbreviationMap = Collections.unmodifiableMap(r);
    }

    /**
     * 获取类加载器
     */
    public static ClassLoader overridenClassLoader;

    /**
     * 获取类加载器
     *
     * @return 类加载器
     */
    public static ClassLoader getContextClassLoader() {
        return overridenClassLoader != null ?
                overridenClassLoader : Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取指定类的全部属性字段
     *
     * @param className    需要获取的类名
     * @param extendsField 是否获取接口或父类中的公共属性
     * @return 属性字段数组
     */
    public final static String[] getField(String className, boolean extendsField) {
        Class classz = loadClass(className);
        Field[] fields = classz.getFields();
        Set<String> set = new HashSet<>();
        if (fields != null) {
            for (Field f : fields) {
                set.add(f.getName());
            }
        }
        if (extendsField) {
            Field[] fieldz = classz.getDeclaredFields();
            if (fieldz != null) {
                for (Field f : fieldz) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中的公共属性
     *
     * @param className    需要获取的类名
     * @param extendsField 是否获取接口或父类中的公共属性
     * @return 属性字段数组
     */
    public final static String[] getPublicField(String className, boolean extendsField) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("public")) {
                    set.add(f.getName());
                }
            }
        }
        if (extendsField) {
            Field[] fieldz = classz.getFields();
            if (fieldz != null) {
                for (Field f : fieldz) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中定义的protected类型的属性字段
     *
     * @param className 需要获取的类名
     * @return protected类型的属性字段数组
     */
    public final static String[] getProtectedField(String className) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("protected")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中定义的private类型的属性字段
     *
     * @param className 需要获取的类名
     * @return private类型的属性字段数组
     */
    public final static String[] getPrivateField(String className) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("private")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部public类型方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getPublicMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("public")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }


    /**
     * 获取对象的全部protected类型方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getProtectedMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("protected")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部private类型方法
     *
     * @param className 需要获取的类名
     * @return 方法名数组
     */
    public final static String[] getPrivateMethod(String className) {
        Class classz = loadClass(className);
        Method[] methods = classz.getDeclaredMethods();
        Set<String> set = new HashSet<>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("private")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<>();
        if (methods != null) {
            for (Method f : methods) {
                set.add(f.getName());
            }
        }
        return set.toArray(new String[set.size()]);
    }


    /**
     * 调用对象的setter方法
     *
     * @param obj   对象
     * @param att   属性名
     * @param value 属性值
     * @param type  属性类型
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException    异常
     */
    public final static void setter(Object obj, String att, Object value, Class<?> type)
            throws InvocationTargetException, IllegalAccessException {
        try {
            String name = att.substring(0, 1).toUpperCase() + att.substring(1);
            Method met = obj.getClass().getMethod("set" + name, type);
            met.invoke(obj, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @return 类的完整名称
     */
    public final static List<String> getClassNameByJar(String jarPath) {
        List<String> myClassName = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myClassName;
    }


    /**
     * 加载指定的类
     *
     * @param className 需要加载的类
     * @return 加载后的类
     */
    public final static Class loadClass(String className) {
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return theClass;
    }

    /**
     * 获取一个类的父类
     *
     * @param className 需要获取的类
     * @return 父类的名称
     */
    public final static String getSuperClass(String className) {
        Class classz = loadClass(className);
        Class superclass = classz.getSuperclass();
        return superclass.getName();
    }

    /**
     * 获取一个雷的继承链
     *
     * @param className 需要获取的类
     * @return 继承类名的数组
     */
    public final static String[] getSuperClassChian(String className) {
        Class classz = loadClass(className);
        List<String> list = new ArrayList<>();
        Class superclass = classz.getSuperclass();
        String superName = superclass.getName();
        if (!"java.lang.Object".equals(superName)) {
            list.add(superName);
            list.addAll(Arrays.asList(getSuperClassChian(superName)));
        } else {
            list.add(superName);
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取一类实现的全部接口
     *
     * @param className         需要获取的类
     * @param extendsInterfaces 话说getInterfaces能全部获取到才对，然而测试的时候父类的接口并没有
     *                          因此就多出了这参数
     * @return 实现接口名称的数组
     */
    public final static String[] getInterfaces(String className, boolean extendsInterfaces) {
        Class classz = loadClass(className);
        List<String> list = new ArrayList<>();
        Class[] interfaces = classz.getInterfaces();
        if (interfaces != null) {
            for (Class inter : interfaces) {
                list.add(inter.getName());
            }
        }
        if (extendsInterfaces) {
            String[] superClass = getSuperClassChian(className);
            for (String c : superClass) {
                list.addAll(Arrays.asList(getInterfaces(c, false)));
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取短类名
     *
     * @param object      对象
     * @param valueIfNull 如果对象为空返回的字符串
     * @return 类名
     */
    public static String getShortClassName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass());
    }

    /**
     * 获取短类名
     *
     * @param cls class
     * @return 类名
     */
    public static String getShortClassName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getShortClassName(cls.getName());
    }

    /**
     * 获取短类名
     *
     * @param className 完整类名
     * @return 类名
     */
    public static String getShortClassName(String className) {
        if (ValidUtil.isEmpty(className)) {
            return "";
        }

        final StringBuilder arrayPrefix = new StringBuilder();

        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }

            if (reverseAbbreviationMap.containsKey(className)) {
                className = reverseAbbreviationMap.get(className);
            }
        }

        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        final int innerIdx = className.indexOf(
                INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        }
        return out + arrayPrefix;
    }

    /**
     * 通过反射,获得指定类的父类的泛型参数的实际类型. 如BuyerServiceBean extends DaoSupport
     *
     * @param clazz clazz 需要反射的类,该类必须继承范型父类
     * @param index 泛型参数所在索引,从0开始.
     * @return 范型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();// 得到泛型父类
        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
        // DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
        }
        if (!(params[index] instanceof Class<?>)) {
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    /**
     * 通过反射,获得指定类的父类的第一个泛型参数的实际类型. 如BuyerServiceBean extends DaoSupport
     *
     * @param clazz clazz 需要反射的类,该类必须继承泛型父类
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得方法返回值泛型参数的实际类型.
     *
     * @param method method 方法
     * @param index  index 泛型参数所在索引,从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getMethodGenericReturnType(Method method, int index) {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();
            if (index >= typeArguments.length || index < 0) {
                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            return (Class<?>) typeArguments[index];
        }
        return Object.class;
    }

    /**
     * 通过反射,获得方法返回值第一个泛型参数的实际类型.
     *
     * @param method method 方法
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getMethodGenericReturnType(Method method) {
        return getMethodGenericReturnType(method, 0);
    }

    /**
     * 通过反射,获得方法输入参数第index个输入参数的所有泛型参数的实际类型.
     *
     * @param method method 方法
     * @param index  index 第几个输入参数
     * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回空集合
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method, int index) {
        List<Class<?>> results = new ArrayList<Class<?>>();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (index >= genericParameterTypes.length || index < 0) {
            throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
        }
        Type genericParameterType = genericParameterTypes[index];
        if (genericParameterType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericParameterType;
            Type[] parameterArgTypes = aType.getActualTypeArguments();
            for (Type parameterArgType : parameterArgTypes) {
                Class<?> parameterArgClass = (Class<?>) parameterArgType;
                results.add(parameterArgClass);
            }
            return results;
        }
        return results;
    }

    /**
     * 通过反射,获得方法输入参数第一个输入参数的所有泛型参数的实际类型.
     *
     * @param method method 方法
     * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回空集合
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method) {
        return getMethodGenericParameterTypes(method, 0);
    }

    /**
     * 根据实际传入的对象以及对象属性名称，获取对应参数值
     *
     * @param entity    对象
     * @param fieldName 属性
     * @return 对应值
     */
    public static Object getValueByField(Object entity, String fieldName) {
        try {
            try {
                return entity.getClass().getDeclaredMethod(("get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1))).invoke(entity);
            } catch (NoSuchMethodException e) {
                Method method = entity.getClass().getSuperclass().getDeclaredMethod(("get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1)));
                return method.invoke(entity);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据实际传入的对象以及对象属性名称，设置对应参数值
     *
     * @param entity    传入对象
     * @param fieldName 属性名称
     * @param value     属性值
     * @return 对象
     * @throws IllegalAccessException    设置可能出现异常
     * @throws IllegalArgumentException  设置可能出现异常
     * @throws InvocationTargetException 设置可能出现异常
     * @throws NoSuchMethodException     设置可能出现异常
     * @throws NoSuchFieldException      设置可能出现异常
     */
    public static Object setValueByField(Object entity, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        try {
            return entity.getClass().getDeclaredMethod(("set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1)), entity.getClass().getDeclaredField(fieldName).getType()).invoke(entity, value);
        } catch (NoSuchFieldException e) {
            return entity.getClass().getSuperclass().getDeclaredMethod(("set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1)), entity.getClass().getSuperclass().getDeclaredField(fieldName).getType()).invoke(entity, value);
        }
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型.
     *
     * @param field field 字段
     * @param index index 泛型参数所在索引,从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getFieldGenericType(Field field, int index) {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (index >= fieldArgTypes.length || index < 0) {
                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            return (Class<?>) fieldArgTypes[index];
        }
        return Object.class;
    }

    /**
     * 通过反射,获得Field泛型参数的实际类型.
     *
     * @param field field 字段
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     * <code>Object.class</code>
     */
    public static Class<?> getFieldGenericType(Field field) {
        return getFieldGenericType(field, 0);
    }

    /**
     * 根据实体得到实体的所有属性
     *
     * @param objClass         实体
     * @param withSuperclass   是否包含父类
     * @param filterAnnotation 过滤的注释，即被filterAnnotation 注释的的属性会被过滤掉
     * @return 属性数组
     */
    public static String[] getColumnNames(Class<?> objClass, boolean withSuperclass, Class filterAnnotation) {
        return getColumnNamesAsString(objClass, withSuperclass, filterAnnotation).split(",");
    }

    /**
     * 根据实体得到实体的所有属性
     *
     * @param objClass         实体
     * @param withSuperclass   是否包含父类
     * @param filterAnnotation 过滤的注释
     * @return 属性字符串
     */
    public static String getColumnNamesAsString(Class<?> objClass, boolean withSuperclass, Class<? extends Annotation> filterAnnotation) {
        List<Field> fields = new ArrayList<>();
        if (withSuperclass) {
            fields.addAll(Arrays.asList(objClass.getSuperclass().getDeclaredFields()));
        }
        fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
        if (filterAnnotation != null) {
            return fields.stream().filter(f -> f.getAnnotation(filterAnnotation) == null).map(Field::getName).reduce((a, b) -> a + "," + b).get();
        } else {
            return fields.stream().map(Field::getName).reduce((a, b) -> a + "," + b).get();
        }
    }

    /**
     * 是否为基本类型（包括包装类和原始类）
     *
     * @param clazz 类
     * @return 是否为基本类型
     */
    public static boolean isBasicType(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 是否为包装类型
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return BasicType.wrapperPrimitiveMap.containsKey(clazz);
    }

    /**
     * 实例化对象
     *
     * @param <T>   类型
     * @param clazz 类名
     * @return 对象
     * @throws Exception 异常
     */
    public static <T> T newInstance(String clazz) throws Exception {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new Exception(String.format("Instance class [%s] error!", clazz), e);
        }
    }

    /**
     * 实例化对象
     *
     * @param <T>   类型
     * @param clazz 类
     * @return 对象
     * @throws Exception 异常
     */
    public static <T> T newInstance(Class<T> clazz) throws Exception {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new Exception(String.format("Instance class [%s] error!", clazz), e);
        }
    }

    /**
     * 实例化对象
     *
     * @param <T>    类型
     * @param clazz  类
     * @param params 参数
     * @return 对象
     * @throws Exception 异常
     */
    public static <T> T newInstance(Class<T> clazz, Object... params) throws Exception {
        if (params == null || params.length == 0) {
            return newInstance(clazz);
        }

        final Class<?>[] paramTypes = getClasses(params);
        final Constructor<?> constructor = getConstructor(clazz, getClasses(params));
        if (null == constructor) {
            throw new Exception(String.format("No Constructor matched for parameter types: [%s]", new Object[]{paramTypes}));
        }
        try {
            return getConstructor(clazz, paramTypes).newInstance(params);
        } catch (Exception e) {
            throw new Exception(String.format("Instance class [%s] error!", clazz), e);
        }
    }

    /**
     * 查找类中的指定参数的构造方法
     *
     * @param <T>            类型
     * @param clazz          类
     * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可
     * @return 构造方法，如果未找到返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        if (null == clazz) {
            return null;
        }

        final Constructor<?>[] constructors = clazz.getConstructors();
        Class<?>[] pts;
        for (Constructor<?> constructor : constructors) {
            pts = constructor.getParameterTypes();
            if (isAllAssignableFrom(pts, parameterTypes)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    /**
     * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
     *
     * @param types1 类组1
     * @param types2 类组2
     * @return 是否相同、父类或接口
     */
    public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
        if ((types1 == null || types1.length == 0) && (types2 == null || types2.length == 0)) {
            return true;
        }
        if (types1.length == types2.length) {
            for (int i = 0; i < types1.length; i++) {
                if (false == types1[i].isAssignableFrom(types2[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 加载类
     *
     * @param <T>           类型
     * @param className     类名
     * @param isInitialized 是否初始化
     * @return 类
     * @throws ClassNotFoundException 未找到类
     */
    public static <T> Class<T> loadClass(String className, boolean isInitialized) throws ClassNotFoundException {
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(className, isInitialized, getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return clazz;
    }

    /**
     * 获得对象数组的类数组
     *
     * @param objects 对象数组
     * @return 类数组
     */
    public static Class<?>[] getClasses(Object... objects) {
        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }
    // ---------------------------------------------------------------------------------------------------- Invoke start

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     * 非单例模式，如果是非静态方法，每次创建一个新对象
     *
     * @param <T>                    类型
     * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
     * @param args                   参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws Exception 执行可能出现异常
     */
    public static <T> T invoke(String classNameDotMethodName, Object[] args) throws Exception {
        return invoke(classNameDotMethodName, false, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     *
     * @param <T>                    类型
     * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
     * @param isSingleton            是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
     * @param args                   参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws Exception 执行可能出现异常
     */
    public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object[] args) throws Exception {
        if (ValidUtil.isEmpty(classNameDotMethodName)) {
            throw new Exception("Blank classNameDotMethodName!");
        }
        final int dotIndex = classNameDotMethodName.lastIndexOf('.');
        if (dotIndex <= 0) {
            throw new Exception(String.format("Invalid classNameDotMethodName [%s]!", classNameDotMethodName));
        }

        final String className = classNameDotMethodName.substring(0, dotIndex);
        final String methodName = classNameDotMethodName.substring(dotIndex + 1);

        return invoke(className, methodName, isSingleton, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     * 非单例模式，如果是非静态方法，每次创建一个新对象
     *
     * @param <T>        类型
     * @param className  类名，完整类路径
     * @param methodName 方法名
     * @param args       参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws Exception 执行可能出现异常
     */
    public static <T> T invoke(String className, String methodName, Object[] args) throws Exception {
        return invoke(className, methodName, false, args);
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     *
     * @param <T>         类型
     * @param className   类名，完整类路径
     * @param methodName  方法名
     * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
     * @param args        参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws Exception 执行可能出现异常
     */
    public static <T> T invoke(String className, String methodName, boolean isSingleton, Object[] args) throws Exception {
        Class<Object> clazz = loadClass(className);
        try {
            return invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), methodName, args);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     *
     * @param <T>        类型
     * @param obj        对象
     * @param methodName 方法名
     * @param args       参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     * @throws Exception 执行可能出现异常
     */
    public static <T> T invoke(Object obj, String methodName, Object[] args) throws Exception {
        try {
            final Method method = getDeclaredMethod(obj, methodName, args);
            if (null == method) {
                throw new NoSuchMethodException(String.format("No such method: [%s]", methodName));
            }
            return invoke(obj, method, args);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获得指定类中的Public方法名<br>
     * 去重重载的方法
     *
     * @param clazz 类
     * @return set
     */
    public static Set<String> getDeclaredMethodNames(Class<?> clazz) {
        HashSet<String> methodSet = new HashSet<String>();
        Method[] methodArray = getDeclaredMethods(clazz);
        for (Method method : methodArray) {
            String methodName = method.getName();
            methodSet.add(methodName);
        }
        return methodSet;
    }

    /**
     * 获得声明的所有方法，包括本类及其父类和接口的所有方法和Object类的方法
     *
     * @param clazz 类
     * @return 方法数组
     */
    public static Method[] getDeclaredMethods(Class<?> clazz) {
        Set<Method> methodSet = new HashSet<>();
        Method[] declaredMethods;
        for (; null != clazz; clazz = clazz.getSuperclass()) {
            declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                methodSet.add(method);
            }
        }
        return methodSet.toArray(new Method[methodSet.size()]);
    }

    /**
     * 执行方法
     *
     * @param obj    对象
     * @param method 方法（对象方法或static方法都可）
     * @param args   参数对象
     * @param <T>    类型
     * @return 结果
     * @throws InvocationTargetException 目标方法执行异常
     * @throws IllegalAccessException    参数异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (false == method.isAccessible()) {
            method.setAccessible(true);
        }
        try {
            return (T) method.invoke(isStatic(method) ? null : obj, args);
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    /**
     * 是否为静态方法
     *
     * @param method 方法
     * @return 是否为静态方法
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
     *
     * @param obj        被查找的对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法
     * @throws NoSuchMethodException 无此方法
     * @throws SecurityException     权限异常
     */
    public static Method getDeclaredMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, SecurityException {
        return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
    }

    /**
     * 查找指定类中的所有方法（包括非public方法），也包括父类和Object类的方法
     *
     * @param clazz          被查找的类
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法
     * @throws NoSuchMethodException 无此方法
     * @throws SecurityException     权限访问异常
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method = null;
        for (; null != clazz; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                break;
            } catch (NoSuchMethodException e) {
                // 继续向上寻找
            }
        }
        return method;
    }

    /**
     * 获得ClassPath
     *
     * @return ClassPath
     */
    public static String getClassPath() {
        return getClassPathURL().getPath();
    }

    /**
     * 获得ClassPath URL
     *
     * @return ClassPath URL
     */
    public static URL getClassPathURL() {
        return getURL("");
    }

    /**
     * 获得资源的URL
     *
     * @param resource 资源（相对Classpath的路径）
     * @return 资源URL
     */
    public static URL getURL(String resource) {
        return ClassUtil.getContextClassLoader().getResource(resource);
    }
}

