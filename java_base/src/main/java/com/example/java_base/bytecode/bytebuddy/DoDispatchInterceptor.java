package com.example.java_base.bytecode.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

public class DoDispatchInterceptor {
    @RuntimeType
    public static Object intercept(@Argument(0) HttpServletRequest request, @SuperCall Callable<?> callable) throws Exception {
        final StringBuilder in = new StringBuilder();
        if (request.getParameterMap() != null && request.getParameterMap().size() > 0) {
            request.getParameterMap().keySet().forEach(key -> in.append("key=" + key + "_value=" + request.getParameter(key) + ","));
        }
        long agentStart = System.currentTimeMillis();
        try {
            return callable.call();
        }  finally {
            System.out.println("path:" + request.getRequestURI() + " 入参:" + in + " 耗时:" + (System.currentTimeMillis() - agentStart));
        }
    }
}

