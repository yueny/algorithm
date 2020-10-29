package com.yueny.study.jdk.collection.union;

import java.util.List;

/**
 * 集合的交集(AnB)、并集(AuB)、差集(A-B)、交集的补集 (AuB) - (AnB)、去重并集 计算
 */
public interface ICollectionUnoinService {
    /**
     * 求2个集合的交集(AnB)
     *
     * @param ls
     * @param compare
     * @param <T>
     * @return
     */
    <T> List<T> intersect(List<T> ls, List<T> compare);

    /**
     * 求2个集合的并集(AuB)， 不排序，不去重
     *
     * @param ls
     * @param compare
     * @param <T>
     * @return
     */
    <T> List<T> union(List<T> ls, List<T> compare);

    /**
     * 求 ls 和 compare 的差集, 即ls 中有，但 compare 中没有的
     *
     * @param ls
     * @param compare
     * @return
     */
    <T> List<T> diff(List<T> ls, List<T> compare);

    /**
     * 交集的补集 (AuB) - (AnB)， 既并集 减 交集
     */
    <T> List<T> disjunction(List<T> ls, List<T> compare);

    // 去重并集
}
