package com.yueny.study.jdk.collection.union;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * apache 的操作
 */
public class Apache implements ICollectionUnoinService {

    public static void main(String[] args) {
        Apache apache = new Apache();

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
        List<String> lt1 = apache.diff(list1, list2);
        System.out.println("差集(A-B) (list1 - list2)");
        System.out.println(lt1);

        // 差集 (list2 - list1)
        List<String> lt2 = apache.diff(list2, list1);
        System.out.println("差集(A-B) (list2 - list1)");
        System.out.println(lt2);

        // 交集 intersection
        List<String> lt3 = apache.intersect(list1, list2);
        System.out.println("交集(AnB) intersection");
        System.out.println(lt3);

        // 并集
        List<String> listAll = apache.union(list1, list2);
        System.out.println("并集(AuB)");
        System.out.println(listAll);

        // 交集的补集
        List<String> disjunction = apache.disjunction(list1, list2);
        System.out.println("交集的补集 (AuB) - (AnB)");
        System.out.println(disjunction);

        // 去重并集
    }

    @Override
    public <T> List<T> diff(List<T> ls, List<T> compare) {
        //集合相减
        return new ArrayList<>(CollectionUtils.subtract(ls, compare));
    }

    @Override
    public <T> List<T> disjunction(List<T> ls, List<T> compare) {
        // 交集的补集
        return new ArrayList<>(CollectionUtils.disjunction(ls, compare));
    }

    @Override
    public <T> List<T> intersect(List<T> ls, List<T> compare) {
        //交集
        return new ArrayList<>(CollectionUtils.intersection(ls, compare));
    }

    @Override
    public <T> List<T> union(List<T> ls, List<T> compare) {
        //并集， 去重
        return new ArrayList<>(CollectionUtils.union(ls, compare));
    }
}
