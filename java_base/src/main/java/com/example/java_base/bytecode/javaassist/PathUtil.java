package com.example.java_base.bytecode.javaassist;

public class PathUtil {
    /**
     * 类似getPath(Class), 只是不包含类的路径,而是获取到当前类包的根路径。
     * @param clazz 类
     * @return 转换后的路径
     */
    public static String getRootPath(Class clazz, final String classPath) {
        String uriPath = clazz.getResource("/").toString();
        return uriPath.replace("file:", "");
    }
}

