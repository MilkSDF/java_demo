package com.example.java_base.jdksource;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class ArrayListBaseTest {
    public void xray(ArrayList list) {
        Class clazz = list.getClass();
        try {
            Field elementData = clazz.getDeclaredField("elementData");
            elementData.setAccessible(true);
            Object[] objects = (Object[]) elementData.get(list);
            Field sizeField = clazz.getDeclaredField("size");
            sizeField.setAccessible(true);

            int size = 0;
            for (int i = 0; i < objects.length; i++) {
                if (Objects.nonNull(objects[i])) {
                    ++size;
                }
            }
            System.out.println("length = " + objects.length
                    + ", size = " + sizeField.get(list)
                    + ", arraySize = " + size);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAtTail() {
        int initialCapacity = 8;
        ArrayList<Integer> integers = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity * 2; i++) {
            xray(integers);
            integers.add(i);
        }
    }
}
