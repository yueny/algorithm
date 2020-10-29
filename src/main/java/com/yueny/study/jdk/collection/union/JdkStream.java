package com.yueny.study.jdk.collection.union;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jdk  Stream 的操作
 */
public class JdkStream implements ICollectionUnoinService {

    public static void main(String[] args) {
        JdkStream jdk = new JdkStream();

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
    public <T> List<T> intersect(List<T> ls, List<T> compare) {
        return ls.stream().filter(item -> compare.contains(item)).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> union(List<T> ls, List<T> compare) {
        //并集， 但不去重
        List<T> listAll = ls.parallelStream().collect(Collectors.toList());
        List<T> listAll2 = compare.parallelStream().collect(Collectors.toList());
        listAll.addAll(listAll2);

        return listAll;
    }

    @Override
    public <T> List<T> diff(List<T> ls, List<T> compare) {
        return ls.stream().filter(item -> !compare.contains(item)).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> disjunction(List<T> ls, List<T> compare) {
        List<T> union = union(ls, compare);
        union.removeAll(intersect(ls, compare));

        return union;
    }
}
