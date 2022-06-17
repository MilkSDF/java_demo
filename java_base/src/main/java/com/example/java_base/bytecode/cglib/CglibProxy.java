package com.example.java_base.bytecode.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib intercept start...");
        //通过代理子类调用父类的方法
        Object object = methodProxy.invokeSuper(o, objects);
        System.out.println("cglib intercept end...");
        return object;
    }

    /**
     * 获取代理类
     * @param clazz 类信息
     * @return 代理类结果
     */
    public Object getProxy(Class clazz){
        Enhancer enhancer = new Enhancer();
        //目标对象类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术创建目标对象类的子类实例作为代理
        return enhancer.create();
    }

}

