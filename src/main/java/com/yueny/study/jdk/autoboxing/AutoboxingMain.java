package com.yueny.study.jdk.autoboxing;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.util.Assert;

/**
 * 自动装箱拆箱
 *
 * @author fengyang <deep_blue_yang@126.com>
 * @date 2020/9/29  6:24 下午
 * @title: AutoboxingMain
 * @projectName algorithm
 * @description:
 */
public class AutoboxingMain {
    public static void main(String[] args) {
        // testInteger();
        testDouble();

//        Boolean.valueOf(1).booleanValue()
//        Long.valueOf(1).longValue()
//        Integer.valueOf(1).intValue()
//        Character.valueOf(1).charValue()
//        Byte.valueOf(1).charValue()
    }

    private static void testInteger(){
        Integer i1 = 100;
        Integer i2 = 100;

        Integer i3 = 200;
        Integer i4 = 200;

        Assert.isTrue(i1 == i2, "如果此处报错说明返回了 false。");
        System.out.println(i1 == i2);  //true

        Assert.isTrue(i3!=i4, "如果此处报错说明返回了 false");
        System.out.println(i3==i4);  //false
    }

    private static void testDouble(){
        Double i1 = 100d;
        Double i2 = 100d;

        Double i3 = 200d;
        Double i4 = 200d;

        Assert.isTrue(i1 == i2, "如果此处报错说明返回了 false。");
        System.out.println(i1 == i2);  //true

        Assert.isTrue(i3!=i4, "如果此处报错说明返回了 false");
        System.out.println(i3==i4);  //false
    }
}
