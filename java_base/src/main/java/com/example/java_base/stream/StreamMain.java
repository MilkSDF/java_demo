package com.example.java_base.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhouhs
 */
@Slf4j
public class StreamMain {

    public static void main(String[] args)  {
       ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("AA","BB"));
       arrayList.stream().forEach(System.out::println);
    }

    public String printTest() {
        log.info("aa");
        return this.toString();
    }
}
