package com.example.java_base.bytecode.javaassist;

import javassist.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws CannotCompileException, IOException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();

        // 1. 创建一个空类
        final String classPath = "com.github.houbb.spring.aop.javassist.GenClass";
        CtClass cc = pool.makeClass(classPath);

        // 2. 新增一个字段 private String name = "init";
        // 字段名为name
        CtField param = new CtField(pool.get("java.lang.String"), "name", cc);
        // 访问级别是 private
        param.setModifiers(Modifier.PRIVATE);
        // 初始值是 "init"
        cc.addField(param, CtField.Initializer.constant("init"));

        // 3. 生成 getter、setter 方法
        cc.addMethod(CtNewMethod.setter("setName", param));
        cc.addMethod(CtNewMethod.getter("getName", param));

        // 4. 添加无参的构造函数
        CtConstructor cons = new CtConstructor(new CtClass[]{}, cc);
        cons.setBody("{name = \"ryo\";}");
        cc.addConstructor(cons);

        // 5. 添加有参的构造函数
        // http://jboss-javassist.github.io/javassist/tutorial/tutorial2.html#before
        cons = new CtConstructor(new CtClass[]{pool.get("java.lang.String")}, cc);
        // $0=this / $1,$2,$3... 代表方法参数
        cons.setBody("{$0.name = $1;}");
        cc.addConstructor(cons);

        // 6. 创建一个名为execute方法，无参数，无返回值，输出name值
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "execute", new CtClass[]{}, cc);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(name);}");
        cc.addMethod(ctMethod);

        final String targetPath = PathUtil.getRootPath(Main.class, classPath);
        cc.writeFile(targetPath);
    }
}

