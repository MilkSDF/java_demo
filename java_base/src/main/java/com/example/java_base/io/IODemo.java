package com.example.java_base.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class IODemo {

    public static void main(String[] args) throws IOException {
        IODemo ioDemo =new IODemo();
        ioDemo.readMMAP();
    }


    public void readFileChannel() throws IOException {
        FileChannel fileChannel = new RandomAccessFile(new File("db.data"), "rw").getChannel();
        // 写
        byte[] data = new byte[4096];
        long position = 1024L;
        // 指定 position 写入 4kb 的数据
        fileChannel.write(ByteBuffer.wrap(data), position);
        // 从当前文件指针的位置写入 4kb 的数据
        fileChannel.write(ByteBuffer.wrap(data));

        // 读
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        // 指定 position 读取 4kb 的数据
        fileChannel.read(buffer,position);
        // 从当前文件指针的位置读取 4kb 的数据
        fileChannel.read(buffer);

    }

    public void readMMAP() throws IOException {
        FileChannel fileChannel = new RandomAccessFile(new File("db.data"), "rw").getChannel();
//        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, (long) (1.5 * 1024 * 1024 * 1024));
        // 写
        byte[] data = new byte[4];
        int position = 8;
        // 从当前 mmap 指针的位置写入 4b 的数据
        mappedByteBuffer.put(data);
        // 指定 position 写入 4b 的数据
        MappedByteBuffer subBuffer = (MappedByteBuffer) mappedByteBuffer.slice();
        subBuffer.position(position);
        subBuffer.put(data);

        // 读
        // 从当前 mmap 指针的位置读取 4b 的数据
        mappedByteBuffer.get(data);
        // 指定 position 读取 4b 的数据
        subBuffer = (MappedByteBuffer) mappedByteBuffer.slice();
        subBuffer.position(position);
        subBuffer.get(data);

    }


    //回收 mappedByteBuffer
    public static void clean(MappedByteBuffer mappedByteBuffer) {
        ByteBuffer buffer = mappedByteBuffer;
        if (buffer == null || !buffer.isDirect() || buffer.capacity()== 0)
            return;
        invoke(invoke(viewed(buffer), "cleaner"), "clean");
    }

    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private static Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }
        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }




}
