package com.yueny.study.jdk.collection.union;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * jdk  list 的操作
 */
public class JdkList implements ICollectionUnoinService {

    public static void main(String[] args) {
        JdkList jdk = new JdkList();

        List<String> list1 = new ArrayList<String>();
        list1.add("1");
        list1.add("2"); //
        list1.add("3"); //
        list1.add("5");
        list1.add("6");

        List<String> list2 = new ArrayList<String>();
        list2.add("2"); //
        list2.add("3"); //
        list2.add("7");
        list2.add("8");


        // 差集 (list1 - list2)
        List<String> lt1 = jdk.diff(list1, list2);
        System.out.println("差集 (list1 - list2)");
        System.out.println(lt1);

        // 差集 (list2 - list1)
        List<String> lt2 = jdk.diff(list2, list1);
        System.out.println("差集 (list2 - list1)");
        System.out.println(lt2);

        // 交集 intersection
        List<String> lt3 = jdk.intersect(list1, list2);
        System.out.println("交集 intersection");
        System.out.println(lt3);

        // 并集
        List<String> listAll = jdk.union(list1, list2);
        System.out.println("并集");
        System.out.println(listAll);

        // 交集的补集
        List<String> disjunction = jdk.disjunction(list1, list2);
        System.out.println("交集的补集 (AuB) - (AnB)");
        System.out.println(disjunction);

        // 去重并集
    }

    @Override
    public <T> List<T> diff(List<T> ls, List<T> compare) {
        /**
         * 构造一个相同大小的空集合
         *
         * new Object[ls.size()] 的意义在于直接分配相应的内存空间， 直接 new ArrayList(ls.size())  是不行的
         */
        List<T> list = new ArrayList(Arrays.asList(new Object[ls.size()]));

        // 将 ls 的数据克隆至 list。
        Collections.copy(list, ls);

        // 移除  compare  数据
        list.removeAll(compare);

        return list;
    }

    @Override
    public <T> List<T> disjunction(List<T> ls, List<T> compare) {
        List<T> union = union(ls, compare);
        union.removeAll(intersect(ls, compare));

        return union;
    }

    @Override
    public <T> List<T> intersect(List<T> ls, List<T> compare) {
        List<T> list = new ArrayList(Arrays.asList(new Object[ls.size()]));

        Collections.copy(list, ls);
        list.retainAll(compare);
        return list;
    }

    @Override
    public <T> List<T> union(List<T> ls, List<T> compare) {
        //并集， 去重
        List<T> list = new ArrayList(Arrays.asList(new Object[ls.size()]));

        //将ls的值拷贝一份到list中
        Collections.copy(list, ls);
        list.removeAll(compare);
        list.addAll(compare);

        return list;
    }

}
